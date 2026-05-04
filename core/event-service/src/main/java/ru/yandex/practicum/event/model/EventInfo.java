package ru.yandex.practicum.event.model;

import ru.yandex.practicum.event.dto.EventState;

public interface EventInfo {

    Long getId();

    Long getInitiatorId();

    Integer getParticipantLimit();

    Boolean isRequestModeration();

    EventState getState();
}
