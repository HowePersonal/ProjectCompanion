package com.example.projectwaifu.controller;

import com.example.projectwaifu.security.CustomUserDetails;
import com.example.projectwaifu.security.User;
import com.example.projectwaifu.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"}, allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("history")
    public List<Map<String, Object>> getUserHistory() {
        CustomUserDetails currUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.getChatLog(currUser.getId());
    }
}
