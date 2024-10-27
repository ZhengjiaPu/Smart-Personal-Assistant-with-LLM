package com.haoc.smartassistant.model.vo;

import com.haoc.smartassistant.model.entity.HealthData;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * healthData视图
 */
@Data
public class HealthDataVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 心率数据 (存储为逗号分隔的字符串)
     */
    private String heartRate;

    /**
     * 平均心率
     */
    private BigDecimal averageHeartRate;

    /**
     * 步频
     */
    private Integer stepsPerMinute;

    /**
     * 每日睡眠时长（小时）
     */
    private BigDecimal sleepTime;

    /**
     * 深度睡眠百分比
     */
    private BigDecimal deepSleep;

    /**
     * 浅度睡眠百分比
     */
    private BigDecimal lightSleep;

    /**
     * REM 睡眠百分比
     */
    private BigDecimal remSleep;

    /**
     * 卡路里消耗
     */
    private Integer caloriesBurned;

    /**
     * 医生姓名
     */
    private String doctorName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建用户信息
     */
    private UserVO user;

    /**
     * 每日饮水量（升）
     */
    private BigDecimal waterIntake;

    /**
     * 每日热量摄入（卡路里）
     */
    private Integer caloricIntake;

    /**
     * 脂肪燃烧率（百分比）
     */
    private BigDecimal fatBurnRate;

    /**
     * 封装类转对象
     *
     * @param healthDataVO
     * @return
     */
    public static HealthData voToObj(HealthDataVO healthDataVO) {
        if (healthDataVO == null) {
            return null;
        }
        HealthData healthData = new HealthData();
        BeanUtils.copyProperties(healthDataVO, healthData);
        return healthData;
    }

    /**
     * 对象转封装类
     *
     * @param healthData
     * @return
     */
    public static HealthDataVO objToVo(HealthData healthData) {
        if (healthData == null) {
            return null;
        }
        HealthDataVO healthDataVO = new HealthDataVO();
        BeanUtils.copyProperties(healthData, healthDataVO);
        return healthDataVO;
    }
}
