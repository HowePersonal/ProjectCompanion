package com.example.projectwaifu.controller;

import com.example.projectwaifu.models.entities.Friends;
import com.example.projectwaifu.models.entities.FriendsKey;
import com.example.projectwaifu.models.entities.Messages;
import com.example.projectwaifu.repositories.ConversationRepository;
import com.example.projectwaifu.repositories.MessageRepository;
import com.example.projectwaifu.repositories.SocialRepository;
import com.example.projectwaifu.repositories.UserRepository;
import com.example.projectwaifu.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    ConversationRepository conversationRepository;


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

    @PostMapping("/createconversation")
    ResponseEntity<Object> createConversation(@RequestBody Map<String, Integer> body, @AuthenticationPrincipal CustomUserDetails currUser) {
        try {
            Integer friendId = body.get("friend_id");
            if (friendId == null || !userRepository.existsById(friendId)) {
                return new ResponseEntity<>(Map.of("message", "Invalid friend id"), HttpStatus.BAD_REQUEST);
            }
            Integer conversationId = conversationRepository.createConversation(new Date());
            conversationRepository.createConversationParticipant(currUser.getId(), conversationId);
            conversationRepository.createConversationParticipant(friendId, conversationId);
            return new ResponseEntity<>(Map.of("message", "Created conversation successfully"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(Map.of("message", "Failed to create conversation"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getconversationparticipants")
    List<Integer> getConversationParticipants(@RequestParam Integer conversationId) {
        return conversationRepository.getConversationParticipants(conversationId);
    }

    @GetMapping("/getprivateconversationid")
    Integer getPrivateConversationId(@RequestParam Integer userIdOne, @RequestParam Integer userIdTwo) {
        return conversationRepository.getConversationID(userIdOne, userIdTwo);
    }

    @PostMapping("/addmessage")
    ResponseEntity<Object> addMessage(@RequestBody Messages messages) {
        try {
            messageRepository.save(messages);
            return new ResponseEntity<>(Map.of("message", "Successfully saved message"), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(Map.of("message", "Failed to save message"), HttpStatus.BAD_REQUEST);
        }
    }
}
