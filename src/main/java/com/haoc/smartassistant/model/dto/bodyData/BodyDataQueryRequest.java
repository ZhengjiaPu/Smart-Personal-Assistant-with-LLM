package com.haoc.smartassistant.model.dto.bodyData;

import com.haoc.smartassistant.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 查询bodyData请求
 *
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BodyDataQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * userId
     */
    private Long userId;

    /**
     * createTime
     */
    private Date createTime;
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
     * updateTime
     */
    private Date updateTime;

    /**
     * isDelete
     */
    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}