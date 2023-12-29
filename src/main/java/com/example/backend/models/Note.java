package com.example.backend.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "NOTES")
@Data
public class Note implements Serializable {
    @Serial
    private static final long serialVersionUID = 1588300952381948680L;
    @Id
    private String id;
    @Column
    private String content;
    @Column
    private String title;
    @Column
    private Long createdDate;
    @Column
    private Long lastModifiedDate;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "CATEGORY_NOTE", joinColumns = @JoinColumn(name = "NOTE_ID"), inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID"))
    private Set<Note> categories = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
