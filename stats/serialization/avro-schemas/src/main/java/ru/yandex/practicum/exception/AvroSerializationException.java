package ru.yandex.practicum.exception;

public class AvroSerializationException extends RuntimeException {

    public AvroSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
