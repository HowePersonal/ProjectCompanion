package com.example.projectwaifu.message;

import com.example.projectwaifu.message.Conversation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer> {

    @Transactional
    @Query(value="INSERT INTO conversations (created_timestamp) VALUES (:date) RETURNING id", nativeQuery = true)
    Integer createConversation(Date date);

    @Modifying
    @Transactional
    @Query(value="INSERT INTO conversation_participants (user_id, conversation_id) VALUES (:userId, :conversationId)", nativeQuery = true)
    void createConversationParticipant(Integer userId, Integer conversationId);

    @Query(value="SELECT DISTINCT user_id FROM conversation_participants WHERE conversation_id = :conversationId", nativeQuery = true)
    List<Integer> getConversationParticipants(Integer conversationId);

    @Query(value = "SELECT DISTINCT c1.id FROM conversation_participants c1, conversation_participants c2 WHERE c1.user_id = :userIdOne AND c2.user_id = :userIdTwo", nativeQuery = true)
    Integer getConversationID(Integer userIdOne, Integer userIdTwo);

}
