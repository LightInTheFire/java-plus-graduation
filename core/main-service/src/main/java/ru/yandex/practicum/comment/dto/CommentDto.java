package ru.yandex.practicum.comment.dto;

import java.time.LocalDateTime;

import ru.yandex.practicum.user.dto.UserShortDto;

public record CommentDto(Long id, String text, UserShortDto author, LocalDateTime created, boolean edited) {}
