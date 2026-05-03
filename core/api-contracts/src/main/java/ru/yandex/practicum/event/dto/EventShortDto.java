package ru.yandex.practicum.event.dto;

import ru.yandex.practicum.category.dto.CategoryDto;
import ru.yandex.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

public record EventShortDto(String annotation, CategoryDto category, Long confirmedRequests, LocalDateTime eventDate,
    Long id, UserShortDto initiator, boolean paid, String title, Long views, Long commentaries) {}
