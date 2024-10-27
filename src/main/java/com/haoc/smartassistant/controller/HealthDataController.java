package com.haoc.smartassistant.controller;

import com.haoc.smartassistant.aiservices.HealthReportAIService;
import com.haoc.smartassistant.aiservices.HealthPredictionAIService;
import com.haoc.smartassistant.model.entity.HealthData;
import com.haoc.smartassistant.service.HealthDataService;
import com.haoc.smartassistant.common.BaseResponse;
import com.haoc.smartassistant.common.ErrorCode;
import com.haoc.smartassistant.common.ResultUtils;
import com.haoc.smartassistant.exception.ThrowUtils;
import com.haoc.smartassistant.model.vo.HealthDataVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/healthData")
@Slf4j
public class HealthDataController {

    @Autowired
    private HealthDataService healthDataService;

    @Autowired
    private HealthReportAIService healthReportAIService;

    @Autowired
    private HealthPredictionAIService healthPredictionAIService;

    /**
     * Get health data for the user by userId, including heart rate and sleep time.
     * @param userId The ID of the user whose health data is being retrieved.
     * @return BaseResponse with health data.
     */
    @GetMapping("/get")
    public BaseResponse<HealthDataVO> getHealthDataById(@RequestParam(value = "userId", required = true) Long userId) {
        // Validate that userId is not null and is greater than 0
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR);

        // Fetch the health data by userId
        HealthData healthData = healthDataService.getByUserId(userId);
        // Check if the health data is found, otherwise throw an error
        ThrowUtils.throwIf(healthData == null, ErrorCode.NOT_FOUND_ERROR);

        // Convert the health data entity to a VO (View Object) and return
        return ResultUtils.success(healthDataService.getHealthDataVO(healthData));
    }

    /**
     * Generate health report based on the user's health data
     * @param userId The ID of the user for whom to generate the health report.
     * @return Health Report as a BaseResponse<String>.
     */
    @GetMapping("/generate-health-report/{userId}")
    public BaseResponse<String> generateHealthReport(@PathVariable Long userId) {
        // Validate that userId is greater than 0
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR);

        // Fetch the health data by userId
        HealthData healthData = healthDataService.getByUserId(userId);
        // Check if the health data is found, otherwise throw an error
        ThrowUtils.throwIf(healthData == null, ErrorCode.NOT_FOUND_ERROR);

        log.info("healthData ---------- " + healthData);

        // Generate health report using AI service
        String healthReport = healthReportAIService.generateHealthReport(healthData.toString());

        // Return health report wrapped in BaseResponse
        return ResultUtils.success(healthReport);
    }

    /**
     * Generate comprehensive health prediction and plan (including Fitness and Nutrition) based on health data.
     * @param userId The ID of the user for whom to generate the health prediction and plan.
     * @return Combined Fitness and Nutrition prediction and plan as a BaseResponse<String>.
     */
    @GetMapping("/generate-health-prediction-plan/{userId}")
    public BaseResponse<String> generateHealthPredictionAndPlan(@PathVariable Long userId) {
        // Validate that userId is greater than 0
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR);

        // Fetch the health data by userId
        HealthData healthData = healthDataService.getByUserId(userId);
        // Check if the health data is found, otherwise throw an error
        ThrowUtils.throwIf(healthData == null, ErrorCode.NOT_FOUND_ERROR);

        // Prepare user health data in JSON format for AI service
        String userHealthData = String.format(
                "{\"steps\":\"%s\",\"heartRate\":\"%s\",\"caloriesBurned\":\"%s\",\"waterIntake\":\"%s\",\"caloricIntake\":\"%s\",\"fatBurnRate\":\"%s\"}",
                healthData.getStepsPerDay() != null ? healthData.getStepsPerDay().toString() : "0",
                healthData.getAverageHeartRate() != null ? healthData.getAverageHeartRate().toString() : "0",
                healthData.getCaloriesBurned() != null ? healthData.getCaloriesBurned().toString() : "0",
                healthData.getWaterIntake() != null ? healthData.getWaterIntake().toString() : "0",
                healthData.getCaloricIntake() != null ? healthData.getCaloricIntake().toString() : "0",
                healthData.getFatBurnRate() != null ? healthData.getFatBurnRate().toString() : "0"
        );

        // Generate health prediction and plan using AI service
        String healthPlan = healthPredictionAIService.generateHealthPredictionAndPlan(userHealthData);

        // Return health prediction and plan wrapped in BaseResponse
        return ResultUtils.success(healthPlan);
    }
}
