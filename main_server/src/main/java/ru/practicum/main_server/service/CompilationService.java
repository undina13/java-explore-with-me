package ru.practicum.main_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_server.dto.CompilationDto;
import ru.practicum.main_server.dto.NewCompilationDto;
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

    @Autowired
    public CompilationService(CompilationRepository compilationRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
    //TODO  прилепить эвенты в список при отдаче
        return compilationRepository.findAllByPinned(pinned, PageRequest.of(from / size, size))
                .stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    public CompilationDto getCompilationById(long id) {
        //TODO  прилепить эвенты в список при отдаче

        return CompilationMapper.toCompilationDto(compilationRepository.findById(id).get());
    }

    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        List<Event> events = newCompilationDto.getEvents()
                .stream()
                .map(id -> eventRepository.findById(id).get())
                .collect(Collectors.toList());
        compilation.setEvents(events);
//TODO  прилепить эвенты в список при отдаче
        Compilation newCompilation = compilationRepository.save(compilation);
        return CompilationMapper.toCompilationDto(newCompilation);
    }

    @Transactional
    public void deleteCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).get();
        compilationRepository.delete(compilation);
    }

    public void deleteEventFromCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId).get();
        List<Event> events = compilation.getEvents();
        events.remove(eventRepository.findById(eventId).get());
        compilationRepository.save(compilation);
    }

    public void addEventToCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId).get();
        List<Event> events = compilation.getEvents();
        events.add(eventRepository.findById(eventId).get());
        compilationRepository.save(compilation);
    }

    public void deleteCompilationFromMainPage(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).get();
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    public void addCompilationToMainPage(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).get();
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }
}
