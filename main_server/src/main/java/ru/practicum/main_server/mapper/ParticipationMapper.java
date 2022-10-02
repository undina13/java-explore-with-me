package ru.practicum.main_server.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main_server.dto.ParticipationRequestDto;
import ru.practicum.main_server.model.Participation;

import java.time.format.DateTimeFormatter;

@UtilityClass
public class ParticipationMapper {
    public static ParticipationRequestDto toParticipationRequestDto(Participation participation) {
        return ParticipationRequestDto.builder()
                .id(participation.getId())
                .created(participation.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .event(participation.getEvent().getId())
                .requester(participation.getRequester().getId())
                .status(participation.getStatus().toString())
                .build();
    }
}
