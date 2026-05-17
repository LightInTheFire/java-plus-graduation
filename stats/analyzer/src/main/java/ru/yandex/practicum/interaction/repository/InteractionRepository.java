package ru.yandex.practicum.interaction.repository;

import java.util.List;
import java.util.Optional;

import ru.yandex.practicum.interaction.model.EventScore;
import ru.yandex.practicum.interaction.model.Interaction;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface InteractionRepository extends CrudRepository<Interaction, Long> {

    Optional<Interaction> findByUserIdAndEventId(Long userId, Long eventId);

    List<Interaction> findByUserIdOrderByTimestampDesc(Long userId);

    @Query("""
            SELECT event_id, SUM(rating) AS score
            FROM interactions
            WHERE event_id IN (:eventIds)
            GROUP BY event_id
        """)
    List<EventScore> getInteractionsSum(List<Long> eventIds);

}
