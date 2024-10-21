package com.haoc.smartassistant.controller;


import com.haoc.smartassistant.service.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/ai")
public class AIController {

    private final OpenAIService openAIService;

    @Autowired
    public AIController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/chat")
    public String chatWithAI(@RequestBody Map<String, String> request) {
        String prompt = request.get("prompt");
        return openAIService.getAIResponse(prompt);
    }
}

