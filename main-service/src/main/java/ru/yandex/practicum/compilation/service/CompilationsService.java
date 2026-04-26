package ru.yandex.practicum.compilation.service;

import java.util.Collection;

import jakarta.validation.Valid;

import ru.yandex.practicum.compilation.dto.CompilationDto;
import ru.yandex.practicum.compilation.dto.NewCompilationDto;
import ru.yandex.practicum.compilation.dto.UpdateCompilationRequest;

public interface CompilationsService {

    Collection<CompilationDto> findAll(CompilationsPublicGetRequest getRequest);

    CompilationDto findById(long compId);

    CompilationDto save(@Valid NewCompilationDto newCompilationDto);

    void deleteById(long compId);

    CompilationDto update(long compId, UpdateCompilationRequest updateRequest);
}
