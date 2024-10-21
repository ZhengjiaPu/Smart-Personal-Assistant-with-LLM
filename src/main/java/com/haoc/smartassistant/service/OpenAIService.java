package com.haoc.smartassistant.service;

import com.haoc.smartassistant.config.OpenAIConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Service
public class OpenAIService {
    private final OpenAIConfig openAIConfig;
    private final RestTemplate restTemplate;

    @Autowired
    public OpenAIService(OpenAIConfig openAIConfig, RestTemplate restTemplate) {
        this.openAIConfig = openAIConfig;
        this.restTemplate = restTemplate;
    }

    public String getChatCompletion(String prompt) {
        String url = openAIConfig.getApiUrl() + "/chat/completions";
        // 设置请求头、请求体等逻辑...
        return sendRequest(url, prompt);
    }

    public String getEmbeddings(String inputText) {
        String url = openAIConfig.getApiUrl() + "/embeddings";
        // 设置请求头、请求体等逻辑...
        return sendRequest(url, inputText);
    }

    public String getFineTuningStatus(String fineTuneId) {
        String url = openAIConfig.getApiUrl() + "/fine_tuning/" + fineTuneId;
        // 设置请求头、请求体等逻辑...
        return sendRequest(url, fineTuneId);
    }

    private String sendRequest(String url, String input) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAIConfig.getApiKey());
        headers.set("Content-Type", "application/json");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("prompt", input);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        return response.getBody();
    }

    public String getAIResponse(String prompt) {
        String apiUrl = openAIConfig.getApiUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAIConfig.getApiKey());
        headers.set("Content-Type", "application/json");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "text-davinci-003");
        requestBody.put("prompt", prompt);
        requestBody.put("max_tokens", 100);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);

        return response.getBody();
    }
}

