package ru.yandex.practicum.aggregator.util;

import ru.practicum.ewm.stats.avro.ActionTypeAvro;

public enum ActionType {

    VIEW(0.4),
    REGISTER(0.8),
    LIKE(1.0);

    private final double weight;

    ActionType(double weight) {
        this.weight = weight;
    }

    public static double toWeight(ActionTypeAvro actionType) {
        return switch (actionType) {
            case VIEW -> VIEW.weight;
            case REGISTER -> REGISTER.weight;
            case LIKE -> LIKE.weight;
        };
    }
}
