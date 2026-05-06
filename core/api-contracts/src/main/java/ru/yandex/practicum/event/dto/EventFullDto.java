package ru.yandex.practicum.event.dto;

import java.time.LocalDateTime;

import ru.yandex.practicum.category.dto.CategoryDto;
import ru.yandex.practicum.user.dto.UserShortDto;

public record EventFullDto(String annotation, CategoryDto category, Long confirmedRequests, LocalDateTime createdOn,
    String description, LocalDateTime eventDate, Long id, UserShortDto initiator, LocationDto location, boolean paid,
    Integer participantLimit, LocalDateTime publishedOn, boolean requestModeration, EventState state, String title,
    Long views, Long commentaries) {}
