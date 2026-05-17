package ru.yandex.practicum.similarity.repository;

import java.time.Instant;

public interface SimilarityNativeRepository {

    void upsert(long event1, long event2, double similarity, Instant ts);
}
