package com.haoc.smartassistant.model.vo;

import cn.hutool.json.JSONUtil;
import com.haoc.smartassistant.model.entity.BodyData;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * bodyData视图
 *
 *
 */
@Data
public class BodyDataVO implements Serializable {

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
     * 创建用户信息
     */
    private UserVO user;

    /**
     * 封装类转对象
     *
     * @param bodyDataVO
     * @return
     */
    public static BodyData voToObj(BodyDataVO bodyDataVO) {
        if (bodyDataVO == null) {
            return null;
        }
        BodyData bodyData = new BodyData();
        BeanUtils.copyProperties(bodyDataVO, bodyData);

        return bodyData;
    }

    /**
     * 对象转封装类
     *
     * @param bodyData
     * @return
     */
    public static BodyDataVO objToVo(BodyData bodyData) {
        if (bodyData == null) {
            return null;
        }
        BodyDataVO bodyDataVO = new BodyDataVO();
        BeanUtils.copyProperties(bodyData, bodyDataVO);
        return bodyDataVO;
    }
}
