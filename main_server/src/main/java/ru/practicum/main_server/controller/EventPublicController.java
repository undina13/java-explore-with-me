package ru.practicum.main_server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_server.dto.EventFullDto;
import ru.practicum.main_server.dto.EventShortDto;
import ru.practicum.main_server.service.EventService;

import java.util.List;

@RestController
@RequestMapping(path = "/events")
@Slf4j
public class EventPublicController {
    private final EventService eventService;

    public EventPublicController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping()
    List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                  @RequestParam List<Integer> categories,
                                  @RequestParam Boolean paid,
                                  @RequestParam String rangeStart,
                                  @RequestParam String rangeEnd,
                                  @RequestParam Boolean onlyAvailable,
                                  @RequestParam String sort,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        log.info("get events public");
        return eventService
                .getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{id}")
    EventFullDto getEventById(@PathVariable long id) {
        log.info("get event id={}", id);
        return eventService.getEventById(id);
    }
}
