package ru.yandex.practicum.client.config;

import ru.yandex.practicum.client.StatsClient;
import ru.yandex.practicum.client.StatsClientImpl;
import ru.yandex.practicum.client.StatsClientProperties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@AutoConfiguration
@EnableConfigurationProperties(StatsClientProperties.class)
public class StatsClientAutoConfiguration {

    @Bean
    public RestTemplate statsRestTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    @ConditionalOnMissingBean
    public StatsClient statsClient(@Qualifier("statsRestTemplate") RestTemplate statsRestTemplate,
        StatsClientProperties statsClientProperties) {
        return new StatsClientImpl(
            statsRestTemplate,
            statsClientProperties.serverUrl(),
            statsClientProperties.appName());
    }
}
