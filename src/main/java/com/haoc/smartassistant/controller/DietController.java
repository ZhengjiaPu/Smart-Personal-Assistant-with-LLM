package com.haoc.smartassistant.controller;

import com.haoc.smartassistant.service.DietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/diet")
public class DietController {
    private final DietService dietService;

    @Autowired
    public DietController(DietService dietService) {
        this.dietService = dietService;
    }

    @PostMapping("/plan")
    public ResponseEntity<String> generateMealPlan(@RequestBody Map<String, Object> request) {
        String userId = (String) request.get("userId");
        return ResponseEntity.ok(dietService.createMealPlan(userId));
    }

    @GetMapping("/preferences/{userId}")
    public ResponseEntity<String> getUserPreferences(@PathVariable String userId) {
        return ResponseEntity.ok(dietService.getUserPreferences(userId));
    }

    @PutMapping("/feedback")
    public ResponseEntity<String> updateMealPlan(@RequestBody Map<String, Object> feedback) {
        String userId = (String) feedback.get("userId");
        String feedbackText = (String) feedback.get("feedback");
        return ResponseEntity.ok(dietService.updateMealPlan(userId, feedbackText));
    }
}
