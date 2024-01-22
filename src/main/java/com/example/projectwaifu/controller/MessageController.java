package com.example.projectwaifu.controller;

import com.example.projectwaifu.models.entities.Messages;
import com.example.projectwaifu.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@CrossOrigin
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @MessageMapping("/conversation/{message.getConversationId()}")
    @SendTo("/queue/{message.getConversationId()}")
    public Messages sendConversationMessage(@Payload Messages message) {
        messageRepository.save(message);
        return message;
    }

    @MessageMapping("/public")
    @SendTo("/topic/public")
    public Messages broadcastNews(@Payload Messages message) {
        return message;
    }
}
