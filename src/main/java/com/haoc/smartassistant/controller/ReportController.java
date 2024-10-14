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
import com.haoc.smartassistant.model.dto.report.ReportAddRequest;
import com.haoc.smartassistant.model.dto.report.ReportEditRequest;
import com.haoc.smartassistant.model.dto.report.ReportQueryRequest;
import com.haoc.smartassistant.model.dto.report.ReportUpdateRequest;
import com.haoc.smartassistant.model.entity.Report;
import com.haoc.smartassistant.model.entity.User;
import com.haoc.smartassistant.model.vo.ReportVO;
import com.haoc.smartassistant.service.ReportService;
import com.haoc.smartassistant.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * report接口
 *
 */
@RestController
@RequestMapping("/report")
@Slf4j
public class ReportController {

    @Resource
    private ReportService reportService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建report
     *
     * @param reportAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addReport(@RequestBody ReportAddRequest reportAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(reportAddRequest == null, ErrorCode.PARAMS_ERROR);
        // 在此处将实体类和 DTO 进行转换
        Report report = new Report();
        BeanUtils.copyProperties(reportAddRequest, report);
        // 数据校验
        reportService.validReport(report, true);
        // 填充默认值
        User loginUser = userService.getLoginUser(request);
        report.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = reportService.save(report);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newReportId = report.getId();
        return ResultUtils.success(newReportId);
    }

    /**
     * 删除report
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteReport(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Report oldReport = reportService.getById(id);
        ThrowUtils.throwIf(oldReport == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldReport.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = reportService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新report（仅管理员可用）
     *
     * @param reportUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateReport(@RequestBody ReportUpdateRequest reportUpdateRequest) {
        if (reportUpdateRequest == null || reportUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Report report = new Report();
        BeanUtils.copyProperties(reportUpdateRequest, report);
        // 数据校验
        reportService.validReport(report, false);
        // 判断是否存在
        long id = reportUpdateRequest.getId();
        Report oldReport = reportService.getById(id);
        ThrowUtils.throwIf(oldReport == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = reportService.updateById(report);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取report（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<ReportVO> getReportVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Report report = reportService.getById(id);
        ThrowUtils.throwIf(report == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(reportService.getReportVO(report, request));
    }

    /**
     * 分页获取report列表（仅管理员可用）
     *
     * @param reportQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Report>> listReportByPage(@RequestBody ReportQueryRequest reportQueryRequest) {
        long current = reportQueryRequest.getCurrent();
        long size = reportQueryRequest.getPageSize();
        // 查询数据库
        Page<Report> reportPage = reportService.page(new Page<>(current, size),
                reportService.getQueryWrapper(reportQueryRequest));
        return ResultUtils.success(reportPage);
    }

    /**
     * 分页获取report列表（封装类）
     *
     * @param reportQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<ReportVO>> listReportVOByPage(@RequestBody ReportQueryRequest reportQueryRequest,
                                                               HttpServletRequest request) {
        long current = reportQueryRequest.getCurrent();
        long size = reportQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Report> reportPage = reportService.page(new Page<>(current, size),
                reportService.getQueryWrapper(reportQueryRequest));
        // 获取封装类
        return ResultUtils.success(reportService.getReportVOPage(reportPage, request));
    }

    /**
     * 分页获取当前登录用户创建的report列表
     *
     * @param reportQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<ReportVO>> listMyReportVOByPage(@RequestBody ReportQueryRequest reportQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(reportQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        reportQueryRequest.setUserId(loginUser.getId());
        long current = reportQueryRequest.getCurrent();
        long size = reportQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Report> reportPage = reportService.page(new Page<>(current, size),
                reportService.getQueryWrapper(reportQueryRequest));
        // 获取封装类
        return ResultUtils.success(reportService.getReportVOPage(reportPage, request));
    }

    /**
     * 编辑report（给用户使用）
     *
     * @param reportEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editReport(@RequestBody ReportEditRequest reportEditRequest, HttpServletRequest request) {
        if (reportEditRequest == null || reportEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Report report = new Report();
        BeanUtils.copyProperties(reportEditRequest, report);
        // 数据校验
        reportService.validReport(report, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = reportEditRequest.getId();
        Report oldReport = reportService.getById(id);
        ThrowUtils.throwIf(oldReport == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldReport.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = reportService.updateById(report);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
