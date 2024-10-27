package com.haoc.smartassistant.aiservices;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface HealthPredictionAIService {

    // Method to generate a comprehensive health report in four sections based on user health data
    @SystemMessage("""
            You are tasked with generating a comprehensive health report based on the user's health data. 
            The report should follow this structured format, divided into four distinct parts:
             
            ### Fitness Prediction (about 30 words)
            - **Steps:** Analyze the user's average daily steps and describe its impact on cardiovascular and musculoskeletal health.
            - **Heart Rate:** Assess the user's heart rate in terms of aerobic capacity and potential for fitness improvements.
            - **Calories Burned:** Explain what the user's calorie burn suggests about their physical activity level and metabolic rate.

            ### Fitness Plan (about 30 words)
            - **Step Goal:** Provide a recommended step target for daily activity to promote cardiovascular health.
            - **Heart Rate Goal:** Suggest a heart rate zone or range for physical activities to maximize aerobic benefits.
            - **Activity Suggestions:** Recommend types of physical activities (e.g., walking, jogging, strength training) to support fitness and wellness.

            ### Nutrition Prediction (about 30 words)
            - **Water Intake:** Assess if the user's water intake is adequate and its effect on hydration and health.
            - **Caloric Intake:** Analyze the user's caloric intake in relation to their fitness goals and physical activity level.
            - **Fat Burn Rate:** Describe what the user's fat burn rate suggests about their metabolic health and energy expenditure.

            ### Nutrition Plan (about 30 words)
            - **Water Intake Goal:** Recommend a daily water intake goal to ensure adequate hydration.
            - **Caloric Intake Goal:** Suggest an appropriate daily caloric intake based on the user’s goals (e.g., weight maintenance, weight loss).
            - **Dietary Recommendations:** Provide general dietary advice, including macronutrient balance and tips for achieving health goals.
            
            """)
    public String generateHealthPredictionAndPlan(String userHealthData);
}
