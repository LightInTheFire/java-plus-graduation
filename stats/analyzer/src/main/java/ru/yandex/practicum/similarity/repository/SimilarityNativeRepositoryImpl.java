package ru.yandex.practicum.similarity.repository;

import java.sql.Timestamp;
import java.time.Instant;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SimilarityNativeRepositoryImpl implements SimilarityNativeRepository {

    private final NamedParameterJdbcOperations jdbcTemplate;

    @Override
    public void upsert(long event1, long event2, double similarity, Instant ts) {
        String sql = """
                INSERT INTO similarities (event1, event2, similarity, ts)
                VALUES (:event1, :event2, :similarity, :ts)
                ON CONFLICT (event1, event2)
                DO UPDATE SET
                    similarity = EXCLUDED.similarity,
                    ts = EXCLUDED.ts
            """;

        MapSqlParameterSource params = new MapSqlParameterSource().addValue("event1", event1)
            .addValue("event2", event2)
            .addValue("similarity", similarity)
            .addValue("ts", Timestamp.from(ts));

        jdbcTemplate.update(sql, params);
    }
}
