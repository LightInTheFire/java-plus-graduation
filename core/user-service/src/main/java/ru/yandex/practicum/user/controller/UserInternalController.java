package ru.yandex.practicum.user.controller;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import ru.yandex.practicum.user.dto.UserShortDto;
import ru.yandex.practicum.user.service.UserService;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class UserInternalController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public UserShortDto getUser(@PathVariable @NotNull Long userId) {
        log.info("Internal get user requested with id: {}", userId);
        return userService.getUserById(userId);
    }

    @GetMapping
    public List<UserShortDto> getUsersByIds(@RequestParam @NotEmpty List<Long> userIds) {
        log.info("Internal get users requested with ids: {}", userIds);
        return userService.getUsersByIds(userIds);
    }
}
