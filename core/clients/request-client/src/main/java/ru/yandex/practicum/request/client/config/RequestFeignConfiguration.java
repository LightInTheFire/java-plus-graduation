package ru.yandex.practicum.request.client.config;

import org.springframework.context.annotation.Bean;

import feign.codec.ErrorDecoder;

public class RequestFeignConfiguration {

    @Bean
    public ErrorDecoder userErrorDecoder() {
        return new RequestClientErrorDecoder();
    }
}
