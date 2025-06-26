package com.example.backend.rest;

import com.example.backend.dtos.EventDto;
import com.example.backend.mappers.EventMapper;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.EventService;
import com.example.backend.shared.Constant;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @DeleteMapping("")
    public ResponseEntity<?> deleteEvents(@RequestBody List<String> eventIds) {
        eventService.deleteEvents(eventIds);
        return ResponseEntity.ok().build();
    }

    @PutMapping("")
    public ResponseEntity<?> editEvent(@RequestBody EventDto event) throws Exception {
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

        // Fetch data from Firebase
        Flux<Map> rawResponse = client.get()
            .retrieve()
            .bodyToFlux(Map.class);

        // Transform Firebase data to EventDto objects
        Flux<EventDto> eventDtoFlux = rawResponse.flatMap(this::convertMapToEventDtos);

        // Save events asynchronously without blocking the response
        eventDtoFlux
            .flatMap(this::saveEventAsync)
            .subscribe();

        return eventDtoFlux;
    }

    /**
     * Converts a Firebase response map to a Flux of EventDto objects
     *
     * @param firebaseData Map containing Firebase data
     * @return Flux of EventDto objects
     */
    private Flux<EventDto> convertMapToEventDtos(Map firebaseData) {
        List<EventDto> events = new ArrayList<>();

        firebaseData.forEach((key, value) -> {
            String id = (String) key;
            Map<String, Object> eventData = (LinkedHashMap<String, Object>) value;

            EventDto eventDto = EventDto.builder()
                .id(id)
                .title((String) eventData.get("title"))
                .allDay((Boolean) eventData.get("allDay"))
                .start((String) eventData.get("start"))
                .end((String) eventData.get("end"))
                .build();

            events.add(eventDto);
        });

        return Flux.fromIterable(events);
    }

    /**
     * Saves an event asynchronously
     *
     * @param eventDto The event to save
     * @return Mono containing the saved event
     */
    private Mono<EventDto> saveEventAsync(EventDto eventDto) {
        return Mono.fromCallable(() -> {
            try {
                eventService.save(eventDto, true);
                return eventDto;
            } catch (Exception e) {
                throw new RuntimeException("Failed to save event: " + eventDto.getId(), e);
            }
        }).onErrorResume(e -> {
            // Log error and continue with next event
            LOG.error("Error saving event {}: {}", eventDto.getId(), e.getMessage(), e);
            return Mono.empty();
        });
    }

    @GetMapping("/sync")
    public ResponseEntity<?> syncCalender() {
        return ResponseEntity.ok(null);
    }
    private static final Logger LOG = LoggerFactory.getLogger(EventController.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventService eventService;
}
