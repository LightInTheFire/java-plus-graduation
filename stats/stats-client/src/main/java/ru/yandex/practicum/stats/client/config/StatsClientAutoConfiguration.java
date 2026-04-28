package ru.yandex.practicum.stats.client.config;

import java.time.format.DateTimeFormatter;

import ru.yandex.practicum.stats.client.StatsClient;
import ru.yandex.practicum.stats.client.StatsClientImpl;
import ru.yandex.practicum.stats.client.StatsClientProperties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.MaxAttemptsRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestClient;

@AutoConfiguration
@EnableConfigurationProperties(StatsClientProperties.class)
public class StatsClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RestClient statsRestClient(RestClient.Builder builder) {
        return builder.build();
    }

    @Bean
    @ConditionalOnMissingBean
    public RetryTemplate statsRetryTemplate(StatsClientProperties statsClientProperties) {
        RetryTemplate retryTemplate = new RetryTemplate();

        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(statsClientProperties.backOffPeriod());
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

        MaxAttemptsRetryPolicy maxAttemptsRetryPolicy = new MaxAttemptsRetryPolicy();
        maxAttemptsRetryPolicy.setMaxAttempts(statsClientProperties.retryAttempts());
        retryTemplate.setRetryPolicy(maxAttemptsRetryPolicy);

        return retryTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    public StatsClient statsClient(@Qualifier("statsRestClient") RestClient statsRestClient,
        DiscoveryClient discoveryClient, @Qualifier("statsRetryTemplate") RetryTemplate retryTemplate,
        StatsClientProperties statsClientProperties) {
        return new StatsClientImpl(
            statsRestClient,
            statsClientProperties.statsServiceId(),
            statsClientProperties.appName(),
            retryTemplate,
            discoveryClient,
            DateTimeFormatter.ofPattern(statsClientProperties.dateTimeFormat()));
    }
}
