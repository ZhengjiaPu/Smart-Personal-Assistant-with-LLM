package com.haoc.smartassistant.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.haoc.smartassistant.model.dto.report.ReportQueryRequest;
import com.haoc.smartassistant.model.entity.Report;
import com.haoc.smartassistant.model.vo.ReportVO;

import javax.servlet.http.HttpServletRequest;

/**
 * report服务
 *
 */
public interface ReportService extends IService<Report> {

    /**
     * 校验数据
     *
     * @param report
     * @param add 对创建的数据进行校验
     */
    void validReport(Report report, boolean add);

    /**
     * 获取查询条件
     *
     * @param reportQueryRequest
     * @return
     */
    QueryWrapper<Report> getQueryWrapper(ReportQueryRequest reportQueryRequest);
    
    /**
     * 获取report封装
     *
     * @param report
     * @param request
     * @return
     */
    ReportVO getReportVO(Report report, HttpServletRequest request);

    /**
     * 分页获取report封装
     *
     * @param reportPage
     * @param request
     * @return
     */
    Page<ReportVO> getReportVOPage(Page<Report> reportPage, HttpServletRequest request);
}
