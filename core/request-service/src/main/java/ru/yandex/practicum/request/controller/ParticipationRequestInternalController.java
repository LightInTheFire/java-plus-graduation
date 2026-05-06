package ru.yandex.practicum.request.controller;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotEmpty;

import ru.yandex.practicum.request.service.ParticipationRequestService;

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
@RequestMapping("/internal/requests")
@RequiredArgsConstructor
public class ParticipationRequestInternalController {

    private final ParticipationRequestService requestService;

    @GetMapping("/confirmed")
    public Map<Long, Long> countConfirmedRequestsByEventIds(@RequestParam @NotEmpty List<Long> eventIds) {
        log.info("Received count confirmed requests for eventIds {}", eventIds);
        return requestService.countConfirmedByEventIds(eventIds);
    }
}
