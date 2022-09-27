package ru.practicum.stat_server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stat_server.dto.EndpointHit;
import ru.practicum.stat_server.dto.ViewStats;
import ru.practicum.stat_server.service.HitService;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@Slf4j
public class HitController {
    private final HitService hitService;

    @Autowired
    public HitController(HitService hitService) {
        this.hitService = hitService;
    }

    @PostMapping("/hit")
    public EndpointHit createHit(@RequestBody EndpointHit endpointHit) {
        log.info("create hit");
        return hitService.createHit(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getViewStats(@RequestParam String start,
                                        @RequestParam String end,
                                        @RequestParam List<String> uris,
                                        @RequestParam(defaultValue = "false") Boolean unique) throws UnsupportedEncodingException {
        return hitService.getViewStats(start, end, uris, unique);
    }
}
