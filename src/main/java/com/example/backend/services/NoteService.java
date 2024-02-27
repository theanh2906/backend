package com.example.backend.services;

import com.example.backend.dtos.NoteDto;
import com.example.backend.mappers.NoteMapper;
import com.example.backend.models.Note;
import com.example.backend.repositories.NoteRepository;
import com.example.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class NoteService {
    public NoteDto addNote(NoteDto note, Boolean isSynced) {
//        User currentUser = SecurityUtils.getCurrentUser().toModel();
        final Note savedNote = NoteMapper.toModel(note);
        savedNote.setUser(null);
        savedNote.setId(isSynced ? note.getId() : UUID.randomUUID().toString());
        savedNote.setTitle(note.getTitle());
        savedNote.setContent(note.getContent());
        savedNote.setCategories(null);
        return NoteMapper.toDto(noteRepository.save(savedNote));
    }

    @Transactional
    public void deleteAll() {
        noteRepository.deleteAll();
    }

    @Transactional
    public Integer deleteNote(String id) {
        return noteRepository.deleteNotes(id);
    }

    public List<Note> findAll() {
        return noteRepository
                .findAll()
                .stream()
                .sorted(Comparator.comparing(Note::getCreatedDate).reversed())
                .toList();
    }

    @Transactional
    public Note updateNote(NoteDto note) {
        Note savedNote = noteRepository.findNote(note.getId());
        savedNote.setLastModifiedDate(new Date().getTime());
        savedNote.setContent(note.getContent());
        savedNote.setTitle(note.getTitle());
        return noteRepository.save(savedNote);
    }


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteRepository noteRepository;
}
