package ru.yandex.practicum.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("stats-client")
public record StatsClientProperties(String serverUrl, String appName) {

    public StatsClientProperties {
        if (serverUrl == null) {
            serverUrl = "http://localhost:9090";
        }
        if (appName == null) {
            appName = "ewm-main-service";
        }
    }
}
