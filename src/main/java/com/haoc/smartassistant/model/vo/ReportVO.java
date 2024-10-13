package com.haoc.smartassistant.model.vo;

import cn.hutool.json.JSONUtil;
import com.haoc.smartassistant.model.entity.Report;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * report视图
 *
 *
 */
@Data
public class ReportVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 标签列表
     */
    private List<String> tagList;

    /**
     * 创建用户信息
     */
    private UserVO user;

    /**
     * 封装类转对象
     *
     * @param reportVO
     * @return
     */
    public static Report voToObj(ReportVO reportVO) {
        if (reportVO == null) {
            return null;
        }
        Report report = new Report();
        BeanUtils.copyProperties(reportVO, report);
        return report;
    }

    /**
     * 对象转封装类
     *
     * @param report
     * @return
     */
    public static ReportVO objToVo(Report report) {
        if (report == null) {
            return null;
        }
        ReportVO reportVO = new ReportVO();
        BeanUtils.copyProperties(report, reportVO);
        return reportVO;
    }
}
