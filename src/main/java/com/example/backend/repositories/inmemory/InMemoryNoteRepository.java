package com.example.backend.repositories.inmemory;

import com.example.backend.models.Note;
import com.example.backend.repositories.NoteRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@Primary
public class InMemoryNoteRepository extends InMemoryRepository<Note, String> implements NoteRepository {
    
    public InMemoryNoteRepository() {
        super(Note::getId);
    }
    
    @Override
    public Integer deleteNotes(String id) {
        if (storage.containsKey(id)) {
            storage.remove(id);
            return 1;
        }
        return 0;
    }
    
    @Override
    public List<Note> findAllByUser(String userId) {
        return storage.values().stream()
                .filter(note -> note.getUser() != null && userId.equals(note.getUser().getId()))
                .collect(Collectors.toList());
    }
    
    @Override
    public Note findNote(String id) {
        return storage.get(id);
    }
    
    @Override
    public <S extends Note> S save(S entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID().toString());
        }
        
        // Set timestamps if not already set
        if (entity.getCreatedDate() == null) {
            entity.setCreatedDate(System.currentTimeMillis());
        }
        entity.setLastModifiedDate(System.currentTimeMillis());
        
        return super.save(entity);
    }
}