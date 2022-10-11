package ru.practicum.stats_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.stats_server.model.HitModel;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<HitModel, Long> {

    List<HitModel> findAllByUriAndTimestampBetween(String uri, LocalDateTime start, LocalDateTime end);

    List<HitModel> findAllByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
