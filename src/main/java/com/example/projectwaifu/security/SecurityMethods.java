
package com.example.projectwaifu.security;

import java.io.IOException;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SecurityMethods {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String oauth2ClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String oauth2ClientSecret;

    @Value("${base-domain}")
    private String base_domain;

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

    public String oauth2CodeToEmail(String code) throws IOException, URISyntaxException, InterruptedException {
        HttpRequest googleDocumentRequest = HttpRequest.newBuilder()
                .uri(new URI("https://accounts.google.com/.well-known/openid-configuration"))
                .build();

        HttpResponse<String> googleDocumentResponse = httpClient.send(googleDocumentRequest, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> googleDocumentBody = gson.fromJson(googleDocumentResponse.body(), new TypeToken<Map<String, Object>>() {}.getType());

        String requestURI = String.valueOf(googleDocumentBody.get("token_endpoint"));


        String requestBody = gson.toJson(Map.ofEntries(Map.entry("code", code), Map.entry("client_id", oauth2ClientId),
                Map.entry("client_secret", oauth2ClientSecret), Map.entry("redirect_uri", "http://localhost:8080/security/oauth2/callback"),
                Map.entry("grant_type", "authorization_code")));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(requestURI))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, String> responseBody = gson.fromJson(response.body(), new TypeToken<Map<String, String>>() {}.getType());
        String idToken = responseBody.get("id_token");

        String[] tokenChunks = idToken.split("\\.");

        Base64.Decoder decoder = Base64.getUrlDecoder();

        String tokenPayload = new String(decoder.decode(tokenChunks[1]));
        Map<String, String> payloadMap = gson.fromJson(tokenPayload, new TypeToken<Map<String, String>>() {}.getType());

        return payloadMap.get("email");
    }


}
