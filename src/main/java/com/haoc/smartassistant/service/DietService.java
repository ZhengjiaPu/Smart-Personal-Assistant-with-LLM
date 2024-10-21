package com.haoc.smartassistant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DietService {
    private final OpenAIService openAIService; // 与OpenAI交互
    private final UserService userService; // 获取用户数据的服务

    @Autowired
    public DietService(OpenAIService openAIService, UserService userService) {
        this.openAIService = openAIService;
        this.userService = userService;
    }

    public String createMealPlan(String userId) {
        // 获取用户的健康数据和饮食偏好
        Map<String, Object> healthData = userService.getHealthData(userId);
        Map<String, Object> preferences = userService.getPreferences(userId);

        // 构建请求体发送给OpenAI
        String prompt = buildMealPlanPrompt(healthData, preferences);
        return openAIService.getChatCompletion(prompt);
    }

    public String getUserPreferences(String userId) {
        return userService.getPreferences(userId).toString();
    }

    public String updateMealPlan(String userId, String feedback) {
        // 根据用户反馈更新膳食计划
        return "Meal plan for user " + userId + " has been updated based on feedback: " + feedback;
    }

    private String buildMealPlanPrompt(Map<String, Object> healthData, Map<String, Object> preferences) {
        // 根据用户数据构建适合OpenAI的请求体
        return "Create a personalized meal plan for a user with the following data: " +
                "Health: " + healthData.toString() + ", Preferences: " + preferences.toString();
    }
}
