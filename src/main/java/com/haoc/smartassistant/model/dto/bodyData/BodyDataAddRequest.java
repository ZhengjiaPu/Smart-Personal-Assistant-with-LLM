package com.haoc.smartassistant.model.dto.bodyData;

import lombok.Data;
import org.apache.poi.hpsf.Decimal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 创建bodyData请求
 *
 *
 */
@Data
public class BodyDataAddRequest implements Serializable {

    /**
     * height_cm
     */
    private Integer height_cm;

    /**
     * weight_kg
     */
    private Integer weight_kg;


    private static final long serialVersionUID = 1L;
}