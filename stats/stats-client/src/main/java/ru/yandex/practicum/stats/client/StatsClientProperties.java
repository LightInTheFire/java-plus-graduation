package ru.yandex.practicum.stats.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("stats-client")
public record StatsClientProperties(String statsServiceId, String appName, Long backOffPeriod, Integer retryAttempts,
    String dateTimeFormat) {

    public StatsClientProperties {
        if (statsServiceId == null) {
            statsServiceId = "stats-server";
        }
        if (appName == null) {
            appName = "ewm-main-service";
        }
        if (backOffPeriod == null) {
            backOffPeriod = 3000L;
        }
        if (retryAttempts == null) {
            retryAttempts = 3;
        }
        if (dateTimeFormat == null) {
            dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
        }
    }
}
