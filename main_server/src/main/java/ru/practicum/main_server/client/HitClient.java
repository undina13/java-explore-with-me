//package ru.practicum.main_server.client;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.web.client.RestTemplateBuilder;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.util.DefaultUriBuilderFactory;
//import ru.practicum.main_server.dto.EndpointHit;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class HitClient extends BaseClient {
//
//    @Autowired
//    public HitClient(@Value("${stat-server.url}") String serverUrl, RestTemplateBuilder builder) {
//        super(
//                builder
//                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
//                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
//                        .build()
//        );
//    }
//
//    public ResponseEntity<Object> createHit(EndpointHit endpointHit) {
//        return post("/hit", endpointHit);
//    }
//
//    public ResponseEntity<Object> getStat(LocalDateTime start, LocalDateTime end,
//                                          List<String> uris, Boolean unique) throws UnsupportedEncodingException {
//        Map<String, Object> parameters = Map.of(
//                "start", URLEncoder.encode(start.toString(), StandardCharsets.UTF_8.toString()),
//                "end", URLEncoder.encode(end.toString(), StandardCharsets.UTF_8.toString()),
//                "uris", uris,
//                "unique", unique
//        );
//        return get("/stats?start=" + parameters.get("start") + "&end=" + parameters.get("end") +
//                "&uris=" + String.join(", ", uris) + "&unique=" + unique, parameters);
//    }
//}
