package com.haoc.smartassistant.controller;

import com.haoc.smartassistant.aiservices.HealthReportAIService;
import com.haoc.smartassistant.model.entity.HealthData;
import com.haoc.smartassistant.service.HealthDataService;
import com.haoc.smartassistant.service.UserService;
import com.haoc.smartassistant.common.BaseResponse;
import com.haoc.smartassistant.common.ErrorCode;
import com.haoc.smartassistant.common.ResultUtils;
import com.haoc.smartassistant.exception.ThrowUtils;
import com.haoc.smartassistant.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.haoc.smartassistant.model.vo.HealthDataVO;


import jakarta.servlet.http.HttpServletRequest;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

/**
 * HealthDataController handles API requests for health data (e.g., heart rate, sleep time).
 */
@RestController
@RequestMapping("/healthData")
@Slf4j
public class HealthDataController {

    @Autowired
    private HealthDataService healthDataService;

    @Autowired
    private UserService userService;

    /**
     * Get health data for the user by userId, including heart rate and sleep time.
     * @param userId The ID of the user whose health data is being retrieved.
     * @return BaseResponse with health data.
     */
    @GetMapping("/get")
    public BaseResponse<HealthDataVO> getHealthDataById(@RequestParam(value = "userId", required = false) Long userId) {
        // 检查 userId 是否为空或者为负数
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR);

        // 获取用户的健康数据
        HealthData healthData = healthDataService.getByUserId(userId);
        ThrowUtils.throwIf(healthData == null, ErrorCode.NOT_FOUND_ERROR);

        // 返回封装的健康数据对象
        return ResultUtils.success(healthDataService.getHealthDataVO(healthData));
    }
    @Autowired
    private HealthReportAIService HealthReportAIService;
    /**
     * Generate health report based on the user's health data
     * @param userId The ID of the user for whom to generate the health report.
     * @return Stream of Health Report as Strings.
     */
    @GetMapping("/generate-health-report/{userId}")
    public String generateHealthReport(@PathVariable Long userId) {
        ThrowUtils.throwIf(userId <= 0, ErrorCode.PARAMS_ERROR);

        HealthData healthData = healthDataService.getByUserId(userId);
        ThrowUtils.throwIf(healthData == null, ErrorCode.NOT_FOUND_ERROR);

        log.info("healthData ---------- " + healthData);

        return HealthReportAIService.generateHealthReport(healthData.toString());
    }
}