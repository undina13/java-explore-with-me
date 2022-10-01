package ru.practicum.main_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_server.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
