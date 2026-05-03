package ru.yandex.practicum.compilation.dto;

import ru.yandex.practicum.event.dto.EventShortDto;

import java.util.List;

public record CompilationDto(List<EventShortDto> events, Long id, boolean pinned, String title) {}
