package ru.practicum.stats_server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndpointHit {
    private long id;

    private String app;

    private String uri;

    private String ip;

    @NotNull
    private String timestamp;
}
