package ru.yandex.practicum.similarity.service;

import java.util.List;
import java.util.Set;

import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.yandex.practicum.similarity.model.Similarity;
import ru.yandex.practicum.similarity.repository.SimilarityNativeRepository;
import ru.yandex.practicum.similarity.repository.SimilarityRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SimilarityServiceImpl implements SimilarityService {

    private final SimilarityRepository similarityRepository;
    private final SimilarityNativeRepository similarityNativeRepository;

    @Override
    public void process(EventSimilarityAvro event) {
        long first = Math.min(event.getEventA(), event.getEventB());
        long second = Math.max(event.getEventA(), event.getEventB());

        similarityNativeRepository.upsert(first, second, event.getScore(), event.getTimestamp());

    }

    @Override
    public List<Similarity> getByEvent(long eventId) {
        return similarityRepository.findByEvent(eventId);
    }

    @Override
    public List<Similarity> getByEvents(Set<Long> eventIds) {
        return similarityRepository.findByEventIds(eventIds);
    }
}
