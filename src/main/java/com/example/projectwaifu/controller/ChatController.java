package com.example.projectwaifu.controller;

import com.example.projectwaifu.security.CustomUserDetails;
import com.example.projectwaifu.security.UserRepository;
import com.example.projectwaifu.service.GeminiAPI;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"}, allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private GeminiAPI geminiAPI;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/AI")
    public String getAIResponse(
            @RequestParam String input) {
        CustomUserDetails currUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String response = geminiAPI.getGeminiResponse(input);
        userRepository.saveChatLog(currUser.getId(), input, response);
        return response;
    }
}
