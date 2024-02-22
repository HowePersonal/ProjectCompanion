package com.example.projectwaifu.user;

import com.example.projectwaifu.util.UserManager;
import com.example.projectwaifu.user.UserDataRepository;
import com.example.projectwaifu.security.CustomUserDetails;
import com.example.projectwaifu.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.projectwaifu.security.SecurityMethods.validatePassword;

@RestController
@RequestMapping("/api/user/change")
@CrossOrigin(origins = { "http://localhost:5173" }, allowedHeaders = "*", allowCredentials = "true")
public class UserChangeController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDataRepository userDataRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    SecurityContextRepository securityContextRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserManager userManager;



    @PutMapping("/username")
    public ResponseEntity<?> changeUsername(@RequestBody Map<String, String> userInfo, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @AuthenticationPrincipal CustomUserDetails currUser) {
        String newUsername = userInfo.get("username");

        if (newUsername == null || newUsername.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "No username provided"));
        }

        if (userRepository.findByUsername(newUsername) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Username already exists");
        }

        currUser.setUsername(newUsername);
        userRepository.changeUsername(newUsername, currUser.getEmail());
        userManager.updateContext(currUser.getEmail(), currUser.getPassword(), httpServletRequest, httpServletResponse, true);

        return ResponseEntity.ok(Map.of("message", "Username changed"));
    }

    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> userInfo, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @AuthenticationPrincipal CustomUserDetails currUser) {
        String oldPassword = userInfo.get("oldpassword");
        String newPassword = userInfo.get("newpassword");

        if (oldPassword == null || newPassword == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Improper request body"));
        }

        if (!passwordEncoder.matches(oldPassword, currUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Incorrect old password"));
        }
        else if (!validatePassword(newPassword)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid new password"));
        }

        String encodedNewPassword = passwordEncoder.encode(newPassword);

        currUser.setPassword(newPassword);
        userRepository.changePassword(encodedNewPassword, currUser.getEmail());
        userManager.updateContext(currUser.getEmail(), newPassword, httpServletRequest, httpServletResponse, false);

        return ResponseEntity.ok(Map.of("message", "Password changed"));
    }

    @PutMapping("/description")
    public ResponseEntity<?> changeDescription(@RequestBody Map<String, String> data, @AuthenticationPrincipal CustomUserDetails currUser) {
        String newDescription = data.get("description");

        if (newDescription == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Improper request body"));
        }

        userDataRepository.updateDescriptionByUserId(currUser.getId(), newDescription);
        return ResponseEntity.ok(Map.of("message", "Description changed successfully"));
    }



}
