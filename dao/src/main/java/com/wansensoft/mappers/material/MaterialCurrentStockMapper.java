package com.wansensoft.mappers.material;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wansensoft.entities.material.MaterialCurrentStock;
import com.wansensoft.entities.material.MaterialCurrentStockExample;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MaterialCurrentStockMapper extends BaseMapper<MaterialCurrentStock> {
    long countByExample(MaterialCurrentStockExample example);

    int deleteByExample(MaterialCurrentStockExample example);

    int deleteByPrimaryKey(Long id);

    int insertMaterialCurrentStock(MaterialCurrentStock record);

    int insertSelective(MaterialCurrentStock record);

    List<MaterialCurrentStock> selectByExample(MaterialCurrentStockExample example);

    MaterialCurrentStock selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") MaterialCurrentStock record, @Param("example") MaterialCurrentStockExample example);

    int updateByExample(@Param("record") MaterialCurrentStock record, @Param("example") MaterialCurrentStockExample example);

    int updateByPrimaryKeySelective(MaterialCurrentStock record);

    int updateByPrimaryKey(MaterialCurrentStock record);
}