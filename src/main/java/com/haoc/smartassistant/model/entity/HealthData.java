package com.haoc.smartassistant.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * health_data
 * @TableName health_data
 */
@TableName(value ="health_data")
@Data
public class HealthData implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * userId
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
     * 每日步数
     */
    private Integer stepsPerDay;

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
     * 水摄入量（升/天）
     */
    private BigDecimal waterIntake;

    /**
     * 热量摄入量（calories/day）
     */
    private Integer caloricIntake;

    /**
     * 脂肪燃烧率（%）
     */
    private BigDecimal fatBurnRate;

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
     * 删除标志
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", heartRate=").append(heartRate);
        sb.append(", averageHeartRate=").append(averageHeartRate);
        sb.append(", stepsPerMinute=").append(stepsPerMinute);
        sb.append(", stepsPerDay=").append(stepsPerDay);
        sb.append(", sleepTime=").append(sleepTime);
        sb.append(", deepSleep=").append(deepSleep);
        sb.append(", lightSleep=").append(lightSleep);
        sb.append(", remSleep=").append(remSleep);
        sb.append(", caloriesBurned=").append(caloriesBurned);
        sb.append(", waterIntake=").append(waterIntake);
        sb.append(", caloricIntake=").append(caloricIntake);
        sb.append(", fatBurnRate=").append(fatBurnRate);
        sb.append(", doctorName=").append(doctorName);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
