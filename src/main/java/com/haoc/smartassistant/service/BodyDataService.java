package com.haoc.smartassistant.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.haoc.smartassistant.model.dto.bodyData.BodyDataQueryRequest;
import com.haoc.smartassistant.model.entity.BodyData;
import com.haoc.smartassistant.model.vo.BodyDataVO;

import jakarta.servlet.http.HttpServletRequest;

/**
 * bodyData服务
 *
 */
public interface BodyDataService extends IService<BodyData> {

    /**
     * 校验数据
     *
     * @param bodyData
     * @param add 对创建的数据进行校验
     */
    void validBodyData(BodyData bodyData, boolean add);

    /**
     * 获取查询条件
     *
     * @param bodyDataQueryRequest
     * @return
     */
    QueryWrapper<BodyData> getQueryWrapper(BodyDataQueryRequest bodyDataQueryRequest);
    
    /**
     * 获取bodyData封装
     *
     * @param bodyData
     * @param request
     * @return
     */
    BodyDataVO getBodyDataVO(BodyData bodyData, HttpServletRequest request);

    /**
     * 分页获取bodyData封装
     *
     * @param bodyDataPage
     * @param request
     * @return
     */
    Page<BodyDataVO> getBodyDataVOPage(Page<BodyData> bodyDataPage, HttpServletRequest request);

    /**
     * 根据用户id获取bodyData
     *
     * @param  userId
     * @return
     */
    BodyData getBodyDataByUserId(Long userId);

}
