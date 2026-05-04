package ru.yandex.practicum.request.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import ru.yandex.practicum.request.dto.EventRequestStatus;

import lombok.experimental.FieldDefaults;
import lombok.*;

@Entity
@Table(name = "participation_request")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JoinColumn(name = "event_id", nullable = false)
    Long eventId;

    @JoinColumn(name = "requester_id", nullable = false)
    Long requesterId;

    @Column(nullable = false)
    LocalDateTime created;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    EventRequestStatus status;
}
