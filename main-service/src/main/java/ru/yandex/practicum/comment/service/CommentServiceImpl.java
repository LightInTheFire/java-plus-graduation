package ru.yandex.practicum.comment.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import ru.yandex.practicum.comment.dto.CommentDto;
import ru.yandex.practicum.comment.mapper.CommentMapper;
import ru.yandex.practicum.comment.model.Comment;
import ru.yandex.practicum.comment.repository.CommentRepository;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.repository.EventRepository;
import ru.yandex.practicum.exception.ForbiddenAccessException;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.user.model.User;
import ru.yandex.practicum.user.repository.UserRepository;

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
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional(readOnly = true)
    public Collection<CommentDto> getAllCommentsPaged(CommentsPublicGetRequest request) {
        if (!eventRepository.existsById(request.eventId())) {
            throw new NotFoundException("Event with id=%d not found".formatted(request.eventId()));
        }

        Page<Comment> comments = commentRepository.findAllByEventId(request.eventId(), request.getPageable());

        return comments.stream()
            .map(comment -> CommentMapper.toCommentDto(comment, comment.getAuthor()))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<CommentDto> getAllCommentsOfUserPaged(CommentsPrivateGetRequest request) {
        User user = getUserByIdOrThrow(request.userId());

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
        User user = getUserByIdOrThrow(userId);
        Comment comment = getCommentByIdOrThrow(commentId);
        if (!comment.getAuthor()
            .getId()
            .equals(user.getId())) {
            throw new ForbiddenAccessException("You are not allowed to delete others comments");
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentDto createComment(CommentsCreateRequest request) {
        Event event = getEventByIdOrThrow(
            request.newComment()
                .eventId());
        User user = getUserByIdOrThrow(request.userId());

        Comment newComment = CommentMapper.toEntity(request.newComment(), user, event);
        Comment saved = commentRepository.save(newComment);
        return CommentMapper.toCommentDto(saved, user);
    }

    @Override
    public CommentDto updateComment(CommentsUpdateRequest request) {
        User user = getUserByIdOrThrow(request.userId());
        Comment comment = getCommentByIdOrThrow(request.commentId());
        if (!comment.getAuthor()
            .getId()
            .equals(user.getId())) {
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
        return CommentMapper.toCommentDto(comment, comment.getAuthor());
    }

    private Event getEventByIdOrThrow(Long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        return eventOptional.orElseThrow(NotFoundException.supplier("Event with id=%d not found", eventId));
    }

    private User getUserByIdOrThrow(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.orElseThrow(NotFoundException.supplier("User with id=%d not found", userId));
    }

    private Comment getCommentByIdOrThrow(Long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        return optionalComment.orElseThrow(NotFoundException.supplier("Comment with id=%d not found", commentId));
    }
}
