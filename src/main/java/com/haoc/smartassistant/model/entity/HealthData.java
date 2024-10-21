package com.haoc.smartassistant.model.entity;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Data
public class HealthData {
    private String userId;
    private Double weight;
    private Double height;
    private String allergies;
    private Integer calorieLimit;
    // 其他字段...
}

@Mapper
interface HealthDataMapper extends BaseMapper<HealthData> {
    @Select("SELECT * FROM health_data WHERE user_id = #{userId}")
    HealthData selectByUserId(String userId);
}