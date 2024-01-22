package com.example.projectwaifu.controller;

import com.example.projectwaifu.models.entities.Messages;
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
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/conversation")
    public void sendConvo(@Payload Messages message) {
        simpMessagingTemplate.convertAndSend("/queue/"+message.getConversationId(), message);
    }

    @MessageMapping("/public")
    @SendTo("/topic/public")
    public Messages broadcastNews(@Payload Messages message) {
        return message;
    }
}
