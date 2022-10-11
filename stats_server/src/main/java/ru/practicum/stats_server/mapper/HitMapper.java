package ru.practicum.stats_server.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.stats_server.dto.EndpointHit;
import ru.practicum.stats_server.model.HitModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class HitMapper {
    public static HitModel toHitModel(EndpointHit endpointHit) {
        return HitModel.builder()
                .app(endpointHit.getApp())
                .ip(endpointHit.getIp())
                .uri(endpointHit.getUri())
                .timestamp(LocalDateTime.parse(endpointHit.getTimestamp(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    public static EndpointHit toEndpointHit(HitModel hitModel) {
        return EndpointHit.builder()
                .id(hitModel.getId())
                .app(hitModel.getApp())
                .ip(hitModel.getIp())
                .uri(hitModel.getUri())
                .timestamp(hitModel.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
