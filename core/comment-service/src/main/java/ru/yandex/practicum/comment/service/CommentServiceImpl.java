package ru.yandex.practicum.comment.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import ru.yandex.practicum.comment.dto.CommentDto;
import ru.yandex.practicum.comment.mapper.CommentMapper;
import ru.yandex.practicum.comment.model.Comment;
import ru.yandex.practicum.comment.repository.CommentRepository;
import ru.yandex.practicum.comment.repository.EventCommentCount;
import ru.yandex.practicum.event.client.EventClient;
import ru.yandex.practicum.exception.ForbiddenAccessException;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.user.client.UserClient;
import ru.yandex.practicum.user.dto.UserShortDto;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserClient userClient;
    private final EventClient eventClient;

    @Override
    @Transactional(readOnly = true)
    public Collection<CommentDto> getAllCommentsPaged(CommentsPublicGetRequest request) {
        Page<Comment> comments = commentRepository.findAllByEventId(request.eventId(), request.getPageable());
        List<Long> authorIds = comments.stream()
            .map(Comment::getAuthorId)
            .toList();
        List<UserShortDto> usersByIds = userClient.getUsersByIds(authorIds);
        Map<Long, UserShortDto> usersDtoMap = usersByIds.stream()
            .collect(Collectors.toMap(UserShortDto::id, Function.identity()));

        return comments.stream()
            .map(comment -> CommentMapper.toCommentDto(comment, usersDtoMap.get(comment.getAuthorId())))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<CommentDto> getAllCommentsOfUserPaged(CommentsPrivateGetRequest request) {
        UserShortDto user = userClient.getUser(request.userId());

        List<Comment> comments = commentRepository.findAllByAuthorId(request.userId(), request.getPageable());

        return comments.stream()
            .map(comment -> CommentMapper.toCommentDto(comment, user))
            .toList();
    }

    @Override
    public void deleteComment(long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public void deleteCommentByUser(long userId, long commentId) {
        UserShortDto user = userClient.getUser(userId);
        Comment comment = getCommentByIdOrThrow(commentId);
        if (!comment.getAuthorId()
            .equals(user.id())) {
            throw new ForbiddenAccessException("You are not allowed to delete others comments");
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentDto createComment(CommentsCreateRequest request) {
        boolean eventExists = eventClient.existsById(
            request.newComment()
                .eventId());
        if (!eventExists) {
            throw new NotFoundException(
                "Event with id %d not found".formatted(
                    request.newComment()
                        .eventId()));
        }
        UserShortDto user = userClient.getUser(request.userId());

        Comment newComment = CommentMapper.toEntity(
            request.newComment(),
            user.id(),
            request.newComment()
                .eventId());
        Comment saved = commentRepository.save(newComment);
        return CommentMapper.toCommentDto(saved, user);
    }

    @Override
    public CommentDto updateComment(CommentsUpdateRequest request) {
        UserShortDto user = userClient.getUser(request.userId());
        Comment comment = getCommentByIdOrThrow(request.commentId());
        if (!comment.getAuthorId()
            .equals(user.id())) {
            throw new ForbiddenAccessException("You are not allowed to update others comments");
        }

        comment.setText(
            request.updateComment()
                .text());
        comment.setEdited(true);
        Comment saved = commentRepository.save(comment);
        return CommentMapper.toCommentDto(saved, user);
    }

    @Override
    public CommentDto getById(Long commentId) {
        Comment comment = getCommentByIdOrThrow(commentId);
        UserShortDto author = userClient.getUser(comment.getAuthorId());
        return CommentMapper.toCommentDto(comment, author);
    }

    @Override
    public Map<Long, Long> countCommentsForEventIds(List<Long> eventIds) {
        return commentRepository.countCommentsByEventIds(eventIds)
            .stream()
            .collect(Collectors.toMap(EventCommentCount::eventId, EventCommentCount::count));
    }

    private Comment getCommentByIdOrThrow(Long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        return optionalComment.orElseThrow(NotFoundException.supplier("Comment with id=%d not found", commentId));
    }
}
