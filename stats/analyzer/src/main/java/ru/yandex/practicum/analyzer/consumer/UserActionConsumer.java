package ru.yandex.practicum.analyzer.consumer;

import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.yandex.practicum.interaction.service.InteractionService;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserActionConsumer {

    private final InteractionService interactionService;

    @KafkaListener(topics = "${app.kafka.user-actions.topic}", containerFactory = "userActionKafkaListenerFactory")
    public void listen(UserActionAvro userAction) {
        interactionService.process(userAction);
    }
}
