package ru.yandex.practicum.comment.client.config;

import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.exception.ServiceUnavailableException;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommentClientErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        int statusCode = response.status();

        if (statusCode == 404) {
            log.warn("Comment not found. methodKey={}, status={}", methodKey, statusCode);
            return new NotFoundException("User not found");
        }

        if (statusCode >= 500) {
            log.error("Server error. methodKey={}, status={}", methodKey, statusCode);
            return new ServiceUnavailableException("Comment server unavailable");
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
