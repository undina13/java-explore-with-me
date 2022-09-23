package ru.practicum.main_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_server.model.Participation;
import ru.practicum.main_server.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
