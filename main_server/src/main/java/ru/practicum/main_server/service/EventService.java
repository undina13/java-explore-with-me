package ru.practicum.main_server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_server.client.HitClient;
import ru.practicum.main_server.dto.*;
import ru.practicum.main_server.exception.ObjectNotFoundException;
import ru.practicum.main_server.exception.WrongRequestException;
import ru.practicum.main_server.mapper.EventMapper;
import ru.practicum.main_server.model.*;
import ru.practicum.main_server.repository.CategoryRepository;
import ru.practicum.main_server.repository.EventRepository;
import ru.practicum.main_server.repository.ParticipationRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class EventService {
    private final EventRepository eventRepository;
    private final ParticipationRepository participationRepository;
    private final UserService userService;
    private final HitClient hitClient;
    private final CategoryRepository categoryRepository;
    private final LocationService locationService;

    public EventService(EventRepository eventRepository, ParticipationRepository participationRepository,
                        HitClient hitClient, UserService userService, CategoryRepository categoryRepository, LocationService locationService) {
        this.eventRepository = eventRepository;
        this.participationRepository = participationRepository;
        this.userService = userService;
        this.hitClient = hitClient;
        this.categoryRepository = categoryRepository;
        this.locationService = locationService;
    }

    public List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, String rangeStart,
                                         String rangeEnd, Boolean onlyAvailable, String sort, int from, int size) {
        LocalDateTime start = (rangeStart == null) ? LocalDateTime.now() :
                LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

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
        if (onlyAvailable) {
            eventShortDtos = eventShortDtos.stream()
                    .filter(eventShortDto -> eventShortDto.getConfirmedRequests()
                            <= checkAndGetEvent(eventShortDto.getId()).getParticipantLimit())
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

    @Transactional
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
            LocalDateTime date = LocalDateTime.parse(updateEventRequest.getEventDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
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

    @Transactional
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        Location location = newEventDto.getLocation();
        log.info("before location find");
        location = locationService.findByLatAndLon(location.getLat(), location.getLon())
                .orElseThrow(() -> new ObjectNotFoundException("Location not found"));
        log.info("location find");
        Event event = EventMapper.toNewEvent(newEventDto);
        log.info("event {}", event);
        if (event.getEventDate().isBefore(LocalDateTime.now().minusHours(2))) {
            throw new WrongRequestException("date event is too late");
        }
        event.setInitiator(userService.checkAndGetUser(userId));
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new ObjectNotFoundException("Category not found"));
        event.setCategory(category);
        event.setLocation(location);

        event = eventRepository.save(event);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        return setConfirmedRequestsAndViewsEventFullDto(eventFullDto);
    }

    public EventFullDto getEventCurrentUser(Long userId, Long eventId) {
        Event event = checkAndGetEvent(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new WrongRequestException("only initiator can get fullEventDto");
        }
        return setConfirmedRequestsAndViewsEventFullDto(EventMapper.toEventFullDto(event));
    }

    @Transactional
    public EventFullDto cancelEvent(Long userId, Long eventId) {
        Event event = checkAndGetEvent(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new WrongRequestException("only initiator can cancel event");
        }
        if (!event.getState().equals(State.PENDING)) {
            throw new WrongRequestException("you can cancel only pending event");
        }
        event.setState(State.CANCELED);
        event = eventRepository.save(event);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        return setConfirmedRequestsAndViewsEventFullDto(eventFullDto);
    }


    public List<EventFullDto> getAdminEvents(List<Long> users, List<State> states, List<Long> categories,
                                             String rangeStart, String rangeEnd, int from, int size) {
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

        return eventRepository.searchEventsByAdmin(users, states, categories, start, end,
                PageRequest.of(from / size, size))
                .stream()
                .map(EventMapper::toEventFullDto)
                .map(this::setConfirmedRequestsAndViewsEventFullDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventFullDto updateEventByAdmin(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = checkAndGetEvent(eventId);

        Optional.ofNullable(adminUpdateEventRequest.getAnnotation())
                .ifPresent(event::setAnnotation);

        if (adminUpdateEventRequest.getCategory() != null) {
            Category category = categoryRepository.findById(adminUpdateEventRequest.getCategory())
                    .orElseThrow(() -> new ObjectNotFoundException("Category not found"));
            event.setCategory(category);
        }

        Optional.ofNullable(adminUpdateEventRequest.getDescription())
                .ifPresent(event::setDescription);


        if (adminUpdateEventRequest.getEventDate() != null) {
            LocalDateTime date = LocalDateTime.parse(adminUpdateEventRequest.getEventDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if (date.isBefore(LocalDateTime.now().minusHours(2))) {
                throw new WrongRequestException("date event is too late");
            }
            event.setEventDate(date);
        }
        Optional.ofNullable(adminUpdateEventRequest.getLocation())
                .ifPresent(event::setLocation);

        Optional.ofNullable(adminUpdateEventRequest.getRequestModeration())
                .ifPresent(event::setRequestModeration);

        Optional.ofNullable(adminUpdateEventRequest.getPaid())
                .ifPresent(event::setPaid);

        Optional.ofNullable(adminUpdateEventRequest.getParticipantLimit())
                .ifPresent(event::setParticipantLimit);

        Optional.ofNullable(adminUpdateEventRequest.getTitle())
                .ifPresent(event::setTitle);

        event = eventRepository.save(event);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        return setConfirmedRequestsAndViewsEventFullDto(eventFullDto);
    }

    @Transactional
    public EventFullDto publishEventByAdmin(Long eventId) {
        Event event = checkAndGetEvent(eventId);
        if (event.getEventDate().isBefore(LocalDateTime.now().minusHours(2))) {
            throw new WrongRequestException("date event is too late");
        }
        if (!event.getState().equals(State.PENDING)) {
            throw new WrongRequestException("admin can publish only pending event");
        }
        event.setState(State.PUBLISHED);
        event = eventRepository.save(event);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        return setConfirmedRequestsAndViewsEventFullDto(eventFullDto);
    }

    @Transactional
    public EventFullDto rejectEventByAdmin(Long eventId) {
        Event event = checkAndGetEvent(eventId);

        event.setState(State.CANCELED);
        event = eventRepository.save(event);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        return setConfirmedRequestsAndViewsEventFullDto(eventFullDto);
    }

    public Event checkAndGetEvent(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new ObjectNotFoundException("event with id = " + eventId + " not found"));
    }

    public EventShortDto setConfirmedRequestsAndViewsEventShortDto(EventShortDto eventShortDto) {
        int confirmedRequests = participationRepository
                .countByEventIdAndStatus(eventShortDto.getId(), StatusRequest.CONFIRMED);
        eventShortDto.setConfirmedRequests(confirmedRequests);
        eventShortDto.setViews(getViews(eventShortDto.getId()));
        return eventShortDto;
    }

    public EventFullDto setConfirmedRequestsAndViewsEventFullDto(EventFullDto eventFullDto) {
        int confirmedRequests = participationRepository
                .countByEventIdAndStatus(eventFullDto.getId(), StatusRequest.CONFIRMED);
        eventFullDto.setConfirmedRequests(confirmedRequests);
        eventFullDto.setViews(getViews(eventFullDto.getId()));
        return eventFullDto;
    }

    public int getViews(long eventId) {
        ResponseEntity<Object> responseEntity = hitClient.getStat(
                LocalDateTime.of(2020, 9, 1, 0, 0),
                LocalDateTime.now(),
                List.of("/events/" + eventId),
                false);

        log.info("responseEntity {}", responseEntity.getBody());
        if (responseEntity.getBody().equals("")) {
            Integer hits = (Integer) ((LinkedHashMap) responseEntity.getBody()).get("hits");
            return hits;
        }

        return 0;
    }

    public void sentHitStat(HttpServletRequest request) {
        log.info("request URL {}", request.getRequestURI());
        EndpointHit endpointHit = EndpointHit.builder()
                .app("main_server")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
        hitClient.createHit(endpointHit);
    }
}
