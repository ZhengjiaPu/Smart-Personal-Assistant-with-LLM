package com.haoc.smartassistant.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haoc.smartassistant.common.ErrorCode;
import com.haoc.smartassistant.constant.CommonConstant;
import com.haoc.smartassistant.exception.ThrowUtils;
import com.haoc.smartassistant.mapper.BodyDataMapper;
import com.haoc.smartassistant.model.dto.bodyData.BodyDataQueryRequest;
import com.haoc.smartassistant.model.entity.BodyData;

import com.haoc.smartassistant.model.entity.User;
import com.haoc.smartassistant.model.vo.BodyDataVO;
import com.haoc.smartassistant.model.vo.UserVO;
import com.haoc.smartassistant.service.BodyDataService;
import com.haoc.smartassistant.service.UserService;
import com.haoc.smartassistant.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hpsf.Decimal;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * bodyData服务实现
 *
 *
 */
@Service
@Slf4j
public class BodyDataServiceImpl extends ServiceImpl<BodyDataMapper, BodyData> implements BodyDataService {

    @Resource
    @Lazy
    private UserService userService;


    /**
     * 获取查询条件
     *
     * @param bodyDataQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<BodyData> getQueryWrapper(BodyDataQueryRequest bodyDataQueryRequest) {
        QueryWrapper<BodyData> queryWrapper = new QueryWrapper<>();
        if (bodyDataQueryRequest == null) {
            return queryWrapper;
        }
        // 从请求对象中取出参数
        Long id = bodyDataQueryRequest.getId();
        Long userId = bodyDataQueryRequest.getUserId();
        Integer height_cm = bodyDataQueryRequest.getHeight_cm();
        Integer weight_kg = bodyDataQueryRequest.getWeight_kg();
        Double bmi = bodyDataQueryRequest.getBmi();
        Date createTime = bodyDataQueryRequest.getCreateTime();
        Date updateTime = bodyDataQueryRequest.getUpdateTime();
        Integer isDelete = bodyDataQueryRequest.getIsDelete();


        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.ne(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(height_cm), "height_cm", height_cm);
        queryWrapper.eq(ObjectUtils.isNotEmpty(weight_kg), "weight_kg", weight_kg);
        queryWrapper.eq(ObjectUtils.isNotEmpty(bmi), "bmi", bmi);
        queryWrapper.eq(ObjectUtils.isNotEmpty(isDelete), "isDelete", isDelete);
        // 时间范围查询（假设需要进行日期范围过滤）
        if (createTime != null) {
            queryWrapper.ge("createTime", createTime);
        }
        if (updateTime != null) {
            queryWrapper.le("updateTime", updateTime);
        }

        // 排序规则
        String sortField = bodyDataQueryRequest.getSortField();  // 假设有这个字段
        String sortOrder = bodyDataQueryRequest.getSortOrder();  // 假设有这个字段
        if (SqlUtils.validSortField(sortField)) {
            queryWrapper.orderBy(true, "ASC".equalsIgnoreCase(sortOrder), sortField);
        }
        return queryWrapper;
    }

    /**
     * 获取bodyData封装
     *
     * @param bodyData
     * @param request
     * @return
     */
    @Override
    public BodyDataVO getBodyDataVO(BodyData bodyData, HttpServletRequest request) {
        // 对象转封装类
        BodyDataVO bodyDataVO = BodyDataVO.objToVo(bodyData);

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = bodyData.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        bodyDataVO.setUser(userVO);
        // 2. 已登录，获取用户点赞、收藏状态
        long bodyDataId = bodyData.getId();
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {

        }
        // endregion

        return bodyDataVO;
    }
    /**
     * 根据用户id获取bodyData
     *
     * @param  userId
     * @return
     */
    public BodyData getBodyDataByUserId(Long userId) {
        QueryWrapper<BodyData> queryWrapper = new QueryWrapper<>();
        System.out.println(queryWrapper);
        queryWrapper.eq("userId", userId);  // 假设数据库中字段名为 userId
        return this.baseMapper.selectOne(queryWrapper);
    }

    /**
     * 分页获取bodyData封装
     *
     * @param bodyDataPage
     * @param request
     * @return
     */
    @Override
    public Page<BodyDataVO> getBodyDataVOPage(Page<BodyData> bodyDataPage, HttpServletRequest request) {
        List<BodyData> bodyDataList = bodyDataPage.getRecords();
        Page<BodyDataVO> bodyDataVOPage = new Page<>(bodyDataPage.getCurrent(), bodyDataPage.getSize(), bodyDataPage.getTotal());
        if (CollUtil.isEmpty(bodyDataList)) {
            return bodyDataVOPage;
        }
        // 对象列表 => 封装对象列表
        List<BodyDataVO> bodyDataVOList = bodyDataList.stream().map(bodyData -> {
            return BodyDataVO.objToVo(bodyData);
        }).collect(Collectors.toList());

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = bodyDataList.stream().map(BodyData::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 已登录

        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
            Set<Long> bodyDataIdSet = bodyDataList.stream().map(BodyData::getId).collect(Collectors.toSet());
            loginUser = userService.getLoginUser(request);

        }
        // 填充信息
        bodyDataVOList.forEach(bodyDataVO -> {
            Long userId = bodyDataVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            bodyDataVO.setUser(userService.getUserVO(user));

        });


        // endregion

        bodyDataVOPage.setRecords(bodyDataVOList);
        return bodyDataVOPage;
    }

}
