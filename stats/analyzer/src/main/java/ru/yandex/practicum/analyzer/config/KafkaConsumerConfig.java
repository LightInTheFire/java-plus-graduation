package ru.yandex.practicum.analyzer.config;

import java.util.HashMap;
import java.util.Map;

import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.yandex.practicum.analyzer.properties.KafkaTopicsConsumerProperties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(KafkaTopicsConsumerProperties.class)
public class KafkaConsumerConfig {

    private final KafkaProperties springKafka;
    private final KafkaTopicsConsumerProperties appKafka;

    @Bean
    public ConsumerFactory<String, UserActionAvro> userActionConsumerFactory() {

        Map<String, Object> props = new HashMap<>(springKafka.buildConsumerProperties(null));

        props.put(
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            appKafka.userActions()
                .keyDeserializer());

        props.put(
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            appKafka.userActions()
                .valueDeserializer());

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserActionAvro> userActionKafkaListenerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, UserActionAvro> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(userActionConsumerFactory());

        factory.getContainerProperties()
            .setAckMode(
                appKafka.listener()
                    .ackMode());

        return factory;
    }

    @Bean
    public ConsumerFactory<String, EventSimilarityAvro> similarityConsumerFactory() {

        Map<String, Object> props = new HashMap<>(springKafka.buildConsumerProperties(null));

        props.put(
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            appKafka.eventSimilarity()
                .keyDeserializer());

        props.put(
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            appKafka.eventSimilarity()
                .valueDeserializer());

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EventSimilarityAvro> similarityKafkaListenerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, EventSimilarityAvro> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(similarityConsumerFactory());

        factory.getContainerProperties()
            .setAckMode(
                appKafka.listener()
                    .ackMode());

        return factory;
    }

}
