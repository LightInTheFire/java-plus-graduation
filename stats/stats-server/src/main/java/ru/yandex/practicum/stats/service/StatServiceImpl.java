package ru.yandex.practicum.stats.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import ru.yandex.practicum.dto.EndpointHitDto;
import ru.yandex.practicum.dto.ViewStatsDto;
import ru.yandex.practicum.stats.exception.IllegalArgumentException;
import ru.yandex.practicum.stats.mapper.StatsMapper;
import ru.yandex.practicum.stats.model.StatsInfo;
import ru.yandex.practicum.stats.repository.StatRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    private final StatRepository statRepository;
    private final StatsMapper statsMapper;

    @Override
    @Transactional
    public void createEndpointHit(EndpointHitDto endpointHitDto) {
        statRepository.save(statsMapper.mapToEntity(endpointHitDto));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ViewStatsDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("The end date must be before start date.");
        }

        List<StatsInfo> statsInfoList;
        if (uris != null) {
            if (unique) {
                statsInfoList = statRepository.getUniqueStatsWithUris(start, end, uris);
            } else {
                statsInfoList = statRepository.getNotUniqueStatsWithUris(start, end, uris);
            }
        } else {
            if (unique) {
                statsInfoList = statRepository.getUniqueStatsWithoutUris(start, end);
            } else {
                statsInfoList = statRepository.getNotUniqueStatsWithoutUris(start, end);
            }
        }

        return statsInfoList.stream()
            .map(statsMapper::mapToDto)
            .toList();
    }
}
