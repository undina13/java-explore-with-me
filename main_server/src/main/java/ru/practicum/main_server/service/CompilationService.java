package ru.practicum.main_server.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_server.dto.CompilationDto;
import ru.practicum.main_server.dto.EventFullDto;
import ru.practicum.main_server.dto.EventShortDto;
import ru.practicum.main_server.dto.NewCompilationDto;
import ru.practicum.main_server.repository.CompilationRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CompilationService {
    private final CompilationRepository compilationRepository;

    public CompilationService(CompilationRepository compilationRepository) {
        this.compilationRepository = compilationRepository;
    }

    public List<EventShortDto> getCompilations(Boolean pinned, int from, int size) {
        return null;
    }

    public EventFullDto getCompilationById(long id) {
        return null;
    }

    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        return null;
    }

    public void deleteCompilation(Long compId) {
    }

    public void deleteEventFromCompilation(Long compId, Long eventId) {
    }

    public void addEventToCompilation(Long compId, Long eventId) {
    }

    public void deleteCompilationFromMainPage(Long compId) {
    }

    public void addCompilationToMainPage(Long compId) {
    }
}
