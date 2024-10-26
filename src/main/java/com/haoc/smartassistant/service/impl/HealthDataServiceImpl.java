package com.haoc.smartassistant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haoc.smartassistant.mapper.HealthDataMapper;
import com.haoc.smartassistant.model.entity.HealthData;
import com.haoc.smartassistant.model.vo.HealthDataVO;
import com.haoc.smartassistant.service.HealthDataService;
import com.haoc.smartassistant.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HealthData 服务实现类
 */
@Service
@Slf4j
public class HealthDataServiceImpl extends ServiceImpl<HealthDataMapper, HealthData> implements HealthDataService {

    @Resource
    private HealthDataMapper healthDataMapper;

    @Resource
    private UserService userService;

    @Override
    public HealthData getByUserId(Long userId) {
        // 查询数据库获取用户的健康数据
        QueryWrapper<HealthData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        HealthData healthData = this.healthDataMapper.selectOne(queryWrapper);

        if (healthData == null) {
            throw new RuntimeException("No health data found for this user");
        }
        return healthData;
    }

    @Override
    public HealthDataVO getHealthDataVO(HealthData healthData) {
        if (healthData == null) {
            return null;
        }

        // 将 HealthData 转换为 HealthDataVO
        HealthDataVO healthDataVO = new HealthDataVO();
        BeanUtils.copyProperties(healthData, healthDataVO);

        // 直接从 healthData 中获取 doctorName，无需 HttpServletRequest
        healthDataVO.setDoctorName(healthData.getDoctorName());

        return healthDataVO;
    }



    // 其他方法保持不变
    @Override
    public List<Integer> getHeartRate(Long userId) {
        QueryWrapper<HealthData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        HealthData healthData = this.healthDataMapper.selectOne(queryWrapper);

        if (healthData == null) {
            throw new RuntimeException("No health data found for this user");
        }

        String heartRateString = healthData.getHeartRate();
        List<String> heartRateList = Arrays.asList(heartRateString.split(","));
        return heartRateList.stream().map(Integer::valueOf).toList();
    }

    @Override
    public double getSleepTime(Long userId) {
        QueryWrapper<HealthData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        HealthData healthData = healthDataMapper.selectOne(queryWrapper);

        if (healthData == null) {
            throw new RuntimeException("No health data found for this user");
        }

        return healthData.getSleepTime().doubleValue();
    }

    @Override
    public int getStepsPerMinute(Long userId) {
        QueryWrapper<HealthData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        HealthData healthData = healthDataMapper.selectOne(queryWrapper);

        if (healthData == null) {
            throw new RuntimeException("No health data found for this user");
        }

        return healthData.getStepsPerMinute();
    }

    @Override
    public int getCaloriesBurned(Long userId) {
        QueryWrapper<HealthData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        HealthData healthData = healthDataMapper.selectOne(queryWrapper);

        if (healthData == null) {
            throw new RuntimeException("No health data found for this user");
        }

        return healthData.getCaloriesBurned();
    }

    @Override
    public Map<String, Integer> getSleepQuality(Long userId) {
        QueryWrapper<HealthData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        HealthData healthData = healthDataMapper.selectOne(queryWrapper);

        if (healthData == null) {
            throw new RuntimeException("No health data found for this user");
        }

        Map<String, Integer> sleepQuality = new HashMap<>();
        sleepQuality.put("Deep Sleep", healthData.getDeepSleep().intValue());
        sleepQuality.put("Light Sleep", healthData.getLightSleep().intValue());
        sleepQuality.put("REM Sleep", healthData.getRemSleep().intValue());

        return sleepQuality;
    }
}
