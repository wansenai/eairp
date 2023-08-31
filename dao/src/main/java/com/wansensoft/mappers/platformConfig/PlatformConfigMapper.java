package com.wansensoft.mappers.platformConfig;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wansensoft.entities.platformConfig.PlatformConfig;
import com.wansensoft.entities.platformConfig.PlatformConfigExample;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PlatformConfigMapper extends BaseMapper<PlatformConfig> {
    long countByExample(PlatformConfigExample example);

    int deleteByExample(PlatformConfigExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PlatformConfig record);

    int insertSelective(PlatformConfig record);

    List<PlatformConfig> selectByExample(PlatformConfigExample example);

    PlatformConfig selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") PlatformConfig record, @Param("example") PlatformConfigExample example);

    int updateByExample(@Param("record") PlatformConfig record, @Param("example") PlatformConfigExample example);

    int updateByPrimaryKeySelective(PlatformConfig record);

    int updateByPrimaryKey(PlatformConfig record);
}