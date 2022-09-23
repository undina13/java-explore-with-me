package ru.practicum.main_server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_server.dto.EventFullDto;
import ru.practicum.main_server.dto.EventShortDto;
import ru.practicum.main_server.service.CompilationService;

import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@Slf4j
public class CompilationPublicController {
    private final CompilationService compilationService;

    public CompilationPublicController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping()
    List<EventShortDto> getCompilations(@RequestParam Boolean pinned,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        log.info("get compilations pinned {}", pinned);
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{id}")
    EventFullDto getCompilationById(@PathVariable long id){
        log.info("get compilation id={}", id);
        return compilationService.getCompilationById(id);
    }
}
