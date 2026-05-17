package ru.yandex.practicum.interaction.service;

import java.util.List;
import java.util.Set;

import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.yandex.practicum.interaction.model.EventScore;

public interface InteractionService {

    void process(UserActionAvro event);

    Set<Long> getUserEvents(long userId);

    List<EventScore> getInteractionsCount(List<Long> eventIds);

}
