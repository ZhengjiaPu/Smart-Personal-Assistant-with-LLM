package com.haoc.smartassistant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.haoc.smartassistant.annotation.AuthCheck;
import com.haoc.smartassistant.common.BaseResponse;
import com.haoc.smartassistant.common.DeleteRequest;
import com.haoc.smartassistant.common.ErrorCode;
import com.haoc.smartassistant.common.ResultUtils;
import com.haoc.smartassistant.constant.UserConstant;
import com.haoc.smartassistant.exception.BusinessException;
import com.haoc.smartassistant.exception.ThrowUtils;
import com.haoc.smartassistant.model.dto.schedules.SchedulesAddRequest;
import com.haoc.smartassistant.model.dto.schedules.SchedulesEditRequest;
import com.haoc.smartassistant.model.dto.schedules.SchedulesQueryRequest;
import com.haoc.smartassistant.model.dto.schedules.SchedulesUpdateRequest;
import com.haoc.smartassistant.model.entity.Schedules;
import com.haoc.smartassistant.model.entity.User;
import com.haoc.smartassistant.model.vo.SchedulesVO;
import com.haoc.smartassistant.service.SchedulesService;
import com.haoc.smartassistant.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

/**
 * schedules接口
 *
 */
@RestController
@RequestMapping("/schedules")
@Slf4j
public class SchedulesController {

    @Resource
    private SchedulesService schedulesService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建schedules
     *
     * @param schedulesAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addSchedules(@RequestBody SchedulesAddRequest schedulesAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(schedulesAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        Schedules schedules = new Schedules();
        BeanUtils.copyProperties(schedulesAddRequest, schedules);
        // 数据校验
        schedulesService.validSchedules(schedules, true);
        // todo 填充默认值
        User loginUser = userService.getLoginUser(request);
        schedules.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = schedulesService.save(schedules);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newSchedulesId = schedules.getId();
        return ResultUtils.success(newSchedulesId);
    }

    /**
     * 删除schedules
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteSchedules(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Schedules oldSchedules = schedulesService.getById(id);
        ThrowUtils.throwIf(oldSchedules == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldSchedules.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = schedulesService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新schedules（仅管理员可用）
     *
     * @param schedulesUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateSchedules(@RequestBody SchedulesUpdateRequest schedulesUpdateRequest) {
        if (schedulesUpdateRequest == null || schedulesUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Schedules schedules = new Schedules();
        BeanUtils.copyProperties(schedulesUpdateRequest, schedules);
        // 数据校验
        schedulesService.validSchedules(schedules, false);
        // 判断是否存在
        long id = schedulesUpdateRequest.getId();
        Schedules oldSchedules = schedulesService.getById(id);
        ThrowUtils.throwIf(oldSchedules == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = schedulesService.updateById(schedules);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取schedules（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<SchedulesVO> getSchedulesVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Schedules schedules = schedulesService.getById(id);
        ThrowUtils.throwIf(schedules == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(schedulesService.getSchedulesVO(schedules, request));
    }

    /**
     * 分页获取schedules列表（仅管理员可用）
     *
     * @param schedulesQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Schedules>> listSchedulesByPage(@RequestBody SchedulesQueryRequest schedulesQueryRequest) {
        long current = schedulesQueryRequest.getCurrent();
        long size = schedulesQueryRequest.getPageSize();
        // 查询数据库
        Page<Schedules> schedulesPage = schedulesService.page(new Page<>(current, size),
                schedulesService.getQueryWrapper(schedulesQueryRequest));
        return ResultUtils.success(schedulesPage);
    }

    /**
     * 分页获取schedules列表（封装类）
     *
     * @param schedulesQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<SchedulesVO>> listSchedulesVOByPage(@RequestBody SchedulesQueryRequest schedulesQueryRequest,
                                                               HttpServletRequest request) {
        long current = schedulesQueryRequest.getCurrent();
        long size = schedulesQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Schedules> schedulesPage = schedulesService.page(new Page<>(current, size),
                schedulesService.getQueryWrapper(schedulesQueryRequest));
        // 获取封装类
        return ResultUtils.success(schedulesService.getSchedulesVOPage(schedulesPage, request));
    }

    /**
     * 分页获取当前登录用户创建的schedules列表
     *
     * @param schedulesQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<SchedulesVO>> listMySchedulesVOByPage(@RequestBody SchedulesQueryRequest schedulesQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(schedulesQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        schedulesQueryRequest.setUserId(loginUser.getId());
        long current = schedulesQueryRequest.getCurrent();
        long size = schedulesQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Schedules> schedulesPage = schedulesService.page(new Page<>(current, size),
                schedulesService.getQueryWrapper(schedulesQueryRequest));
        // 获取封装类
        return ResultUtils.success(schedulesService.getSchedulesVOPage(schedulesPage, request));
    }

    /**
     * 编辑schedules（给用户使用）
     *
     * @param schedulesEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editSchedules(@RequestBody SchedulesEditRequest schedulesEditRequest, HttpServletRequest request) {
        if (schedulesEditRequest == null || schedulesEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Schedules schedules = new Schedules();
        BeanUtils.copyProperties(schedulesEditRequest, schedules);
        // 数据校验
        schedulesService.validSchedules(schedules, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = schedulesEditRequest.getId();
        Schedules oldSchedules = schedulesService.getById(id);
        ThrowUtils.throwIf(oldSchedules == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldSchedules.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = schedulesService.updateById(schedules);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
