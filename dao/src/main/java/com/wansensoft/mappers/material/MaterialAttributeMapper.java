package com.wansensoft.mappers.material;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wansensoft.entities.material.MaterialAttribute;
import com.wansensoft.entities.material.MaterialAttributeExample;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MaterialAttributeMapper extends BaseMapper<MaterialAttribute> {
    long countByExample(MaterialAttributeExample example);

    int deleteByExample(MaterialAttributeExample example);

    int deleteByPrimaryKey(Long id);

    int insertMaterialAttribute(MaterialAttribute record);

    int insertSelective(MaterialAttribute record);

    List<MaterialAttribute> selectByExample(MaterialAttributeExample example);

    MaterialAttribute selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") MaterialAttribute record, @Param("example") MaterialAttributeExample example);

    int updateByExample(@Param("record") MaterialAttribute record, @Param("example") MaterialAttributeExample example);

    int updateByPrimaryKeySelective(MaterialAttribute record);

    int updateByPrimaryKey(MaterialAttribute record);
}