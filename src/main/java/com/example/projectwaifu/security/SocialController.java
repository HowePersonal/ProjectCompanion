package com.example.projectwaifu.security;

import com.example.projectwaifu.social.Friends;
import com.example.projectwaifu.social.FriendsKey;
import com.example.projectwaifu.message.Messages;
import com.example.projectwaifu.message.ConversationRepository;
import com.example.projectwaifu.message.MessageRepository;
import com.example.projectwaifu.social.SocialRepository;
import com.example.projectwaifu.user.UserRepository;
import com.example.projectwaifu.util.ApiResponse;
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

    @GetMapping("/friends/{friendUsername}/check")
    ResponseEntity<Object> checkFriends(@PathVariable String friendUsername, @AuthenticationPrincipal CustomUserDetails currUser) {
        if (userRepository.findByUsername(friendUsername) == null) { return new ResponseEntity<>(Map.of("message", "Invalid friend username"), HttpStatus.BAD_REQUEST);}
        return new ResponseEntity<>(Map.of("message", socialRepository.existsById(new FriendsKey(currUser.getUsername(), friendUsername))), HttpStatus.OK);
    }

    @PostMapping("/friends/{friendUsername}")
    ResponseEntity<Object> addFriend(@PathVariable String friendUsername, @AuthenticationPrincipal CustomUserDetails currUser) {
        if (friendUsername == null || userRepository.findByUsername(friendUsername) == null) { return new ResponseEntity<>(Map.of("message", "Invalid friend username"), HttpStatus.BAD_REQUEST);}
        socialRepository.save(new Friends(currUser.getUsername(), friendUsername));
        return new ResponseEntity<>(Map.of("message", "Saved friends successfully"), HttpStatus.OK);
    }

    @PostMapping("/conversations")
    ResponseEntity<Object> createConversation(@RequestBody Map<String, Integer> body, @AuthenticationPrincipal CustomUserDetails currUser) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            Integer friendId = body.get("friend_id");
            if (friendId == null || !userRepository.existsById(friendId)) {
                apiResponse.setResponse(Map.of("message", "Invalid friend id"), HttpStatus.BAD_REQUEST);
            }
            else {
                Integer conversationId = conversationRepository.createConversation(new Date());
                conversationRepository.createConversationParticipant(currUser.getId(), conversationId);
                conversationRepository.createConversationParticipant(friendId, conversationId);
                apiResponse.setResponse(Map.of("message", "Created conversation successfully"), HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            apiResponse.setResponse(Map.of("message", "Failed to create conversation"), HttpStatus.BAD_REQUEST);
        } finally {
            return apiResponse.toResponseEntity();
        }

    }

    @GetMapping("/conversations/{conversationId}/participants")
    List<Integer> getConversationParticipants(@PathVariable Integer conversationId) {
        return conversationRepository.getConversationParticipants(conversationId);
    }

    @GetMapping("/conversations/private")
    Integer getPrivateConversationId(@RequestParam Integer userIdOne, @RequestParam Integer userIdTwo) {
        return conversationRepository.getConversationID(userIdOne, userIdTwo);
    }


    @GetMapping("/conversations/{conversationId}/messages")
    List<Messages> getConversationMessages(@PathVariable Integer conversationId) {
        return messageRepository.getMessagesByConversationId(conversationId);
    }
}
