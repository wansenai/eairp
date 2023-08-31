package com.wansensoft.mappers.material;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wansensoft.entities.material.MaterialProperty;
import com.wansensoft.entities.material.MaterialPropertyExample;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MaterialPropertyMapper extends BaseMapper<MaterialProperty> {
    long countByExample(MaterialPropertyExample example);

    int deleteByExample(MaterialPropertyExample example);

    int deleteByPrimaryKey(Long id);

    int insert(MaterialProperty record);

    int insertSelective(MaterialProperty record);

    List<MaterialProperty> selectByExample(MaterialPropertyExample example);

    MaterialProperty selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") MaterialProperty record, @Param("example") MaterialPropertyExample example);

    int updateByExample(@Param("record") MaterialProperty record, @Param("example") MaterialPropertyExample example);

    int updateByPrimaryKeySelective(MaterialProperty record);

    int updateByPrimaryKey(MaterialProperty record);
}