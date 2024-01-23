package com.example.projectwaifu.message;

import jakarta.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "messages")
public class Messages implements Serializable {
    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "sender_id")
    private int senderId;

    @Column(name = "message_content")
    private String content;

    @Column(name = "conversation_id")
    private int conversationId;

    @Column(name = "sent_timestamp")
    private Timestamp sentTimestamp = new Timestamp(System.currentTimeMillis());

    public int getId() {
        return id;
    }

    public Timestamp getSentTimestamp() {
        return sentTimestamp;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }
}
