package com.haoc.smartassistant.service;

import com.haoc.smartassistant.model.entity.HealthData;
import com.haoc.smartassistant.model.vo.HealthDataVO;

import java.util.List;
import java.util.Map;

public interface HealthDataService {

    List<Integer> getHeartRate(Long userId);

    double getSleepTime(Long userId);

    int getStepsPerMinute(Long userId);

    int getCaloriesBurned(Long userId);

    Map<String, Integer> getSleepQuality(Long userId);

    // 新增方法
    HealthData getByUserId(Long userId);  // 通过 userId 获取 HealthData 实体对象

    HealthDataVO getHealthDataVO(HealthData healthData);  // 将 HealthData 转换为 HealthDataVO
}
