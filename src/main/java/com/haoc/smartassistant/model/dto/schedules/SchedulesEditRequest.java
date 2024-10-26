package com.haoc.smartassistant.model.dto.schedules;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 编辑schedules请求
 */
@Data
public class SchedulesEditRequest implements Serializable {

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
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    private static final long serialVersionUID = 1L;
}
