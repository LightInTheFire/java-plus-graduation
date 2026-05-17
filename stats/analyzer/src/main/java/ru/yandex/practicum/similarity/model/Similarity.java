package ru.yandex.practicum.similarity.model;

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
@Table("similarities")
public class Similarity {

    @Id
    @Column("id")
    private Long id;

    @Column("event1")
    private Long event1;

    @Column("event2")
    private Long event2;

    @Column("similarity")
    private Double similarity;

    @Column("ts")
    private Instant timestamp;

}
