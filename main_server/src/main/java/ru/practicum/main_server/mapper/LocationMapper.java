package ru.practicum.main_server.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main_server.dto.LocationDto;
import ru.practicum.main_server.model.Location;

@UtilityClass
public class LocationMapper {
    public static Location toLocation(LocationDto locationDto) {
        return Location.builder()
                .id(locationDto.getId())
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .name(locationDto.getName())
                .radius(locationDto.getRadius())
                .build();
    }

    public static LocationDto toLocationDto(Location location) {
        return LocationDto.builder()
                .id(location.getId())
                .lat(location.getLat())
                .lon(location.getLon())
                .name(location.getName())
                .build();
    }
}
