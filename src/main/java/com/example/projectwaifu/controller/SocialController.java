package com.example.projectwaifu.controller;

import com.example.projectwaifu.models.entities.Friends;
import com.example.projectwaifu.models.entities.FriendsKey;
import com.example.projectwaifu.repositories.SocialRepository;
import com.example.projectwaifu.repositories.UserRepository;
import com.example.projectwaifu.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/social")
@CrossOrigin(origins = { "http://localhost:5173" }, allowedHeaders = "*", allowCredentials = "true")
public class SocialController {

    @Autowired
    SocialRepository socialRepository;

    @Autowired
    UserRepository userRepository;


    @GetMapping("/friends")
    List<String> getFriends(@AuthenticationPrincipal CustomUserDetails currUser) {
        return socialRepository.getFriends(currUser.getUsername());
    }

    @GetMapping("/isfriends")
    ResponseEntity<Object> isFriends(@RequestParam String friendUsername, @AuthenticationPrincipal CustomUserDetails currUser) {
        if (userRepository.findByUsername(friendUsername) == null) { return new ResponseEntity<>(Map.of("message", "Invalid friend username"), HttpStatus.BAD_REQUEST);}
        return new ResponseEntity<>(Map.of("message", socialRepository.existsById(new FriendsKey(currUser.getUsername(), friendUsername))), HttpStatus.OK);
    }

    @PostMapping("/addfriend")
    ResponseEntity<Object> addFriend(@RequestBody Map<String, String> friendInfo, @AuthenticationPrincipal CustomUserDetails currUser) {
        String friendUsername = friendInfo.get("username");
        if (friendUsername == null || userRepository.findByUsername(friendUsername) == null) { return new ResponseEntity<>(Map.of("message", "Invalid friend username"), HttpStatus.BAD_REQUEST);}
        socialRepository.save(new Friends(currUser.getUsername(), friendUsername));
        return new ResponseEntity<>(Map.of("message", "Saved friends successfully"), HttpStatus.OK);
    }

}
