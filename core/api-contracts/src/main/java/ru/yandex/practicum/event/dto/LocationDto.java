package ru.yandex.practicum.event.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record LocationDto(@NotNull @DecimalMin("-90.0") @DecimalMax("90.0") BigDecimal lat,
    @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") BigDecimal lon) {}
