package com.example.projectwaifu.security;

import com.example.projectwaifu.other.UserManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        return new ResponseEntity<>(Map.of("message", "Registration success"), HttpStatus.OK);
    }

}
