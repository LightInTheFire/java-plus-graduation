package ru.yandex.practicum.aggregator.service;

import ru.practicum.ewm.stats.avro.UserActionAvro;

public interface SimilarityService {

    void process(UserActionAvro userAction);
}
