package com.haoc.smartassistant.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.haoc.smartassistant.model.dto.schedules.SchedulesQueryRequest;
import com.haoc.smartassistant.model.entity.Schedules;
import com.haoc.smartassistant.model.vo.SchedulesVO;

import javax.servlet.http.HttpServletRequest;

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
}
