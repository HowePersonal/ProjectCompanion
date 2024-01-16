package com.example.projectwaifu.controller;

import com.example.projectwaifu.models.entities.UserData;
import com.example.projectwaifu.repositories.UserDataRepository;
import com.example.projectwaifu.repositories.UserRepository;
import com.example.projectwaifu.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user/data")
@CrossOrigin(origins = { "http://localhost:5173" }, allowedHeaders = "*", allowCredentials = "true")
public class UserDataController {

    @Autowired
    UserDataRepository userDataRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/coins")
    public Map<String, Object> getUserCoins() {
        CustomUserDetails currUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Map.of("coins", userDataRepository.getCoins(currUser.getId()));
    }

    @GetMapping("/history")
    public List<Map<String, Object>> getUserHistory() {
        CustomUserDetails currUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.getChatLog(currUser.getId());
    }

    @GetMapping("/info")
    public Map<String, Object> getUserInfo() {
        CustomUserDetails currUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Map.ofEntries(Map.entry("id", currUser.getId()), Map.entry("email", currUser.getEmail()), Map.entry("username", currUser.getUsername()));
    }

    @GetMapping("/profile")
    public Map<String, Object> getUserProfile() {
        CustomUserDetails currUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserData userData = userDataRepository.getUserData(currUser.getId());
        int totalMessages = userDataRepository.getUserTotalMessages(currUser.getId());
        return Map.ofEntries(Map.entry("profile_pic", userData.getProfilePic()), Map.entry("description", userData.getDescription()), Map.entry("tag", userData.getTag()), Map.entry("total_messages", totalMessages));
    }
}
