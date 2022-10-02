package ru.practicum.main_server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main_server.model.Location;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByLatAndLon(float lat, float lon);

    Page<Location> findAll(Pageable pageable);

    @Query("SELECT l FROM Location AS l " +
            "WHERE function('distance', l.lat, l.lon, :lat, :lon) <= :radius")
    List<Location> findLocationsInRadius(float lat, float lon, float radius);
}
