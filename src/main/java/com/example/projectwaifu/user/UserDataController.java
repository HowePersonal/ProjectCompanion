package com.example.projectwaifu.user;

import com.example.projectwaifu.user.UserData;
import com.example.projectwaifu.user.UserDataRepository;
import com.example.projectwaifu.user.UserRepository;
import com.example.projectwaifu.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getUserCoins() {
        CustomUserDetails currUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Map<String, Object> coins = Map.of("coins", userDataRepository.getCoins(currUser.getId()));
        return ResponseEntity.ok(coins);
    }

    @GetMapping("/history")
    public ResponseEntity<?> getUserHistory() {
        CustomUserDetails currUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Map<String, Object>> userChatLog = userRepository.getChatLog(currUser.getId());
        return ResponseEntity.ok(userChatLog);
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo() {
        CustomUserDetails currUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Map<String, Object> userInfo = Map.ofEntries(Map.entry("id", currUser.getId()), Map.entry("email", currUser.getEmail()), Map.entry("username", currUser.getUsername()));
        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
        CustomUserDetails currUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserData userData = userDataRepository.getUserData(currUser.getId());
        int totalMessages = userDataRepository.getUserTotalMessages(currUser.getId());

        Map<String, Object> userProfile = Map.ofEntries(Map.entry("profile_pic", userData.getProfilePic()), Map.entry("description", userData.getDescription()), Map.entry("tag", userData.getTag()), Map.entry("total_messages", totalMessages));
        return ResponseEntity.ok(userProfile);
    }
}
