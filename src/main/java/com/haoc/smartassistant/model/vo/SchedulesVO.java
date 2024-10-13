package com.haoc.smartassistant.model.vo;

import cn.hutool.json.JSONUtil;
import com.haoc.smartassistant.model.entity.Schedules;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * schedules视图
 *
 *
 */
@Data
public class SchedulesVO implements Serializable {

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
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 标签列表
     */
    private List<String> tagList;

    /**
     * 创建用户信息
     */
    private UserVO user;

    /**
     * 封装类转对象
     *
     * @param schedulesVO
     * @return
     */
    public static Schedules voToObj(SchedulesVO schedulesVO) {
        if (schedulesVO == null) {
            return null;
        }
        Schedules schedules = new Schedules();
        BeanUtils.copyProperties(schedulesVO, schedules);

        return schedules;
    }

    /**
     * 对象转封装类
     *
     * @param schedules
     * @return
     */
    public static SchedulesVO objToVo(Schedules schedules) {
        if (schedules == null) {
            return null;
        }
        SchedulesVO schedulesVO = new SchedulesVO();
        BeanUtils.copyProperties(schedules, schedulesVO);
        return schedulesVO;
    }
}
