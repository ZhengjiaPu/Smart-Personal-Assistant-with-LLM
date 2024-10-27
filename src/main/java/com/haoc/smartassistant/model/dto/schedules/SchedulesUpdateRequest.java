package com.haoc.smartassistant.model.dto.schedules;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 更新schedules请求
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

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    private static final long serialVersionUID = 1L;
}
