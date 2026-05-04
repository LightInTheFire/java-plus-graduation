package ru.yandex.practicum.user.client;

import java.util.List;

import ru.yandex.practicum.user.client.config.UserFeignConfiguration;
import ru.yandex.practicum.user.dto.UserShortDto;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.resilience4j.retry.annotation.Retry;

@Retry(name = "user-client")
@FeignClient(name = "user-service", path = "/internal/users", configuration = UserFeignConfiguration.class)
public interface UserClient {

    @GetMapping("/{userId}")
    UserShortDto getUser(@PathVariable Long userId);

    @GetMapping
    List<UserShortDto> getUsersByIds(@RequestParam List<Long> userIds);
}
