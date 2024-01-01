package com.example.projectwaifu.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


@FeignClient(name = "gemini-client", url="https://generativelanguage.googleapis.com/v1beta/models")
public interface GeminiProxy {
    String API_KEY = "key";

    Map<String, Integer> maxOutputTokens = Map.of(
            "maxOutputTokens", 50
    );

    Map<String, String> geminiHeader = Map.of(
            "Content-Type", "application/json"
    );

    @PostMapping("/gemini-pro:generateContent")
    Map<String, Object> retrieveResponse(
            @RequestParam(name = API_KEY) String apiKey,
            @RequestHeader("header") Map<String, String> header,
            @RequestBody Map<String, Object> requestBody);
}
