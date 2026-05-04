package ru.yandex.practicum.event.controller;

import ru.yandex.practicum.event.dto.EventInfoDto;
import ru.yandex.practicum.event.service.EventService;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("/internal/events")
@RequiredArgsConstructor
public class EventInternalController {

    private final EventService eventService;

    @GetMapping("/exists")
    public boolean existsById(@RequestParam("id") Long id) {
        log.info("Internal event existsById {} requested", id);
        return eventService.existsById(id);
    }

    @GetMapping("/{eventId}")
    public EventInfoDto getInfoDto(@PathVariable Long eventId) {
        log.info("Internal event getInfoDto {} requested", eventId);
        return eventService.getEventById(eventId);
    }
}
