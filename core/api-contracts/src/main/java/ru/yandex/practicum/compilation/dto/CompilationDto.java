package ru.yandex.practicum.compilation.dto;

import java.util.List;

import ru.yandex.practicum.event.dto.EventShortDto;

public record CompilationDto(List<EventShortDto> events, Long id, boolean pinned, String title) {}
