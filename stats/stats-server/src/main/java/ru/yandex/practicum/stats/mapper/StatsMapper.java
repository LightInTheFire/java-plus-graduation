package ru.yandex.practicum.stats.mapper;

import ru.yandex.practicum.dto.EndpointHitDto;
import ru.yandex.practicum.dto.ViewStatsDto;
import ru.yandex.practicum.stats.model.Stats;
import ru.yandex.practicum.stats.model.StatsInfo;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface StatsMapper {

    Stats mapToEntity(EndpointHitDto endpointHitDto);

    ViewStatsDto mapToDto(StatsInfo statsInfo);
}
