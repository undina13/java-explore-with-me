package ru.practicum.main_server.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_server.dto.*;
import ru.practicum.main_server.model.State;
import ru.practicum.main_server.repository.EventRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<EventShortDto> getEvents(String text, List<Integer> categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, int from, int size) {
        return null;
    }

    public EventFullDto getEventById(long id) {
        return null;
    }

    public List<EventShortDto> getEventsCurrentUser(long userId, int from, int size) {
        return null;
    }

    public EventFullDto updateEvent(Long userId, UpdateEventRequest updateEventRequest) {
        return null;
    }

    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        return null;
    }

    public EventFullDto getEventCurrentUser(Long userId, Long eventId) {
        return null;
    }

    public EventFullDto cancelEvent(Long userId, Long eventId) {
        return null;
    }

    public List<ParticipationRequestDto> getEventParticipationByOwner(Long userId, Long eventId) {
        return null;
    }

    public ParticipationRequestDto approvalParticipationEventRequest(Long userId, Long eventId, Long participationId) {
        return null;
    }

    public ParticipationRequestDto rejectParticipationEventRequest(Long userId, Long eventId, Long participationId) {
        return null;
    }

    public List<EventFullDto> getAdminEvents(List<Long> users, List<State> states, List<Long> categories, String rangeStart, String rangeEnd, int from, int size) {
        return null;
    }

    public EventFullDto updateEventByAdmin(Long eventId, NewEventDto newEventDto) {
        return null;
    }

    public EventFullDto publishEventByAdmin(Long eventId) {
        return null;
    }

    public EventFullDto rejectEventByAdmin(Long eventId) {
        return null;
    }
}
