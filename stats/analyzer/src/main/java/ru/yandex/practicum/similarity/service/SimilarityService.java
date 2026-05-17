package ru.yandex.practicum.similarity.service;

import java.util.List;
import java.util.Set;

import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.yandex.practicum.similarity.model.Similarity;

public interface SimilarityService {

    void process(EventSimilarityAvro event);

    List<Similarity> getByEvent(long eventId);

    List<Similarity> getByEvents(Set<Long> eventIds);

}
