package com.haoc.smartassistant.model.dto.bodyData;

import lombok.Data;
import org.apache.poi.hpsf.Decimal;

import java.io.Serializable;

/**
 * 创建bodyData请求
 *
 *
 */
@Data
public class BodyDataAiRequest implements Serializable {

    /**
     * userID
     */
    private Long userID;

    /**
     * adjustmentCommand
     */
    private String adjustmentCommand;

    private static final long serialVersionUID = 1L;
}