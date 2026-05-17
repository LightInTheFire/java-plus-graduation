package ru.yandex.practicum.aggregator.repository;

import java.util.Set;

public interface SimilarityRepository {

    void save(long eventA, long eventB, double sum);

    double find(long eventA, long eventB);

    double getUserWeight(long eventId, long userId);

    void updateUserWeight(long eventId, long userId, double weight);

    double getTotalWeight(long eventId);

    void addToTotalWeight(long eventId, double delta);

    Set<Long> getUserEvents(long userId);

}
