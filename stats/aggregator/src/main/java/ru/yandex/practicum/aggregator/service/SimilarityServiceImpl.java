package ru.yandex.practicum.aggregator.service;

import java.time.Instant;

import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.yandex.practicum.aggregator.producer.EventSimilarityProducer;
import ru.yandex.practicum.aggregator.repository.SimilarityRepository;
import ru.yandex.practicum.aggregator.util.ActionType;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SimilarityServiceImpl implements SimilarityService {

    private final EventSimilarityProducer eventSimilarityProducer;
    private final SimilarityRepository similarityRepository;

    @Override
    public void process(UserActionAvro userAction) {
        long userId = userAction.getUserId();
        long eventId = userAction.getEventId();
        double newWeight = ActionType.toWeight(userAction.getActionType());
        Instant timestamp = userAction.getTimestamp();

        double oldWeight = similarityRepository.getUserWeight(eventId, userId);

        if (newWeight <= oldWeight) {
            return;
        }

        similarityRepository.updateUserWeight(eventId, userId, newWeight);
        similarityRepository.addToTotalWeight(eventId, newWeight - oldWeight);

        for (Long otherEventId : similarityRepository.getUserEvents(userId)) {
            if (otherEventId.equals(eventId)) {
                continue;
            }

            double otherWeight = similarityRepository.getUserWeight(otherEventId, userId);

            if (otherWeight == 0.0) {
                continue;
            }

            double oldMin = Math.min(oldWeight, otherWeight);
            double newMin = Math.min(newWeight, otherWeight);
            double deltaMin = newMin - oldMin;

            double minSum = similarityRepository.find(eventId, otherEventId) + deltaMin;
            similarityRepository.save(eventId, otherEventId, minSum);

            double sumA = similarityRepository.getTotalWeight(eventId);
            double sumB = similarityRepository.getTotalWeight(otherEventId);

            if (sumA == 0.0 || sumB == 0.0) {
                continue;
            }

            double score = minSum / Math.sqrt(sumA * sumB);

            long first = Math.min(eventId, otherEventId);
            long second = Math.max(eventId, otherEventId);

            eventSimilarityProducer.send(
                EventSimilarityAvro.newBuilder()
                    .setEventA(first)
                    .setEventB(second)
                    .setScore(score)
                    .setTimestamp(timestamp)
                    .build());
        }
    }
}
