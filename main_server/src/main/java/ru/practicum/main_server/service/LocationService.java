package ru.practicum.main_server.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_server.dto.LocationDto;
import ru.practicum.main_server.exception.ObjectNotFoundException;
import ru.practicum.main_server.mapper.LocationMapper;
import ru.practicum.main_server.model.Location;
import ru.practicum.main_server.repository.LocationRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LocationService {
    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public LocationDto save(LocationDto locationDto) {
        if (findByLatAndLon(locationDto.getLat(), locationDto.getLon()).isPresent()) {
            return LocationMapper.toLocationDto(findByLatAndLon(locationDto.getLat(), locationDto.getLon()).get());
        }
        Location location = LocationMapper.toLocation(locationDto);

        return LocationMapper.toLocationDto(locationRepository.save(location));
    }

    public Optional<Location> findByLatAndLon(float lat, float lon) {
        return locationRepository.findByLatAndLon(lat, lon);
    }

    public List<LocationDto> getLocations(int from, int size) {
        return locationRepository.findAll(PageRequest.of(from / size, size))
                .stream()
                .map(LocationMapper::toLocationDto)
                .collect(Collectors.toList());
    }

    public LocationDto update(LocationDto locationDto) {
        Location location = locationRepository.findById(locationDto.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Location not found"));
        Optional.ofNullable(locationDto.getLat())
                .ifPresent(location::setLat);
        Optional.ofNullable(locationDto.getLon())
                .ifPresent(location::setLon);
        Optional.ofNullable(locationDto.getName())
                .ifPresent(location::setName);
        Optional.ofNullable(locationDto.getRadius())
                .ifPresent(location::setRadius);
        return LocationMapper.toLocationDto(locationRepository.save(location));
    }

    public List<LocationDto> distance(LocationDto locationDto) {
        return locationRepository.findLocationsInRadius(locationDto.getLat(), locationDto.getLon(), locationDto
                .getRadius())
                .stream()
                .map(LocationMapper::toLocationDto)
                .collect(Collectors.toList());
    }

    ;
}
