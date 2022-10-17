package ru.practicum.main_server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_server.dto.LocationDto;
import ru.practicum.main_server.service.LocationService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/locations")
@Slf4j
public class LocationAdminController {
    private final LocationService locationService;

    public LocationAdminController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    public LocationDto create(@RequestBody @Valid LocationDto locationDto) {
        log.info("save location");
        return locationService.save(locationDto);
    }

    @GetMapping
    public List<LocationDto> getLocations(
            @RequestParam(defaultValue = "0", required = false) int from,
            @RequestParam(defaultValue = "10", required = false) int size) {
        log.info("get locations");
        return locationService.getLocations(from, size);
    }

    @PatchMapping
    public LocationDto updateLocation(@RequestBody LocationDto locationDto) {
        log.info("update location");
        return locationService.update(locationDto);
    }

    @GetMapping("/distance")
    public List<LocationDto> distance(@RequestBody LocationDto locationDto) {
        log.info("distance location");
        return locationService.distance(locationDto);
    }
}
