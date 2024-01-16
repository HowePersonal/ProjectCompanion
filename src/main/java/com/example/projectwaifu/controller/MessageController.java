package com.example.projectwaifu.controller;

import com.example.projectwaifu.models.entities.Messages;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/message")
@CrossOrigin
public class MessageController {


    @MessageMapping("/public")
    @SendTo("/topic/public")
    public Messages broadcastNews(@Payload Messages message) {
        return message;
    }
}
