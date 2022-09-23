package ru.practicum.main_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_server.model.Participation;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
}
