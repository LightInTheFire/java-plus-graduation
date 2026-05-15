package ru.yandex.practicum.aggregator.repository;

import java.util.stream.Collectors;
import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class InMemorySimilarityRepository implements SimilarityRepository {

    private final Map<Long, Map<Long, Double>> minWeightsSums = new HashMap<>();
    private final Map<Long, Map<Long, Double>> eventUserWeights = new HashMap<>();
    private final Map<Long, Double> eventTotalWeights = new HashMap<>();

    @Override
    public void save(long eventA, long eventB, double sum) {
        EventPair pair = normalize(eventA, eventB);

        minWeightsSums.computeIfAbsent(pair.first(), k -> new HashMap<>())
            .put(pair.second(), sum);
    }

    @Override
    public double find(long eventA, long eventB) {
        EventPair pair = normalize(eventA, eventB);

        return minWeightsSums.getOrDefault(pair.first(), Collections.emptyMap())
            .getOrDefault(pair.second(), 0.0);
    }

    @Override
    public double getUserWeight(long eventId, long userId) {
        return eventUserWeights.getOrDefault(eventId, Collections.emptyMap())
            .getOrDefault(userId, 0.0);
    }

    @Override
    public void updateUserWeight(long eventId, long userId, double weight) {
        eventUserWeights.computeIfAbsent(eventId, k -> new HashMap<>())
            .put(userId, weight);
    }

    @Override
    public double getTotalWeight(long eventId) {
        return eventTotalWeights.getOrDefault(eventId, 0.0);
    }

    @Override
    public void addToTotalWeight(long eventId, double delta) {
        eventTotalWeights.merge(eventId, delta, Double::sum);
    }

    @Override
    public Set<Long> getUserEvents(long userId) {
        return eventUserWeights.entrySet()
            .stream()
            .filter(
                entry -> entry.getValue()
                    .containsKey(userId))
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
    }

    private EventPair normalize(long eventA, long eventB) {
        return eventA < eventB ? new EventPair(eventA, eventB) : new EventPair(eventB, eventA);
    }

    private record EventPair(long first, long second) {}
}
