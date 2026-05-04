package ru.yandex.practicum.event.dto;


public record EventInfoDto(Long id, Long initiatorId, Integer participantLimit,
                           Boolean requestModeration, EventState state) {
}
