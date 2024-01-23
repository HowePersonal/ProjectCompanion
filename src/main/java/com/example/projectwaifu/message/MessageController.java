package com.example.projectwaifu.message;

import com.example.projectwaifu.message.Messages;
import com.example.projectwaifu.message.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @MessageMapping("/conversation/{message.getConversationId()}")
    @SendTo("/queue/conversation/{message.getConversationId()}")
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
