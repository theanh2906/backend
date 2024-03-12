package com.example.backend.rest;

import com.example.backend.dtos.NoteDto;
import com.example.backend.dtos.ResponseDto;
import com.example.backend.mappers.NoteMapper;
import com.example.backend.services.NoteService;
import com.example.backend.shared.Constant;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/api/notes")
public class NoteController {
    //    @CacheEvict(value = "notes", allEntries = true)
    @PostMapping("/new")
    @Operation(summary = "Add new note")
    public ResponseEntity<?> addNote(@RequestBody NoteDto note) {
        return ResponseEntity.ok(noteService.addNote(note, false));
    }

    //    @CacheEvict(value = "notes", allEntries = true)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete note by id")
    public ResponseEntity<?> deleteNote(@PathVariable String id) {
        return noteService.deleteNote(id) == 1 ?
                ResponseEntity.ok(new ResponseDto<>(String.format("Successfully delete note with id %s", id))) :
                ResponseEntity.badRequest().body(new ResponseDto<>(String.format("Failed to delete note with id %s", id)));
    }

    //    @Cacheable(value = "notes")
    @GetMapping("")
    @Operation(summary = "Find all notes")
    public List<NoteDto> findAll() {
        return noteService
                .findAll()
                .stream()
                .map(NoteMapper::toDto)
                .sorted(Comparator.comparingInt(a -> ((Long) a.getCreatedDate()).intValue()))
                .collect(Collectors.toList());
    }

    @GetMapping("/firebase")
    public Flux<NoteDto> getFirebaseData() {
        WebClient client = WebClient.builder().baseUrl(Constant.Firebase.NOTES_API).build();
        Flux<Map> rawResponse = client.get().retrieve().bodyToFlux(Map.class);
        Flux<NoteDto> response = rawResponse.flatMap(map -> {
            List<NoteDto> listNotes = new ArrayList<>();
            map.keySet().forEach(note -> {
                String id = (String) note;
                listNotes.add(NoteDto.builder()
                        .id(id)
                        .title((String) ((LinkedHashMap<?, ?>) map.get(id)).get("title"))
                        .content((String) ((LinkedHashMap<?, ?>) map.get(id)).get("content"))
                        .createdDate(((LinkedHashMap<?, ?>) map.get(id)).get("createdDate"))
                        .build());
            });
            return Flux.fromIterable(listNotes);
        });
        response.flatMap((each) -> {
            try {
                noteService.addNote(each, true);
            } catch (Exception e) {
                return Flux.error(new RuntimeException(e));
            }
            return Mono.just(each);
        }).subscribe();
        return response;
    }

    @PutMapping("")
    @Operation(summary = "Update note by id")
    public ResponseEntity<?> updateNote(@RequestBody final NoteDto note) {
        return ResponseEntity.ok(noteService.updateNote(note));
    }

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    @Autowired
    private NoteService noteService;
}
