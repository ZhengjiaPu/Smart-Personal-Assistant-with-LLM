package com.haoc.smartassistant.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * report
 * @TableName schedules
 */
@TableName(value ="schedules")
@Setter
@Getter
@Data
public class Schedules implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * content
     */
    private String content;

    /**
     * title
     */
    private String title;
    /**
     * userId
     */
    private Long userId;

    /**
     * createTime
     */
    private LocalDateTime createTime;

    /**
     * updateTime
     */
    private LocalDateTime updateTime;

    /**
     * startTime
     */
    private LocalDateTime startTime;

    /**
     * endTime
     */
    private LocalDateTime endTime;

    /**
     * isDelete
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}