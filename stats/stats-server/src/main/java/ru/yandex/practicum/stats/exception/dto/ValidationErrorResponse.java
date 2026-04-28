package ru.yandex.practicum.stats.exception.dto;

import java.util.List;

public record ValidationErrorResponse(List<Violation> violations) {}
