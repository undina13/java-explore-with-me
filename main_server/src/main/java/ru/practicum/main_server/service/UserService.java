package ru.practicum.main_server.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.main_server.dto.UserDto;
import ru.practicum.main_server.repository.UserRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        return null;
    }

    public UserDto saveUser(UserDto userDto) {
        return null;
    }

    public void deleteUser(Long userId) {
    }
}
