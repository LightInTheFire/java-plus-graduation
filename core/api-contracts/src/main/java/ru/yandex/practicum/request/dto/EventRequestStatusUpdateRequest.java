package ru.yandex.practicum.request.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import ru.yandex.practicum.request.model.EventRequestStatus;

import java.util.List;

public record EventRequestStatusUpdateRequest(@NotEmpty List<Long> requestIds, @NotNull EventRequestStatus status) {}
