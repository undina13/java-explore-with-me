package ru.practicum.main_server.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main_server.dto.CompilationDto;
import ru.practicum.main_server.dto.EventShortDto;
import ru.practicum.main_server.dto.NewCompilationDto;
import ru.practicum.main_server.model.Compilation;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {
    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.isPinned())
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        List<EventShortDto> eventShortDtos = compilation.getEvents()
                .stream()
                .map(event -> EventMapper.toEventShortDto(event))
                .collect(Collectors.toList());
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(eventShortDtos)
                .title(compilation.getTitle())
                .pinned(compilation.isPinned())
                .build();
    }
}
