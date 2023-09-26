package com.wansensoft.mappers.platformConfig;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wansensoft.entities.platformConfig.PlatformConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlatformConfigMapperEx extends BaseMapper<PlatformConfig> {

    List<PlatformConfig> selectByConditionPlatformConfig(
            @Param("platformKey") String platformKey,
            @Param("offset") Integer offset,
            @Param("rows") Integer rows);

    Long countsByPlatformConfig(
            @Param("platformKey") String platformKey);

}