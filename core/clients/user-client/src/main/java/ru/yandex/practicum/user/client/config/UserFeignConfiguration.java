package ru.yandex.practicum.user.client.config;

import org.springframework.context.annotation.Bean;

import feign.codec.ErrorDecoder;

public class UserFeignConfiguration {

    @Bean
    public ErrorDecoder userErrorDecoder() {
        return new UserClientErrorDecoder();
    }
}
