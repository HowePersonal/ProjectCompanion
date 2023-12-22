package com.example.projectwaifu.controller;

import com.example.projectwaifu.service.GeminiAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private GeminiAPI geminiAPI;


    @PostMapping("/AI")
    public String getAIResponse(
            @RequestParam String input) {
        return geminiAPI.getGeminiResponse(input);
    }
}
