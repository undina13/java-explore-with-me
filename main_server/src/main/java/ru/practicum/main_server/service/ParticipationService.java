package ru.practicum.main_server.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_server.dto.ParticipationRequestDto;
import ru.practicum.main_server.repository.ParticipationRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ParticipationService {
    private final ParticipationRepository participationRepository;

    public ParticipationService(ParticipationRepository participationRepository) {
        this.participationRepository = participationRepository;
    }

    public List<ParticipationRequestDto> getParticipationRequestsByUser(Long userId) {
        return null;
    }

    public ParticipationRequestDto createParticipationRequest(Long userId, Long eventId) {
        return null;
    }

    public ParticipationRequestDto cancelRequestByUser(Long userId, Long requestId) {
        return null;
    }
}
