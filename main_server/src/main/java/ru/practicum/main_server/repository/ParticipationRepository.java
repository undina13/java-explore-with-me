package ru.practicum.main_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_server.model.Event;
import ru.practicum.main_server.model.Participation;
import ru.practicum.main_server.model.StatusRequest;
import ru.practicum.main_server.model.User;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    List<Participation> findAllByRequester(User requester);

    Participation findByEventAndRequester(Event event, User requester);

    Integer countDistinctByEventAndStatus(Event event, StatusRequest status);

    Integer countByEventIdAndStatus(Long eventId, StatusRequest status);

    List<Participation> findAllByEventId(long eventId);
}
