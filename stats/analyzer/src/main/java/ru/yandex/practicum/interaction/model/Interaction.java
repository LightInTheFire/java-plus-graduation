package ru.yandex.practicum.interaction.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("interactions")
public class Interaction {

    @Id
    @Column("id")
    private Long id;

    @Column("user_id")
    private Long userId;

    @Column("event_id")
    private Long eventId;

    @Column("rating")
    private Double rating;

    @Column("ts")
    private Instant timestamp;

}
