package ru.yandex.practicum.aggregator.producer;

import ru.practicum.ewm.stats.avro.EventSimilarityAvro;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventSimilarityProducerImpl implements EventSimilarityProducer {

    private final KafkaTemplate<String, EventSimilarityAvro> kafkaTemplate;

    @Override
    public void send(EventSimilarityAvro eventSimilarityAvro) {
        String key = "%d-%d".formatted(eventSimilarityAvro.getEventA(), eventSimilarityAvro.getEventB());
        kafkaTemplate.sendDefault(key, eventSimilarityAvro)
            .whenComplete((result, ex) -> {
                if (ex == null) {
                    RecordMetadata metadata = result.getRecordMetadata();
                    log.info(
                        "Event sent successfully. key={}, topic={}, partition={}, offset={}",
                        key,
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset());
                } else {
                    log.error("Failed to send event. key={}", key, ex);
                }
            });

    }
}
