package ru.yandex.practicum.collector.producer;

import ru.practicum.ewm.stats.avro.UserActionAvro;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserActionProducerImpl implements UserActionProducer {

    private final KafkaTemplate<Long, UserActionAvro> userKafkaTemplate;

    @Override
    public void sendEvent(UserActionAvro userActionAvro) {
        Long key = userActionAvro.getUserId();
        userKafkaTemplate.sendDefault(key, userActionAvro)
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
