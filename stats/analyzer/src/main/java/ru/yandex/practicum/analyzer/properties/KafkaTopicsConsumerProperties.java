package ru.yandex.practicum.analyzer.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.kafka.listener.ContainerProperties;

@ConfigurationProperties(prefix = "app.kafka")
public record KafkaTopicsConsumerProperties(Listener listener, TopicConfig userActions, TopicConfig eventSimilarity) {

    public record Listener(ContainerProperties.AckMode ackMode) {}

    public record TopicConfig(String topic, Class<?> keyDeserializer, Class<?> valueDeserializer) {}
}
