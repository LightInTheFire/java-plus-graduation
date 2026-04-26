package ru.yandex.practicum.comment.service;

import java.util.Collection;

import ru.yandex.practicum.comment.dto.CommentDto;

public interface CommentService {

    Collection<CommentDto> getAllCommentsPaged(CommentsPublicGetRequest request);

    Collection<CommentDto> getAllCommentsOfUserPaged(CommentsPrivateGetRequest request);

    void deleteComment(long commentId);

    void deleteCommentByUser(long userId, long commentId);

    CommentDto createComment(CommentsCreateRequest request);

    CommentDto updateComment(CommentsUpdateRequest request);

    CommentDto getById(Long commentId);
}
