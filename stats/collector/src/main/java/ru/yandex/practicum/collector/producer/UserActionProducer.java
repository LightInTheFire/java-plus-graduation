package ru.yandex.practicum.collector.producer;

import ru.practicum.ewm.stats.avro.UserActionAvro;

public interface UserActionProducer {

    void sendEvent(UserActionAvro userActionProto);
}
