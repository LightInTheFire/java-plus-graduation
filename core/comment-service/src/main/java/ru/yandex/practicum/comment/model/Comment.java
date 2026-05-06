package ru.yandex.practicum.comment.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.experimental.FieldDefaults;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String text;

    @JoinColumn(name = "author_id", nullable = false)
    Long authorId;

    @JoinColumn(name = "event_id", nullable = false)
    Long eventId;

    @Column(nullable = false)
    LocalDateTime created;

    @Column(nullable = false)
    boolean edited;
}
