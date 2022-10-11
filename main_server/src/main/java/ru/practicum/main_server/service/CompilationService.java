package ru.practicum.main_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_server.dto.CompilationDto;
import ru.practicum.main_server.dto.EventShortDto;
import ru.practicum.main_server.dto.NewCompilationDto;
import ru.practicum.main_server.exception.ObjectNotFoundException;
import ru.practicum.main_server.mapper.CompilationMapper;
import ru.practicum.main_server.model.Compilation;
import ru.practicum.main_server.model.Event;
import ru.practicum.main_server.repository.CompilationRepository;
import ru.practicum.main_server.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final EventService eventService;

    @Autowired
    public CompilationService(CompilationRepository compilationRepository, EventRepository eventRepository, EventService eventService) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
        this.eventService = eventService;
    }

    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        return compilationRepository.findAllByPinned(pinned, PageRequest.of(from / size, size))
                .stream()
                .map(CompilationMapper::toCompilationDto)
                .map(this::setViewsAndConfirmedRequestsInDto)
                .collect(Collectors.toList());
    }

    public CompilationDto getCompilationById(long id) {
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilationRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Compilation not found")));
        return setViewsAndConfirmedRequestsInDto(compilationDto);
    }

    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        List<Event> events = newCompilationDto.getEvents()
                .stream()
                .map(id -> eventRepository.findById(id)
                        .orElseThrow(() -> new ObjectNotFoundException("Compilation not found")))
                .collect(Collectors.toList());
        compilation.setEvents(events);
        Compilation newCompilation = compilationRepository.save(compilation);
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(newCompilation);
        return setViewsAndConfirmedRequestsInDto(compilationDto);
    }

    @Transactional
    public void deleteCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException("Compilation not found"));
        compilationRepository.delete(compilation);
    }

    public void deleteEventFromCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException("Compilation not found"));
        List<Event> events = compilation.getEvents();
        events.remove(eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event not found")));
        compilationRepository.save(compilation);
    }

    public void addEventToCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException("Compilation not found"));
        List<Event> events = compilation.getEvents();
        events.add(eventRepository.findById(eventId).orElseThrow(() -> new ObjectNotFoundException("Event not found")));
        compilationRepository.save(compilation);
    }

    public void deleteCompilationFromMainPage(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException("Compilation not found"));
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    public void addCompilationToMainPage(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException("Compilation not found"));
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

    private CompilationDto setViewsAndConfirmedRequestsInDto(CompilationDto compilationDto) {
        List<EventShortDto> eventShortDtos = compilationDto.getEvents()
                .stream()
                .map(eventService::setConfirmedRequestsAndViewsEventShortDto)
                .collect(Collectors.toList());
        compilationDto.setEvents(eventShortDtos);
        return compilationDto;
    }
}
