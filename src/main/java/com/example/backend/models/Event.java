package com.example.backend.models;

import lombok.Data;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "EVENTS")
@Data
public class Event implements Serializable {
    @Serial
    private static final long serialVersionUID = -5762243842656831439L;
    @Id
    private String id;
    @Column
    private Boolean allDay;
    @Column(name = "START_DATE")
    private String start;
    @Column(name = "END_DATE")
    private String end;
    @Column
    private String title;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID")
    private User user;
}
