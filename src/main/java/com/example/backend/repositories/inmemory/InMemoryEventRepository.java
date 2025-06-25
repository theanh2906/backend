package com.example.backend.repositories.inmemory;

import com.example.backend.models.Event;
import com.example.backend.repositories.EventRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@Primary
public class InMemoryEventRepository extends InMemoryRepository<Event, String> implements EventRepository {
    
    public InMemoryEventRepository() {
        super(Event::getId);
    }
    
    @Override
    public List<Event> findAllByUser(String userId) {
        return storage.values().stream()
                .filter(event -> event.getUser() != null && userId.equals(event.getUser().getId()))
                .collect(Collectors.toList());
    }
    
    @Override
    public <S extends Event> S save(S entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID().toString());
        }
        return super.save(entity);
    }
}