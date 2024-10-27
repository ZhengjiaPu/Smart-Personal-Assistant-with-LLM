package com.haoc.smartassistant.mapper;

import com.haoc.smartassistant.model.entity.Schedules;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author haochen
* @description 针对表【schedules(report)】的数据库操作Mapper
* @createDate 2024-10-13 15:27:02
* @Entity com.haoc.smartassistant.model.entity.Schedules
*/

public interface SchedulesMapper extends BaseMapper<Schedules> {
    @Select("SELECT * FROM schedules WHERE userId = #{userId} AND isDelete = 0")
    List<Schedules> getSchedulesByUserId(Long userId);

}




