package com.haoc.smartassistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haoc.smartassistant.model.entity.BodyData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* @author haochen
* @description 针对表【body_data(body_data)】的数据库操作Mapper
* @createDate 2024-10-21 13:57:30
* @Entity generator12.domain.BodyData
*/

public interface BodyDataMapper extends BaseMapper<BodyData> {
    @Select("SELECT * FROM body_data WHERE userId = #{userId} AND isDelete = 0")
    BodyData getBodyDataByUserId(Long userId);
}




