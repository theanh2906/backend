package com.example.backend.repositories;

import com.example.backend.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, String> {
    @Modifying
    @Query("DELETE FROM Note n WHERE n.id = :id")
    Integer deleteNotes(@Param("id") String id);

    @Query("SELECT n FROM Note n WHERE n.user.id = :userId")
    List<Note> findAllByUser(String userId);

    @Query("SELECT n FROM Note n WHERE n.id = :id")
    Note findNote(String id);
}
