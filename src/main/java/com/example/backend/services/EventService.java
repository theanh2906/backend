package com.example.backend.services;

import com.example.backend.models.Event;
import com.example.backend.repositories.EventRepository;
import com.example.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class EventService {
    public Event addEvent(Event event, Boolean isSynced) throws Exception {
        event.setId(isSynced ? event.getId() : UUID.randomUUID().toString());
        event.setUser(null);
        return eventRepository.save(event);
    }

    @Transactional
    public void deleteAll() {
        eventRepository.deleteAll();
    }

    @Transactional
    public void deleteEvents(List<String> eventIds) {
        eventRepository.deleteAllById(eventIds);
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public List<Event> findAllByUser(String userId) {
        return eventRepository.findAllByUser(userId);
    }
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;
}
