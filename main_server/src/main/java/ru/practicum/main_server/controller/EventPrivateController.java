package ru.practicum.main_server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_server.dto.*;
import ru.practicum.main_server.service.EventService;
import ru.practicum.main_server.service.ParticipationService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@Slf4j
public class EventPrivateController {
    private final EventService eventService;
    private final ParticipationService participationService;

    public EventPrivateController(EventService eventService, ParticipationService participationService) {
        this.eventService = eventService;
        this.participationService = participationService;
    }

    @GetMapping()
    List<EventShortDto> getEventsCurrentUser(@PathVariable long userId,
                                             @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        log.info("get events current userId{}", userId);
        return eventService
                .getEventsCurrentUser(userId, from, size);
    }

    @PatchMapping
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @RequestBody UpdateEventRequest updateEventRequest) {
        log.info("update event  userId{}", userId);
        return eventService.updateEvent(userId, updateEventRequest);
    }

    @PostMapping
    public EventFullDto createEvent(@PathVariable Long userId,
                                    @RequestBody @Valid NewEventDto newEventDto) {
        log.info("create event  userId{} {}", userId, newEventDto);
        return eventService.createEvent(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventCurrentUser(@PathVariable Long userId,
                                            @PathVariable Long eventId) {
        log.info("get event {} userId{}", eventId, userId);
        return eventService.getEventCurrentUser(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId) {
        log.info("cancel event {} userId{}", eventId, userId);
        return eventService.cancelEvent(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getEventParticipationByOwner(@PathVariable Long userId,
                                                                      @PathVariable Long eventId) {
        log.info("get event {} participations  userId{}", eventId, userId);
        return participationService.getEventParticipationByOwner(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto approvalParticipationEventRequest(@PathVariable Long userId,
                                                                     @PathVariable Long eventId,
                                                                     @PathVariable Long reqId) {
        log.info("approval  participations {} userId{}", reqId, userId);
        return participationService.approvalParticipationEventRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectParticipationEventRequest(@PathVariable Long userId,
                                                                   @PathVariable Long eventId,
                                                                   @PathVariable Long reqId) {
        log.info("approval  participations {} userId{}", reqId, userId);
        return participationService.rejectParticipationEventRequest(userId, eventId, reqId);
    }
}
