package com.example.projectwaifu.security;

import com.example.projectwaifu.security.User;
import com.example.projectwaifu.security.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = { "http://localhost:5173" }, allowedHeaders = "*", allowCredentials = "true")
public class LoginController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    SecurityContextRepository securityContextRepository;


    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            securityContextRepository.saveContext(context, httpServletRequest, httpServletResponse);
            String sessionId = "No session";
            Cookie[] cookies = httpServletRequest.getCookies();
            if (cookies != null) {
                for (Cookie cookie: cookies) {
                    if ("SESSION".equals(cookie.getName())) {
                        sessionId = cookie.getValue();
                    }
                }
            }

            return sessionId;
        }
        catch (AuthenticationException e) {
            return "Login Failed";
        }
    }

    @GetMapping("/authorities")
    public String getAuthorities() {
       return SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();

    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return "REGISTRATION SUCCESS";
    }
}
