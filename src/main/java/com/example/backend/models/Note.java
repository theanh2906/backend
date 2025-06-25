package com.example.backend.models;

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
public class Note implements Serializable {
    public Set<Note> getCategories() {
        return categories;
    }

    public void setCategories(Set<Note> categories) {
        this.categories = categories;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Long lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
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
