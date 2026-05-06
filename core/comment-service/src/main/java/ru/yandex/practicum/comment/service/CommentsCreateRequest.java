package ru.yandex.practicum.comment.service;

import ru.yandex.practicum.comment.dto.NewCommentDto;

public record CommentsCreateRequest(long userId, NewCommentDto newComment) {}
