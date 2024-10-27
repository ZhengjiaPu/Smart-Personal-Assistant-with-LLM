package com.haoc.smartassistant.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haoc.smartassistant.common.ErrorCode;
import com.haoc.smartassistant.constant.CommonConstant;
import com.haoc.smartassistant.exception.ThrowUtils;
import com.haoc.smartassistant.mapper.SchedulesMapper;
import com.haoc.smartassistant.model.dto.schedules.SchedulesAddRequest;
import com.haoc.smartassistant.model.dto.schedules.SchedulesQueryRequest;
import com.haoc.smartassistant.model.entity.Schedules;

import com.haoc.smartassistant.model.entity.User;
import com.haoc.smartassistant.model.vo.SchedulesVO;
import com.haoc.smartassistant.model.vo.UserVO;
import com.haoc.smartassistant.service.SchedulesService;
import com.haoc.smartassistant.service.UserService;
import com.haoc.smartassistant.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import java.time.LocalDateTime;

/**
 * schedules服务实现
 *
 *
 */
@Service
@Slf4j
public class SchedulesServiceImpl extends ServiceImpl<SchedulesMapper, Schedules> implements SchedulesService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param schedules
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validSchedules(Schedules schedules, boolean add) {
        ThrowUtils.throwIf(schedules == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        String title = schedules.getContent();
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
     * @param schedulesQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Schedules> getQueryWrapper(SchedulesQueryRequest schedulesQueryRequest) {
        QueryWrapper<Schedules> queryWrapper = new QueryWrapper<>();
        if (schedulesQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = schedulesQueryRequest.getId();

        String title = schedulesQueryRequest.getTitle();
        String content = schedulesQueryRequest.getContent();
        String searchText = schedulesQueryRequest.getSearchText();
        String sortField = schedulesQueryRequest.getSortField();
        String sortOrder = schedulesQueryRequest.getSortOrder();
        List<String> tagList = schedulesQueryRequest.getTags();
        Long userId = schedulesQueryRequest.getUserId();
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
     * 获取schedules封装
     *
     * @param schedules
     * @param request
     * @return
     */
    @Override
    public SchedulesVO getSchedulesVO(Schedules schedules, HttpServletRequest request) {
        // 对象转封装类
        SchedulesVO schedulesVO = SchedulesVO.objToVo(schedules);

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = schedules.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        schedulesVO.setUser(userVO);
        // 2. 已登录，获取用户点赞、收藏状态
        long schedulesId = schedules.getId();
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {

        }
        // endregion

        return schedulesVO;
    }

    /**
     * 分页获取schedules封装
     *
     * @param schedulesPage
     * @param request
     * @return
     */
    @Override
    public Page<SchedulesVO> getSchedulesVOPage(Page<Schedules> schedulesPage, HttpServletRequest request) {
        List<Schedules> schedulesList = schedulesPage.getRecords();
        Page<SchedulesVO> schedulesVOPage = new Page<>(schedulesPage.getCurrent(), schedulesPage.getSize(), schedulesPage.getTotal());
        if (CollUtil.isEmpty(schedulesList)) {
            return schedulesVOPage;
        }
        // 对象列表 => 封装对象列表
        List<SchedulesVO> schedulesVOList = schedulesList.stream().map(schedules -> {
            return SchedulesVO.objToVo(schedules);
        }).collect(Collectors.toList());

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = schedulesList.stream().map(Schedules::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 已登录，获取用户点赞、收藏状态
        Map<Long, Boolean> schedulesIdHasThumbMap = new HashMap<>();
        Map<Long, Boolean> schedulesIdHasFavourMap = new HashMap<>();
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
            Set<Long> schedulesIdSet = schedulesList.stream().map(Schedules::getId).collect(Collectors.toSet());
            loginUser = userService.getLoginUser(request);

        }
        // 填充信息
        schedulesVOList.forEach(schedulesVO -> {
            Long userId = schedulesVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            schedulesVO.setUser(userService.getUserVO(user));

        });
        // endregion

        schedulesVOPage.setRecords(schedulesVOList);
        return schedulesVOPage;
    }

    @Override
    public List<Schedules> getSchedulesByUserIdAndDateRange(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        QueryWrapper<Schedules> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId)
                .ge("createTime", startTime)
                .le("createTime", endTime)
                .eq("isDelete", 0);  // 仅获取未删除的记录
        return this.list(queryWrapper);
    }



    @Override
    public boolean addSchedulesFromAI(Schedules schedules) {
        // 创建 Schedules 实体对象
        Schedules schedule = new Schedules();
        // 将DTO字段赋值到实体类
        schedule.setTitle(schedules.getTitle());
        schedule.setContent(schedules.getContent());
        schedule.setUserId(schedules.getUserId());
        schedule.setStartTime(schedules.getStartTime());
        schedule.setEndTime(schedules.getEndTime());
        schedule.setCreateTime(LocalDateTime.now());      // 设置创建时间
        schedule.setUpdateTime(LocalDateTime.now());      // 设置更新时间
        schedule.setIsDelete(0);  // 默认未删除

        // 调用save方法将schedule保存到数据库
        boolean isSaved = this.save(schedule);

        // 返回保存结果
        return isSaved;
    }


        @Override
        public List<Schedules> getSchedulesByUserId(Long userId) {
            QueryWrapper<Schedules> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userId", userId).eq("isDelete", 0); // 获取未删除的记录
            return this.list(queryWrapper);
        }



}
