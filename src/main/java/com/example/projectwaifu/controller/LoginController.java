package com.example.projectwaifu.controller;

import com.example.projectwaifu.models.User;
import com.example.projectwaifu.models.UserData;
import com.example.projectwaifu.other.UserManager;
import com.example.projectwaifu.repositories.UserDataRepository;
import com.example.projectwaifu.repositories.UserRepository;
import com.example.projectwaifu.security.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.example.projectwaifu.other.SecurityMethods.validatePassword;

@RestController
@RequestMapping("/security")
@CrossOrigin(origins = { "http://localhost:5173" }, allowedHeaders = "*", allowCredentials = "true")
public class LoginController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDataRepository userDataRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    SecurityContextRepository securityContextRepository;

    @Autowired
    UserManager userManager;


    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            if (!userManager.updateContext(loginRequest.getEmail(), loginRequest.getPassword(), httpServletRequest, httpServletResponse, false)) {
                throw new Exception("Invalid login");
            };

            String username = userRepository.getUsername(loginRequest.getEmail());
            Map<String, String> loginResponse = new HashMap<>();
            loginResponse.put("message", "Login Success");
            loginResponse.put("username", username);
            return new ResponseEntity<Object>(loginResponse, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<Object>(Map.of("message", "Login Failed"), HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            userManager.clearContext(httpServletRequest, httpServletResponse);
            return new ResponseEntity<Object>(Map.of("message", "Logout Success"), HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(Map.of("message", "Logout Failed"), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/authorities")
    public String getAuthorities() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();

    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return new ResponseEntity<>(Map.of("message", "Email already exists"), HttpStatus.BAD_REQUEST);
        }
        else if (userRepository.findByUsername(user.getUsername()) != null) {
            return new ResponseEntity<>(Map.of("message", "Username already exists"), HttpStatus.BAD_REQUEST);
        }

        if (!validatePassword(user.getPassword())) {
            return new ResponseEntity<>(Map.of("message", "Password not matching requirement"), HttpStatus.BAD_REQUEST);
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        userDataRepository.initializeCoins(user.getId(), 0);
        userDataRepository.save(new UserData(user.getId(), 0, "None", "Default"));
        return new ResponseEntity<>(Map.of("message", "Registration success"), HttpStatus.OK);
    }

}
