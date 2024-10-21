package com.haoc.smartassistant.aiservices;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

@AiService
public interface StreamingDietPlanningAIService {

    // Method to generate a diet plan based on user body data
    @SystemMessage("""
            You are tasked with generating a personalized diet plan for a user based on their body data.
            The user's data includes their height, weight, and additional preferences.
            Consider these factors and create an optimal meal and diet plan that supports the user's goals (e.g., weight loss, muscle gain, etc.).
            """)
    public Flux<String> generateDietPlan(String userBodyData);

    // Method to adjust an existing diet plan based on user feedback
    @SystemMessage("""
            The user has provided feedback to adjust their diet plan. Please consider the user's request and preferences
            and return an updated meal plan that reflects these adjustments.
            """)
    public Flux<String> adjustDietPlan(String userBodyData, String adjustmentCommand);
}
