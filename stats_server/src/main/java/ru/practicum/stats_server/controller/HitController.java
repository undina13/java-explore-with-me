package ru.practicum.stats_server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats_server.dto.EndpointHit;
import ru.practicum.stats_server.dto.ViewStats;
import ru.practicum.stats_server.service.HitService;

import javax.validation.Valid;
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
    public EndpointHit createHit(@RequestBody @Valid EndpointHit endpointHit) {
        log.info("create hit {}", endpointHit);
        return hitService.createHit(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getViewStats(@RequestParam String start,
                                        @RequestParam String end,
                                        @RequestParam List<String> uris,
                                        @RequestParam(defaultValue = "false") Boolean unique) throws UnsupportedEncodingException {
        log.info("getViewStats {}", uris);
        return hitService.getViewStats(start, end, uris, unique);
    }
}
