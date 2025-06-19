package com.example.backend.repositories.inmemory;

import com.example.backend.models.ChatRoom;
import com.example.backend.repositories.ChatRoomRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
@Primary
public class InMemoryChatRoomRepository extends InMemoryRepository<ChatRoom, String> implements ChatRoomRepository {
    
    public InMemoryChatRoomRepository() {
        super(ChatRoom::getId);
        
        // Initialize with a default chat room
        ChatRoom defaultRoom = ChatRoom.builder()
                .id(UUID.randomUUID().toString())
                .name("General")
                .createdDate(LocalDateTime.now())
                .build();
        storage.put(defaultRoom.getId(), defaultRoom);
    }
    
    @Override
    public Optional<ChatRoom> findByNameIgnoreCase(String name) {
        return storage.values().stream()
                .filter(chatRoom -> chatRoom.getName() != null && 
                        chatRoom.getName().equalsIgnoreCase(name))
                .findFirst();
    }
    
    @Override
    public <S extends ChatRoom> S save(S entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID().toString());
        }
        if (entity.getCreatedDate() == null) {
            entity.setCreatedDate(LocalDateTime.now());
        }
        return super.save(entity);
    }
}