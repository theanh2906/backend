package com.example.backend.services;

import com.example.backend.exceptions.UsefulToolsException;
import com.example.backend.models.ChatRoom;
import com.example.backend.repositories.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomService {
    @Autowired
    private ChatRoomRepository repository;

    public ChatRoom createRoom(String name) {
        ChatRoom saveRoom = new ChatRoom();
        if (repository.findByNameIgnoreCase(name).isPresent()) {
            throw new UsefulToolsException("Name already exists!");
        }
        return repository.save(ChatRoom.builder().name(name).build());
    }
}
