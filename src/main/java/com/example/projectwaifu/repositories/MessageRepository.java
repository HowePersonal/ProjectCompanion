package com.example.projectwaifu.repositories;


import com.example.projectwaifu.models.entities.Messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Repository
public interface MessageRepository extends JpaRepository<Messages, Long> {

    @Query("SELECT m FROM Messages m WHERE m.conversationId = :conversationId ORDER BY m.sentTimestamp")
    List<Messages> getMessagesByConversationId(Integer conversationId);
}
