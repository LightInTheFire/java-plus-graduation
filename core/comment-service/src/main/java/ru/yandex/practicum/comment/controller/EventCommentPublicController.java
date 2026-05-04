package ru.yandex.practicum.comment.controller;

import java.util.Collection;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import ru.yandex.practicum.comment.dto.CommentDto;
import ru.yandex.practicum.comment.service.CommentService;
import ru.yandex.practicum.comment.service.CommentsPublicGetRequest;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventCommentPublicController {

    private final CommentService commentService;

    @GetMapping("/{eventId}/comments")
    public Collection<CommentDto> getComments(@PathVariable Long eventId,
        @RequestParam(defaultValue = "0") @PositiveOrZero int from,
        @RequestParam(defaultValue = "10") @Positive int size) {

        CommentsPublicGetRequest request = new CommentsPublicGetRequest(eventId, from, size);
        log.info("All comments for event {} requested with params {}", eventId, request);
        return commentService.getAllCommentsPaged(request);
    }
}
