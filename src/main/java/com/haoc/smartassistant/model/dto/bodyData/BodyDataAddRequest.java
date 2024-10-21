package com.haoc.smartassistant.model.dto.bodyData;

import lombok.Data;
import org.apache.poi.hpsf.Decimal;

import java.io.Serializable;
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
    private Decimal height_cm;

    /**
     * weight_kg
     */
    private Decimal weight_kg;

    /**
     * bmi
     */
    private Decimal bmi;

    private static final long serialVersionUID = 1L;
}