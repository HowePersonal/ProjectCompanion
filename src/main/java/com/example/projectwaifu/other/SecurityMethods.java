
package com.example.projectwaifu.other;

import java.io.IOException;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.security.SecureRandom;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SecurityMethods {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    String oauth2ClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    String oauth2ClientSecret;

    HttpClient httpClient = HttpClient.newHttpClient();

    Gson gson = new Gson();

    public static boolean validatePassword(String password) {
        String passwordPattern = "^(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{6,}$";
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public void createOauth2Token(HttpServletRequest request) {
        String stateToken = new BigInteger(130, new SecureRandom()).toString();
        request.getSession().setAttribute("state", stateToken);
    }

    public void exchangeOauth2Code(String code) throws IOException, URISyntaxException, InterruptedException {
        String requestURI = "https://oauth2.googleapis.com/token";


        String requestBody = gson.toJson(Map.ofEntries(Map.entry("code", code), Map.entry("client_id", oauth2ClientId),
                Map.entry("client_secret", oauth2ClientSecret), Map.entry("redirect_uri", "http://localhost:8080/security/oauth2/login"),
                Map.entry("grant_type", "authorization_code")));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(requestURI))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

    }


}
