package com.haoc.smartassistant.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.haoc.smartassistant.model.dto.schedules.SchedulesAddRequest;
import com.haoc.smartassistant.model.dto.schedules.SchedulesQueryRequest;
import com.haoc.smartassistant.model.entity.Schedules;
import com.haoc.smartassistant.model.vo.SchedulesVO;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;

/**
 * schedules服务
 *
 */
public interface SchedulesService extends IService<Schedules> {

    /**
     * 校验数据
     *
     * @param schedules
     * @param add 对创建的数据进行校验
     */
    void validSchedules(Schedules schedules, boolean add);

    /**
     * 获取查询条件
     *
     * @param schedulesQueryRequest
     * @return
     */
    QueryWrapper<Schedules> getQueryWrapper(SchedulesQueryRequest schedulesQueryRequest);
    
    /**
     * 获取schedules封装
     *
     * @param schedules
     * @param request
     * @return
     */
    SchedulesVO getSchedulesVO(Schedules schedules, HttpServletRequest request);

    /**
     * 分页获取schedules封装
     *
     * @param schedulesPage
     * @param request
     * @return
     */
    Page<SchedulesVO> getSchedulesVOPage(Page<Schedules> schedulesPage, HttpServletRequest request);

    /**
     * 根据用户ID和时间范围获取日程列表
     *
     * @param userId
     * @param startTime
     * @param endTime
     * @return
     */
    List<Schedules> getSchedulesByUserIdAndDateRange(Long userId, LocalDateTime startTime, LocalDateTime endTime);
    /**
     * 添加由AI生成的日程数据
     *
     * @param schedules
     * @return
     */
    boolean addSchedulesFromAI(Schedules schedules);

    /**
     * 根据用户ID获取用户的所有日程
     *
     * @param userId 用户ID
     * @return 用户的日程列表
     */
    List<Schedules> getSchedulesByUserId(Long userId);

}

