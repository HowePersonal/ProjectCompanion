package com.example.projectwaifu.message;


import com.example.projectwaifu.message.Messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Messages, Long> {

    @Query("SELECT m FROM Messages m WHERE m.conversationId = :conversationId ORDER BY m.sentTimestamp")
    List<Messages> getMessagesByConversationId(Integer conversationId);
}
