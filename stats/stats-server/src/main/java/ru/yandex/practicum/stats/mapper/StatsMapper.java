package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.yandex.practicum.dto.EndpointHitDto;
import ru.yandex.practicum.dto.ViewStatsDto;
import ru.yandex.practicum.model.Stats;
import ru.yandex.practicum.model.StatsInfo;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface StatsMapper {

     Stats mapToEntity(EndpointHitDto endpointHitDto);

     ViewStatsDto mapToDto(StatsInfo statsInfo);
}
