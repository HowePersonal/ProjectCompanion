package com.example.projectwaifu.other;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SecurityMethods {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    String oauth2ClientId;

    HttpClient httpClient = HttpClient.newHttpClient();

    public static boolean validatePassword(String password) {
        String passwordPattern = "^(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{6,}$";
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public void createOauth2Token(HttpServletRequest request, String state, String scope) throws URISyntaxException, IOException, InterruptedException {
        String stateToken = new BigInteger(130, new SecureRandom()).toString();
        request.getSession().setAttribute("state", stateToken);

        String requestURI = String.format(
                "https://accounts.google.com/o/oauth2/v2/auth?" +
                        "response_type=code&" +
                        "client_id=%s&" +
                        "scope=%s&" +
                        "redirect_uri=http://localhost:8080/security/oauth2/authenticated" +
                        "state=%s&" +
                        "nonce=0394852-3190485-2490358&", oauth2ClientId, scope, state
                        );

        HttpRequest authRequest = HttpRequest.newBuilder()
                .uri(new URI(requestURI))
                .GET()
                .build();

        httpClient.send(authRequest, HttpResponse.BodyHandlers.ofString());

    }

}
