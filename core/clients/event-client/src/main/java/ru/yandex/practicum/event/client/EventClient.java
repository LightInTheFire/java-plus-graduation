package ru.yandex.practicum.event.client;

import ru.yandex.practicum.event.client.config.EventFeignConfiguration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.resilience4j.retry.annotation.Retry;

@Retry(name = "event-client")
@FeignClient(name = "event-service", path = "/internal/events", configuration = EventFeignConfiguration.class)
public interface EventClient {

    @GetMapping
    boolean existsById(@RequestParam("id") Long id);

}
