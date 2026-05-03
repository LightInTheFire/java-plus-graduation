package ru.yandex.practicum.request.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record EventRequestStatusUpdateRequest(@NotEmpty List<Long> requestIds, @NotNull EventRequestStatus status) {}
