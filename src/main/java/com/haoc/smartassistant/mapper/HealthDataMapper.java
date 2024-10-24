package com.haoc.smartassistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haoc.smartassistant.model.entity.HealthData;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ZhengjiaPu
 * @description 针对表【health_data(health_data)】的数据库操作Mapper
 * @createDate 2024-10-23
 * @Entity com.haoc.smartassistant.model.entity.HealthData
 */

@Mapper
public interface HealthDataMapper extends BaseMapper<HealthData> {

}
