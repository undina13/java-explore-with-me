package ru.practicum.main_server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {

    private Long id;

    private String name;

    @NotNull
    private Float lat;

    @NotNull
    private Float lon;

    private Float radius;
}
