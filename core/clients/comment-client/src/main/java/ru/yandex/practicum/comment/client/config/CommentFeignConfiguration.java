package ru.yandex.practicum.comment.client.config;

import org.springframework.context.annotation.Bean;

import feign.codec.ErrorDecoder;

public class CommentFeignConfiguration {

    @Bean
    public ErrorDecoder eventErrorDecoder() {
        return new CommentClientErrorDecoder();
    }
}
