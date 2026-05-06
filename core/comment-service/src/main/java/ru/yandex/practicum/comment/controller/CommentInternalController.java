package ru.yandex.practicum.comment.controller;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotEmpty;

import ru.yandex.practicum.comment.service.CommentService;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("internal/comments")
public class CommentInternalController {

    private final CommentService commentService;

    @GetMapping("/count")
    public Map<Long, Long> countCommentsForEventIds(@RequestParam @NotEmpty List<Long> eventIds) {
        log.info("Count comments for events with ids {}", eventIds);
        return commentService.countCommentsForEventIds(eventIds);
    }
}
