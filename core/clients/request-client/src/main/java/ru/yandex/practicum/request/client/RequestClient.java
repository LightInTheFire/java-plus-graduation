package ru.yandex.practicum.request.client;

import java.util.List;
import java.util.Map;

import ru.yandex.practicum.request.client.config.RequestFeignConfiguration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.resilience4j.retry.annotation.Retry;

@Retry(name = "request-client")
@FeignClient(name = "request-service", path = "/internal/requests", configuration = RequestFeignConfiguration.class)
public interface RequestClient {

    @GetMapping("/confirmed")
    Map<Long, Long> countConfirmed(@RequestParam List<Long> eventIds);
}
