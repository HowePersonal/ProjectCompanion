package com.example.projectwaifu.models.entities;

import jakarta.persistence.*;

@Entity
public class ConversationParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "conversation_id")
    private Integer conversationId;
}
