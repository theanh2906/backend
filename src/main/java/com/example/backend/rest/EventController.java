package com.example.backend.rest;

import com.example.backend.dtos.EventDto;
import com.example.backend.mappers.EventMapper;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.EventService;
import com.example.backend.shared.Constant;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
public class EventController {
    @Operation(summary = "Add new event")
    @PostMapping("/new")
    public ResponseEntity<?> addEvent(@RequestBody EventDto event) throws Exception {
        return ResponseEntity.ok(eventService.save(event, false));
    }

    @Operation(summary = "Find all events")
    @GetMapping("")
    public List<EventDto> findAll() {
        return eventService.findAll()
                .stream()
                .map(EventMapper::toDto)
                .sorted(Comparator.comparing(EventDto::getStart))
                .collect(Collectors.toList());
    }

    @GetMapping("/firebase")
    public Flux<EventDto> getFirebaseData() {
        WebClient client = WebClient.builder().baseUrl(Constant.Firebase.EVENTS_API).build();
        Flux<Map> rawResponse = client.get().retrieve().bodyToFlux(Map.class);
        Flux<EventDto> response = rawResponse.flatMap(map -> {
            List<EventDto> listEvents = new ArrayList<>();
            map.keySet().forEach((event -> {
                String id = (String) event;
                listEvents.add(EventDto.builder()
                        .id(id)
                        .title((String) ((LinkedHashMap<?, ?>) map.get(id)).get("title"))
                        .allDay((Boolean) ((LinkedHashMap<?, ?>) map.get(id)).get("allDay"))
                        .start((String) ((LinkedHashMap<?, ?>) map.get(id)).get("start"))
                        .end((String) ((LinkedHashMap<?, ?>) map.get(id)).get("end"))
                        .build());
            }));
            return Flux.fromIterable(listEvents);
        });
        response.flatMap((each) -> {
            try {
                eventService.save(each, true);
            } catch (Exception e) {
                return Flux.error(new RuntimeException(e));
            }
            return Mono.just(each);
        }).subscribe();
        return response;
    }

    @GetMapping("/sync")
    public ResponseEntity<?> syncCalender() {
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteEvents(@RequestBody List<String> eventIds) {
        try {
            eventService.deleteEvents(eventIds);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("")
    public ResponseEntity<?> editEvent(@RequestBody EventDto event) {
        try {
            return ResponseEntity.ok(eventService.save(event, false));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventService eventService;
}
