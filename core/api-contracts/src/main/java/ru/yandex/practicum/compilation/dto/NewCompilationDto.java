package ru.yandex.practicum.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record NewCompilationDto(List<Long> events, Boolean pinned, @Size(min = 1, max = 50) @NotBlank String title) {

    public NewCompilationDto {
        if (pinned == null) pinned = false;
        if (events == null) events = List.of();
    }
}
