package ru.yandex.practicum.analyzer.service;

import java.util.List;

import ru.yandex.practicum.stats.proto.RecommendedEventProto;

public interface RecommendationFacade {

    List<RecommendedEventProto> getForUser(long userId, int limit);

    List<RecommendedEventProto> getSimilar(long eventId, long userId, int limit);

    List<RecommendedEventProto> getInteractions(List<Long> eventIds);
}
