package com.haoc.smartassistant.model.dto.healthData;

import com.haoc.smartassistant.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 查询healthData请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HealthDataQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 心率数据
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
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除标志
     */
    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}
