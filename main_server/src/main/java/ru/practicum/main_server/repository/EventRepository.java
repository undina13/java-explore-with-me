package ru.practicum.main_server.repository;

import ru.practicum.main_server.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long>{

}
