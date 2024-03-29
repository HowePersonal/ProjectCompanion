package com.example.projectwaifu.ai;

import com.example.projectwaifu.util.PremadeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class GeminiAPI {

    @Value("${gemini.key}")
    private String API_KEY;

    @Autowired
    private GeminiProxy geminiProxy;

    PremadeResponse premadeResponse = new PremadeResponse();

    public String getGeminiResponse(String input) {
        Map<String, Object> requestBody = new HashMap<>();

        Map<String, Object> partMap = new HashMap<>();
        Map<String, String> textMap = new HashMap<>();
        textMap.put("text", input+ ". Talk like an anime character");
        partMap.put("parts", textMap);


        requestBody.put("contents", Arrays.asList(partMap));
        requestBody.put("generationConfig", GeminiProxy.maxOutputTokens);

        Map<String, Object> response = geminiProxy.retrieveResponse(API_KEY, GeminiProxy.geminiHeader, requestBody);

        Map<String, Object> promptFeedback = (Map<String, Object>) response.get("promptFeedback");
        if (promptFeedback.get("blockReason") != null) {
            return premadeResponse.explicitWarning();
        }

        try {
            return parseGeminiResponse(response);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String parseGeminiResponse(Map<String, Object> geminiResponse) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.convertValue(geminiResponse, JsonNode.class);
        String textResponse = jsonNode.at("/candidates/0/content/parts/0/text").asText();

        return textResponse;
    }

}
