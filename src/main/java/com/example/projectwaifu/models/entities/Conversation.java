package com.example.projectwaifu.models.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_timestamp")
    private Date createdTimestamp = new Date();

}
