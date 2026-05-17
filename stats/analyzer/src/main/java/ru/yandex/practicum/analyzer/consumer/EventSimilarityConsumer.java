package ru.yandex.practicum.analyzer.consumer;

import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.yandex.practicum.similarity.service.SimilarityService;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventSimilarityConsumer {

    private final SimilarityService similarityService;

    @KafkaListener(topics = "${app.kafka.event-similarity.topic}", containerFactory = "similarityKafkaListenerFactory")
    public void listen(EventSimilarityAvro eventSimilarityAvro) {
        similarityService.process(eventSimilarityAvro);
    }
}
