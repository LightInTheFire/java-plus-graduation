package ru.yandex.practicum.aggregator.consumer;

import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.yandex.practicum.aggregator.service.SimilarityService;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserActionConsumer {

    private final SimilarityService similarityService;

    @KafkaListener(topics = "${app.kafka.user-actions-topic}")
    public void listen(UserActionAvro userAction) {
        similarityService.process(userAction);
    }
}
