package com.wansensoft.material;

import com.wansensoft.entities.material.MaterialInitialStock;
import com.wansensoft.entities.material.MaterialInitialStockExample;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MaterialInitialStockMapper {
    long countByExample(MaterialInitialStockExample example);

    int deleteByExample(MaterialInitialStockExample example);

    int deleteByPrimaryKey(Long id);

    int insert(MaterialInitialStock record);

    int insertSelective(MaterialInitialStock record);

    List<MaterialInitialStock> selectByExample(MaterialInitialStockExample example);

    MaterialInitialStock selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") MaterialInitialStock record, @Param("example") MaterialInitialStockExample example);

    int updateByExample(@Param("record") MaterialInitialStock record, @Param("example") MaterialInitialStockExample example);

    int updateByPrimaryKeySelective(MaterialInitialStock record);

    int updateByPrimaryKey(MaterialInitialStock record);
}