package ru.yandex.practicum.comment.dto;

import ru.yandex.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

public record CommentDto(Long id, String text, UserShortDto author, LocalDateTime created, boolean edited) {}
