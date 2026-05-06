package ru.yandex.practicum.comment.service;

import ru.yandex.practicum.comment.dto.UpdateCommentDto;

public record CommentsUpdateRequest(long userId, long commentId, UpdateCommentDto updateComment) {}
