package com.example.projectwaifu.security;

import com.example.projectwaifu.user.User;
import com.example.projectwaifu.user.UserData;
import com.example.projectwaifu.util.UserManager;
import com.example.projectwaifu.user.UserDataRepository;
import com.example.projectwaifu.user.UserRepository;
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

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static com.example.projectwaifu.security.SecurityMethods.validatePassword;

@RestController
@RequestMapping("/security")
@CrossOrigin(origins = { "http://localhost:5173" }, allowedHeaders = "*", allowCredentials = "true")
public class SecurityController {

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

    @Autowired
    SecurityMethods securityMethods;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            boolean encoded = !(loginRequest.getPassword().equals("oauth2/openid")) ? false : true;
            if (!userManager.updateContext(loginRequest.getEmail(), loginRequest.getPassword(), httpServletRequest, httpServletResponse, encoded)) {
                throw new Exception("Invalid login");
            };

            String username = userRepository.getUsername(loginRequest.getEmail());
            Map<String, String> loginResponse = new HashMap<>();
            loginResponse.put("message", "Login Success");
            loginResponse.put("username", username);
            return ResponseEntity.ok(loginResponse);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("message", "Login Failed"));
        }
    }

    @GetMapping("/oauth2/login")
    public void oauth2Login(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        httpServletRequest.getSession().setAttribute("security_request", "login");
        httpServletResponse.sendRedirect("/oauth2/authorization/google");
    }

    @GetMapping("/oauth2/register")
    public void oauth2Register(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        httpServletRequest.getSession().setAttribute("security_request", "register");
        httpServletResponse.sendRedirect("/oauth2/authorization/google");

    }

    @GetMapping("/oauth2/callback")
    public ResponseEntity<?> oauth2Callback(
            @RequestParam(name = "state") String state,
            @RequestParam(name = "code") String code,
            @RequestParam(name = "scope") String scope,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) throws IOException, URISyntaxException, InterruptedException {
            String securityRequest = (String) httpServletRequest.getSession().getAttribute("security_request");
            if (securityRequest == null) {
                return new ResponseEntity<Object>(Map.of("message", "Oauth2 Request Failed"), HttpStatus.BAD_REQUEST);
            }
            String userEmail = securityMethods.oauth2CodeToEmail(code);
            httpServletResponse.sendRedirect("/");
            if (securityRequest.equals("login"))
                return login(new LoginRequest(userEmail, "oauth2/openid"), httpServletRequest, httpServletResponse);
            else
                return register(new User(userEmail, userEmail, "oauth2/openid"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            userManager.clearContext(httpServletRequest, httpServletResponse);
            return ResponseEntity.ok(Map.of("message", "Logout Success"));
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("message", "Logout Failed"));
        }
    }

    @GetMapping("/authorities")
    public ResponseEntity<?> getAuthorities() {
        String authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        return ResponseEntity.ok(authorities);

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Email already exists"));
        }
        else if (userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Username already exists"));
        }

        if (!validatePassword(user.getPassword())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Password not matching requirement"));
        }

        String encodedPassword = user.getPassword();
        if (!user.getPassword().equals("oauth2/openid"))
            encodedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);
        userRepository.save(user);
        userDataRepository.initializeCoins(user.getId(), 0);
        userDataRepository.save(new UserData(user.getId(), 0, "None", "Default"));
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Registration Success"));
    }

}
