package ru.yandex.practicum.analyzer.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.yandex.practicum.interaction.service.InteractionService;
import ru.yandex.practicum.similarity.model.Similarity;
import ru.yandex.practicum.similarity.service.SimilarityService;
import ru.yandex.practicum.stats.proto.RecommendedEventProto;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendationFacadeImpl implements RecommendationFacade {

    private final InteractionService interactionService;
    private final SimilarityService similarityService;

    public List<RecommendedEventProto> getForUser(long userId, int limit) {

        Set<Long> userEvents = interactionService.getUserEvents(userId);

        if (userEvents.isEmpty()) return List.of();

        Map<Long, Double> scores = new HashMap<>();

        for (Similarity sim : similarityService.getByEvents(userEvents)) {

            long other = userEvents.contains(sim.getEvent1()) ? sim.getEvent2() : sim.getEvent1();

            if (userEvents.contains(other)) continue;

            scores.merge(other, sim.getSimilarity(), Double::sum);
        }

        return toProto(scores, limit);
    }

    public List<RecommendedEventProto> getSimilar(long eventId, long userId, int limit) {

        Set<Long> userEvents = interactionService.getUserEvents(userId);

        return similarityService.getByEvent(eventId)
            .stream()
            .map(sim -> Map.entry(sim.getEvent1() == eventId ? sim.getEvent2() : sim.getEvent1(), sim.getSimilarity()))
            .filter(e -> !userEvents.contains(e.getKey()))
            .sorted(
                Map.Entry.<Long, Double>comparingByValue()
                    .reversed())
            .limit(limit)
            .map(this::toProto)
            .toList();
    }

    public List<RecommendedEventProto> getInteractions(List<Long> eventIds) {

        return interactionService.getInteractionsCount(eventIds)
            .stream()
            .map(
                e -> RecommendedEventProto.newBuilder()
                    .setEventId(e.eventId())
                    .setScore(e.score())
                    .build())
            .toList();
    }

    private List<RecommendedEventProto> toProto(Map<Long, Double> scores, int limit) {
        return scores.entrySet()
            .stream()
            .sorted(
                Map.Entry.<Long, Double>comparingByValue()
                    .reversed())
            .limit(limit)
            .map(this::toProto)
            .toList();
    }

    private RecommendedEventProto toProto(Map.Entry<Long, Double> e) {
        return RecommendedEventProto.newBuilder()
            .setEventId(e.getKey())
            .setScore(e.getValue())
            .build();
    }

}
