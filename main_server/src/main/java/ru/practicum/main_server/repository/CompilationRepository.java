package ru.practicum.main_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_server.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
}
