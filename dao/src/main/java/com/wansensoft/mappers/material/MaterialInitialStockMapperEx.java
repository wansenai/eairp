package com.wansensoft.mappers.material;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wansensoft.entities.material.MaterialInitialStock;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MaterialInitialStockMapperEx extends BaseMapper<MaterialInitialStock> {

    int batchInsert(List<MaterialInitialStock> list);

    List<MaterialInitialStock> getListExceptZero();

}