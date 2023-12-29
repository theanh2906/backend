package com.example.backend.services;

import com.example.backend.models.Event;
import com.example.backend.models.User;
import com.example.backend.repositories.EventRepository;
import com.example.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class EventService {
    public Event addEvent(Event event, String userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(Exception::new);
        event.setId(UUID.randomUUID().toString());
        event.setUser(user);
        return eventRepository.save(event);
    }

    @Transactional
    public void deleteAll() {
        eventRepository.deleteAll();
    }

    public List<Event> findAllByUser(String userId) {
        return eventRepository.findAllByUser(userId);
    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;
}
