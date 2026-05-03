package ru.yandex.practicum.compilation.mapper;

import java.util.List;
import java.util.Set;

import ru.yandex.practicum.compilation.dto.CompilationDto;
import ru.yandex.practicum.compilation.dto.NewCompilationDto;
import ru.yandex.practicum.compilation.dto.UpdateCompilationRequest;
import ru.yandex.practicum.compilation.model.Compilation;
import ru.yandex.practicum.event.dto.EventShortDto;
import ru.yandex.practicum.event.model.Event;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CompilationsMapper {

    public Compilation mapToEntity(NewCompilationDto newCompilationDto, Set<Event> events) {
        return new Compilation(null, newCompilationDto.title(), newCompilationDto.pinned(), events);
    }

    public CompilationDto mapToDto(Compilation compilation, List<EventShortDto> events) {
        return new CompilationDto(
            events,
            compilation.getId(),
            Boolean.TRUE.equals(compilation.getPinned()),
            compilation.getTitle());
    }

    public void updateEntity(Compilation compilation, UpdateCompilationRequest updateRequest, Set<Event> events) {

        if (updateRequest.hasTitle()) {
            compilation.setTitle(updateRequest.title());
        }

        if (updateRequest.hasPinned()) {
            compilation.setPinned(updateRequest.pinned());
        }

        if (updateRequest.hasEvents()) {
            compilation.setEvents(events);
        }
    }
}
