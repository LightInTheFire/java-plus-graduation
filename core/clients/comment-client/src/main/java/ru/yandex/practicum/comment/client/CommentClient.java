package ru.yandex.practicum.comment.client;

import java.util.List;
import java.util.Map;

import ru.yandex.practicum.comment.client.config.CommentFeignConfiguration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.resilience4j.retry.annotation.Retry;

@Retry(name = "comment-client")
@FeignClient(name = "comment-service", path = "/internal/comments", configuration = CommentFeignConfiguration.class)
public interface CommentClient {

    @GetMapping("/count")
    Map<Long, Long> countComments(@RequestParam List<Long> eventIds);
}
