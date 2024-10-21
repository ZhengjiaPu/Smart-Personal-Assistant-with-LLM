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
 * body_data
 * @TableName body_data
 */
@TableName(value ="body_data")
@Data
public class BodyData implements Serializable {
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
     * User height in cm
     */
    private BigDecimal height_cm;

    /**
     * User weight in kg
     */
    private BigDecimal weight_kg;

    /**
     * Calculated BMI
     */
    private BigDecimal bmi;

    /**
     * createTime
     */
    private Date createTime;

    /**
     * updateTime
     */
    private Date updateTime;

    /**
     * isDelete
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
        sb.append(", height_cm=").append(height_cm);
        sb.append(", weight_kg=").append(weight_kg);
        sb.append(", bmi=").append(bmi);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}