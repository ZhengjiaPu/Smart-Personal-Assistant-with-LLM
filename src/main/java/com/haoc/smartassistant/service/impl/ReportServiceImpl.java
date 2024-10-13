package com.haoc.smartassistant.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haoc.smartassistant.common.ErrorCode;
import com.haoc.smartassistant.constant.CommonConstant;
import com.haoc.smartassistant.exception.ThrowUtils;
import com.haoc.smartassistant.mapper.ReportMapper;
import com.haoc.smartassistant.model.dto.report.ReportQueryRequest;
import com.haoc.smartassistant.model.entity.Report;

import com.haoc.smartassistant.model.entity.User;
import com.haoc.smartassistant.model.vo.ReportVO;
import com.haoc.smartassistant.model.vo.UserVO;
import com.haoc.smartassistant.service.ReportService;
import com.haoc.smartassistant.service.UserService;
import com.haoc.smartassistant.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * report服务实现
 *
 *
 */
@Service
@Slf4j
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param report
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validReport(Report report, boolean add) {
        ThrowUtils.throwIf(report == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        String title = report.getTitle();
        // 创建数据时，参数不能为空
        if (add) {
            // todo 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(title), ErrorCode.PARAMS_ERROR);
        }
        // 修改数据时，有参数则校验
        // todo 补充校验规则
        if (StringUtils.isNotBlank(title)) {
            ThrowUtils.throwIf(title.length() > 80, ErrorCode.PARAMS_ERROR, "标题过长");
        }
    }

    /**
     * 获取查询条件
     *
     * @param reportQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Report> getQueryWrapper(ReportQueryRequest reportQueryRequest) {
        QueryWrapper<Report> queryWrapper = new QueryWrapper<>();
        if (reportQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = reportQueryRequest.getId();
        String title = reportQueryRequest.getTitle();
        String content = reportQueryRequest.getContent();
        String sortField = reportQueryRequest.getSortField();
        String sortOrder = reportQueryRequest.getSortOrder();
        List<String> tagList = reportQueryRequest.getTags();
        Long userId = reportQueryRequest.getUserId();
        // todo 补充需要的查询条件

        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        // JSON 数组查询
        if (CollUtil.isNotEmpty(tagList)) {
            for (String tag : tagList) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取report封装
     *
     * @param report
     * @param request
     * @return
     */
    @Override
    public ReportVO getReportVO(Report report, HttpServletRequest request) {
        // 对象转封装类
        ReportVO reportVO = ReportVO.objToVo(report);

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = report.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        reportVO.setUser(userVO);
        // 2. 已登录，获取用户点赞、收藏状态
        long reportId = report.getId();
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {

        }
        // endregion

        return reportVO;
    }

    /**
     * 分页获取report封装
     *
     * @param reportPage
     * @param request
     * @return
     */
    @Override
    public Page<ReportVO> getReportVOPage(Page<Report> reportPage, HttpServletRequest request) {
        List<Report> reportList = reportPage.getRecords();
        Page<ReportVO> reportVOPage = new Page<>(reportPage.getCurrent(), reportPage.getSize(), reportPage.getTotal());
        if (CollUtil.isEmpty(reportList)) {
            return reportVOPage;
        }
        // 对象列表 => 封装对象列表
        List<ReportVO> reportVOList = reportList.stream().map(report -> {
            return ReportVO.objToVo(report);
        }).collect(Collectors.toList());

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = reportList.stream().map(Report::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 已登录，获取用户点赞、收藏状态
        Map<Long, Boolean> reportIdHasThumbMap = new HashMap<>();
        Map<Long, Boolean> reportIdHasFavourMap = new HashMap<>();
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
            Set<Long> reportIdSet = reportList.stream().map(Report::getId).collect(Collectors.toSet());
            loginUser = userService.getLoginUser(request);

        }
        // 填充信息
        reportVOList.forEach(reportVO -> {
            Long userId = reportVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            reportVO.setUser(userService.getUserVO(user));

        });
        // endregion

        reportVOPage.setRecords(reportVOList);
        return reportVOPage;
    }

}
