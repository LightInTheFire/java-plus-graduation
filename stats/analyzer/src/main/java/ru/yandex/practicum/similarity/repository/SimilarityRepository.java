package ru.yandex.practicum.similarity.repository;

import java.util.List;
import java.util.Set;

import ru.yandex.practicum.similarity.model.Similarity;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface SimilarityRepository extends CrudRepository<Similarity, Long> {

    @Query("""
            SELECT * FROM similarities
            WHERE event1 = :eventId OR event2 = :eventId
        """)
    List<Similarity> findByEvent(long eventId);

    @Query("""
            SELECT * FROM similarities
            WHERE event1 IN (:eventIds) OR event2 IN (:eventIds)
        """)
    List<Similarity> findByEventIds(Set<Long> eventIds);

}
