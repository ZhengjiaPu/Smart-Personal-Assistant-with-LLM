package com.haoc.smartassistant.model.dto.schedules;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新schedules请求
 *
 *
 */
@Data
public class SchedulesUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;


    private static final long serialVersionUID = 1L;
}