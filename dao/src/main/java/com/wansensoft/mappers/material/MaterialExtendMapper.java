package com.wansensoft.mappers.material;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wansensoft.entities.material.MaterialExtend;
import com.wansensoft.entities.material.MaterialExtendExample;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MaterialExtendMapper extends BaseMapper<MaterialExtend> {
    long countByExample(MaterialExtendExample example);

    int deleteByExample(MaterialExtendExample example);

    int deleteByPrimaryKey(Long id);

    int insertMaterialExtend(MaterialExtend record);

    int insertSelective(MaterialExtend record);

    List<MaterialExtend> selectByExample(MaterialExtendExample example);

    MaterialExtend selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") MaterialExtend record, @Param("example") MaterialExtendExample example);

    int updateByExample(@Param("record") MaterialExtend record, @Param("example") MaterialExtendExample example);

    int updateByPrimaryKeySelective(MaterialExtend record);

    int updateByPrimaryKey(MaterialExtend record);
}