package com.example.projectwaifu.util;

import com.example.projectwaifu.security.CustomUserDetails;
import com.example.projectwaifu.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

@Component
public class UserManager {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    SecurityContextRepository securityContextRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AuthenticationManager encodedAuthenticationManager;

    public boolean updateContext(String email, String password, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, boolean encoded) {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
            Authentication authentication;
            if (encoded) {
                authentication = encodedAuthenticationManager.authenticate(token);
            }
            else {
                authentication = authenticationManager.authenticate(token);
            }
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            SecurityContextHolder.setContext(context);
            securityContextRepository.saveContext(context, httpServletRequest, httpServletResponse);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public void clearContext(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        SecurityContextHolder.clearContext();
        SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
        securityContextRepository.saveContext(emptyContext, httpServletRequest, httpServletResponse);
    }

}
