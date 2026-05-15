package ru.yandex.practicum.aggregator.producer;

import ru.practicum.ewm.stats.avro.EventSimilarityAvro;

public interface EventSimilarityProducer {

    void send(EventSimilarityAvro eventSimilarityAvro);
}
