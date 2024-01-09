package com.example.projectwaifu.controller;

import com.example.projectwaifu.repositories.UserDataRepository;
import com.example.projectwaifu.security.CustomUserDetails;
import com.example.projectwaifu.repositories.UserRepository;
import com.example.projectwaifu.service.GeminiAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = {"http://localhost:5173"}, allowedHeaders = "*", allowCredentials = "true")
public class AIController {

    @Autowired
    private GeminiAPI geminiAPI;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDataRepository userDataRepository;

    @PostMapping("/chat")
    public String getAIResponse(
            @RequestParam String input) {
        CustomUserDetails currUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String response = geminiAPI.getGeminiResponse(input);
        userRepository.saveChatLog(currUser.getId(), input, response);
        userDataRepository.incrementCoins(currUser.getId());
        return response;
    }
}
