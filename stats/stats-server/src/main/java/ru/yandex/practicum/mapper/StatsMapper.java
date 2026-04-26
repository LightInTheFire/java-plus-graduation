package ru.yandex.practicum.mapper;

import ru.yandex.practicum.dto.EndpointHitDto;
import ru.yandex.practicum.model.Stats;

public class StatsMapper {

    public static Stats mapToEntity(EndpointHitDto endpointHitDto) {
        return new Stats(
            null,
            endpointHitDto.app(),
            endpointHitDto.uri(),
            endpointHitDto.ip(),
            endpointHitDto.timestamp());
    }
}
