package ru.yandex.practicum.event.client.config;

import org.springframework.context.annotation.Bean;

import feign.codec.ErrorDecoder;

public class EventFeignConfiguration {

    @Bean
    public ErrorDecoder eventErrorDecoder() {
        return new EventClientErrorDecoder();
    }
}
