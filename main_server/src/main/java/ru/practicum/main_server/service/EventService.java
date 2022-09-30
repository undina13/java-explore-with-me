package ru.practicum.main_server.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_server.client.HitClient;
import ru.practicum.main_server.dto.EventFullDto;
import ru.practicum.main_server.dto.EventShortDto;
import ru.practicum.main_server.dto.NewEventDto;
import ru.practicum.main_server.dto.UpdateEventRequest;
import ru.practicum.main_server.exception.ObjectNotFoundException;
import ru.practicum.main_server.exception.WrongRequestException;
import ru.practicum.main_server.mapper.EventMapper;
import ru.practicum.main_server.model.Category;
import ru.practicum.main_server.model.Event;
import ru.practicum.main_server.model.State;
import ru.practicum.main_server.repository.CategoryRepository;
import ru.practicum.main_server.repository.EventRepository;
import ru.practicum.main_server.repository.ParticipationRepository;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class EventService {
    private final EventRepository eventRepository;
    private final ParticipationRepository participationRepository;
    private final UserService userService;
    private final HitClient hitClient;
    private final CategoryRepository categoryRepository;

    public EventService(EventRepository eventRepository, ParticipationRepository participationRepository, UserService userService, HitClient hitClient, CategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.participationRepository = participationRepository;
        this.userService = userService;
        this.hitClient = hitClient;
        this.categoryRepository = categoryRepository;
    }

    public List<EventShortDto> getEvents(String text, List<Integer> categories, Boolean paid, String rangeStart,
                                         String rangeEnd, Boolean onlyAvailable, String sort, int from, int size) {
        LocalDateTime start;
        if (rangeStart == null) {
            start = LocalDateTime.now();
        } else {
            start = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        LocalDateTime end;
        if (rangeEnd == null) {
            end = LocalDateTime.MAX;
        } else {
            end = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        List<Event> events = eventRepository.searchEvents(text, categories, paid, start, end,
                PageRequest.of(from / size, size))
                .stream()
                .collect(Collectors.toList());
        if (sort.equals("EVENT_DATE")) {
            events = events.stream()
                    .sorted(Comparator.comparing(Event::getEventDate))
                    .collect(Collectors.toList());
        }

        List<EventShortDto> eventShortDtos = events.stream()
                .filter(event -> event.getState().equals(State.PUBLISHED))
                .map(EventMapper::toEventShortDto)
                .map(this::setConfirmedRequestsAndViewsEventShortDto)
                .collect(Collectors.toList());
        if (sort.equals("VIEWS")) {
            eventShortDtos = eventShortDtos.stream()
                    .sorted(Comparator.comparing(EventShortDto::getViews))
                    .collect(Collectors.toList());
        }

        return eventShortDtos;
    }

    public EventFullDto getEventById(long id) {
        EventFullDto dto = EventMapper.toEventFullDto(checkAndGetEvent(id));
        if (!(dto.getState().equals(State.PUBLISHED.toString()))) {
            throw new WrongRequestException("Wrong state by request");
        }
        return setConfirmedRequestsAndViewsEventFullDto(dto);
    }

    public List<EventShortDto> getEventsCurrentUser(long userId, int from, int size) {
        return eventRepository.findAllByInitiatorId(userId, PageRequest.of(from / size, size))
                .stream()
                .map(EventMapper::toEventShortDto)
                .map(this::setConfirmedRequestsAndViewsEventShortDto)
                .collect(Collectors.toList());
    }

    public EventFullDto updateEvent(Long userId, UpdateEventRequest updateEventRequest) {

        Event event = checkAndGetEvent(updateEventRequest.getEventId());
        if (!event.getInitiator().getId().equals(userId)) {
            throw new WrongRequestException("only creator can update event");
        }
        if (event.getState().equals(State.PUBLISHED)) {
            throw new WrongRequestException("you can`t update published event");
        }
        if (updateEventRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventRequest.getCategory())
                    .orElseThrow(() -> new ObjectNotFoundException("Category not found"));
            event.setCategory(category);
        }
        if (updateEventRequest.getEventDate() != null) {
            LocalDateTime date = LocalDateTime.parse(updateEventRequest.getEventDate());
            if (date.isBefore(LocalDateTime.now().minusHours(2))) {
                throw new WrongRequestException("date event is too late");
            }
            event.setEventDate(date);
        }
        if (updateEventRequest.getDescription() != null) {
            event.setDescription(updateEventRequest.getDescription());
        }
        if (updateEventRequest.getPaid() != null) {
            event.setPaid(updateEventRequest.getPaid());
        }
        if (updateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }
        if (updateEventRequest.getTitle() != null) {
            event.setTitle(updateEventRequest.getTitle());
        }
        event = eventRepository.save(event);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        return setConfirmedRequestsAndViewsEventFullDto(eventFullDto);
    }

    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        Event event = EventMapper.toNewEvent(newEventDto);
        if (event.getEventDate().isBefore(LocalDateTime.now().minusHours(2))) {
            throw new WrongRequestException("date event is too late");
        }
        event.setInitiator(userService.checkAndGetUser(userId));
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new ObjectNotFoundException("Category not found"));
        event.setCategory(category);
        event = eventRepository.save(event);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        return setConfirmedRequestsAndViewsEventFullDto(eventFullDto);
    }

    public EventFullDto getEventCurrentUser(Long userId, Long eventId) {
        return null;
    }

    public EventFullDto cancelEvent(Long userId, Long eventId) {
        return null;
    }


    public List<EventFullDto> getAdminEvents(List<Long> users, List<State> states, List<Long> categories,
                                             String rangeStart, String rangeEnd, int from, int size) {
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

    public Event checkAndGetEvent(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new ObjectNotFoundException("event with id = " + eventId + " not found"));
    }

    public EventShortDto setConfirmedRequestsAndViewsEventShortDto(EventShortDto eventShortDto) {
        int confirmedRequests = participationRepository.countByEventIdAndStatus(eventShortDto.getId(), State.PUBLISHED);
        eventShortDto.setConfirmedRequests(confirmedRequests);
        eventShortDto.setViews(getViews(eventShortDto.getId()));
        return eventShortDto;
    }

    public EventFullDto setConfirmedRequestsAndViewsEventFullDto(EventFullDto eventFullDto) {
        int confirmedRequests = participationRepository.countByEventIdAndStatus(eventFullDto.getId(), State.PUBLISHED);
        eventFullDto.setConfirmedRequests(confirmedRequests);
        eventFullDto.setViews(getViews(eventFullDto.getId()));
        return eventFullDto;
    }

    public int getViews(long eventId) {
        ResponseEntity<Object> responseEntity = null;
        try {
            responseEntity = hitClient.getStat(
                    LocalDateTime.MIN,
                    LocalDateTime.now(),
                    List.of("/events/" + eventId),
                    false
            );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Integer hits = (Integer) ((LinkedHashMap) responseEntity.getBody()).get("hits");
        return hits;
    }
}
