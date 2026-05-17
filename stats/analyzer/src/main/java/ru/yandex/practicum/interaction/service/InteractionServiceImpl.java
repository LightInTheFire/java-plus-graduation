package ru.yandex.practicum.interaction.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.yandex.practicum.analyzer.util.ActionType;
import ru.yandex.practicum.interaction.model.EventScore;
import ru.yandex.practicum.interaction.model.Interaction;
import ru.yandex.practicum.interaction.repository.InteractionRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class InteractionServiceImpl implements InteractionService {

    private final InteractionRepository interactionRepository;

    @Override
    public void process(UserActionAvro event) {

        long userId = event.getUserId();
        long eventId = event.getEventId();
        double rating = ActionType.toWeight(event.getActionType());

        interactionRepository.findByUserIdAndEventId(userId, eventId)
            .ifPresentOrElse(existing -> {

                if (rating > existing.getRating()) {
                    interactionRepository
                        .save(new Interaction(existing.getId(), userId, eventId, rating, event.getTimestamp()));
                }

            }, () -> interactionRepository.save(new Interaction(null, userId, eventId, rating, event.getTimestamp())));
    }

    @Override
    public Set<Long> getUserEvents(long userId) {
        return interactionRepository.findByUserIdOrderByTimestampDesc(userId)
            .stream()
            .map(Interaction::getEventId)
            .collect(Collectors.toSet());
    }

    @Override
    public List<EventScore> getInteractionsCount(List<Long> eventIds) {
        return interactionRepository.getInteractionsSum(eventIds);
    }

}
