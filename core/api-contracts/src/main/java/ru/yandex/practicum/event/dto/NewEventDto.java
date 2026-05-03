package ru.yandex.practicum.event.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record NewEventDto(@NotBlank @Size(min = 20, max = 2000) String annotation, @NotNull Long category,
    @NotBlank @Size(min = 20, max = 7000) String description, @NotNull @Future LocalDateTime eventDate,
    @NotNull LocationDto location, Boolean paid, @PositiveOrZero Integer participantLimit, Boolean requestModeration,
    @NotBlank @Size(min = 3, max = 120) String title) {

    public NewEventDto {
        if (paid == null) paid = false;
        if (requestModeration == null) requestModeration = true;
        if (participantLimit == null) participantLimit = 0;
    }
}
