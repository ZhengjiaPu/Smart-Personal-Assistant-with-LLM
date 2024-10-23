package com.haoc.smartassistant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.haoc.smartassistant.aiservices.DietPlanningAIService;
import com.haoc.smartassistant.aiservices.RecommendedMusicAIService;
import com.haoc.smartassistant.aiservices.StreamingDietPlanningAIService;
import com.haoc.smartassistant.annotation.AuthCheck;
import com.haoc.smartassistant.common.BaseResponse;
import com.haoc.smartassistant.common.DeleteRequest;
import com.haoc.smartassistant.common.ErrorCode;
import com.haoc.smartassistant.common.ResultUtils;
import com.haoc.smartassistant.constant.UserConstant;
import com.haoc.smartassistant.exception.BusinessException;
import com.haoc.smartassistant.exception.ThrowUtils;
import com.haoc.smartassistant.model.dto.bodyData.BodyDataAddRequest;
import com.haoc.smartassistant.model.dto.bodyData.BodyDataAiRequest;
import com.haoc.smartassistant.model.dto.bodyData.BodyDataQueryRequest;
import com.haoc.smartassistant.model.dto.bodyData.BodyDataUpdateRequest;
import com.haoc.smartassistant.model.entity.BodyData;
import com.haoc.smartassistant.model.entity.User;
import com.haoc.smartassistant.model.vo.BodyDataVO;
import com.haoc.smartassistant.service.BodyDataService;
import com.haoc.smartassistant.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

/**
 * bodyData接口
 *
 */
@RestController
@RequestMapping("/bodyData")
@Slf4j
public class BodyDataController {

    @Resource
    private BodyDataService bodyDataService;

    @Resource
    private UserService userService;

    @Autowired
    private DietPlanningAIService dietPlanningAIService;

    @Autowired
    private StreamingDietPlanningAIService streamingDietPlanningAIService;
    @Autowired
    private RecommendedMusicAIService recommendedMusicAIService;

    // region 增删改查

    /**
     * 创建bodyData
     *
     * @param bodyDataAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addBodyData(@RequestBody BodyDataAddRequest bodyDataAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(bodyDataAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        BodyData bodyData = new BodyData();
        BeanUtils.copyProperties(bodyDataAddRequest, bodyData);
        // 数据校验
        bodyDataService.validBodyData(bodyData, true);
        // todo 填充默认值
        User loginUser = userService.getLoginUser(request);
        bodyData.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = bodyDataService.save(bodyData);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newBodyDataId = bodyData.getId();
        return ResultUtils.success(newBodyDataId);
    }

    /**
     * 删除bodyData
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteBodyData(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        BodyData oldBodyData = bodyDataService.getById(id);
        ThrowUtils.throwIf(oldBodyData == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldBodyData.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = bodyDataService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新bodyData（仅本人或管理员）
     *
     * @param bodyDataUpdateRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateBodyData(@RequestBody BodyDataUpdateRequest bodyDataUpdateRequest, HttpServletRequest request) {
        if (bodyDataUpdateRequest == null || bodyDataUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        // todo 在此处将实体类和 DTO 进行转换
        BodyData bodyData = new BodyData();
        BeanUtils.copyProperties(bodyDataUpdateRequest, bodyData);
        // 数据校验
        bodyDataService.validBodyData(bodyData, false);
        // 判断是否存在
        long id = bodyDataUpdateRequest.getId();
        BodyData oldBodyData = bodyDataService.getById(id);
        ThrowUtils.throwIf(oldBodyData == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可更新
        if (!oldBodyData.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = bodyDataService.updateById(bodyData);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取bodyData（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<BodyDataVO> getBodyDataVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        BodyData bodyData = bodyDataService.getById(id);
        ThrowUtils.throwIf(bodyData == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(bodyDataService.getBodyDataVO(bodyData, request));
    }

    /**
     * 分页获取bodyData列表（仅管理员可用）
     *
     * @param bodyDataQueryRequest
     * @return
     */
    @PostMapping( "/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<BodyData>> listBodyDataByPage(@RequestBody BodyDataQueryRequest bodyDataQueryRequest) {
        long current = bodyDataQueryRequest.getCurrent();
        long size = bodyDataQueryRequest.getPageSize();
        // 查询数据库
        Page<BodyData> bodyDataPage = bodyDataService.page(new Page<>(current, size),
                bodyDataService.getQueryWrapper(bodyDataQueryRequest));
        return ResultUtils.success(bodyDataPage);
    }

    /**
     * 分页获取bodyData列表（封装类）
     *
     * @param bodyDataQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<BodyDataVO>> listBodyDataVOByPage(@RequestBody BodyDataQueryRequest bodyDataQueryRequest,
                                                               HttpServletRequest request) {
        long current = bodyDataQueryRequest.getCurrent();
        long size = bodyDataQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<BodyData> bodyDataPage = bodyDataService.page(new Page<>(current, size),
                bodyDataService.getQueryWrapper(bodyDataQueryRequest));
        // 获取封装类
        return ResultUtils.success(bodyDataService.getBodyDataVOPage(bodyDataPage, request));
    }

    /**
     * 分页获取当前登录用户创建的bodyData列表
     *
     * @param bodyDataQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<BodyDataVO>> listMyBodyDataVOByPage(@RequestBody BodyDataQueryRequest bodyDataQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(bodyDataQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        bodyDataQueryRequest.setUserId(loginUser.getId());
        long current = bodyDataQueryRequest.getCurrent();
        long size = bodyDataQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<BodyData> bodyDataPage = bodyDataService.page(new Page<>(current, size),
                bodyDataService.getQueryWrapper(bodyDataQueryRequest));
        // 获取封装类
        return ResultUtils.success(bodyDataService.getBodyDataVOPage(bodyDataPage, request));
    }

    // AI Methods for Automatic Meal and Diet Planning
    /**
     * Generate diet plan based on the user's body data
     * @param userId
     * @return Stream of DietPlanVO objects
     */
    @GetMapping("/generate-diet-plan/{userId}")
    public Flux<String> generateDietPlan(@PathVariable Long userId) {
        ThrowUtils.throwIf(userId <= 0, ErrorCode.PARAMS_ERROR);
        BodyData bodyData = bodyDataService.getBodyDataByUserId(userId);
        ThrowUtils.throwIf(bodyData == null, ErrorCode.NOT_FOUND_ERROR);
        System.out.println("bodyData ---------- "+bodyData);
        return streamingDietPlanningAIService.generateDietPlan(bodyData.toString());
    }

    /**
     * Adjust diet plan based on user feedback
     * @param bodyDataAiRequest include user id and adjustmentCommand
     * @return Stream of updated DietPlanVO objects
     */
    @PostMapping("/adjust-diet-plan/")
    public Flux<String> adjustDietPlan(@RequestBody BodyDataAiRequest bodyDataAiRequest) {
        Long userId = bodyDataAiRequest.getUserID();
        String adjustmentCommand = bodyDataAiRequest.getAdjustmentCommand();
        ThrowUtils.throwIf(userId <= 0, ErrorCode.PARAMS_ERROR);
        BodyData bodyData = bodyDataService.getBodyDataByUserId(userId);
        ThrowUtils.throwIf(bodyData == null, ErrorCode.NOT_FOUND_ERROR);
        return streamingDietPlanningAIService.adjustDietPlan(adjustmentCommand);
    }

    // AI Methods for Mood-Based Music Recommendations
    /**
     * get AI Recommended Music based on mood and body data
     * @param userId
     * @return Stream of DietPlanVO objects
     */
    @GetMapping("/recommend/{userId}")
    public String getAIRecommendedMusic(@PathVariable Long userId, @RequestParam String mood) {
        ThrowUtils.throwIf(userId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(mood== null, ErrorCode.PARAMS_ERROR);
        BodyData bodyData = bodyDataService.getBodyDataByUserId(userId);
        ThrowUtils.throwIf(bodyData == null, ErrorCode.NOT_FOUND_ERROR);

        System.out.println("mood    ---------- "+mood);
        return recommendedMusicAIService.generateRecommendedMusic(bodyData.toString(),mood);
    }


    // endregion
}
