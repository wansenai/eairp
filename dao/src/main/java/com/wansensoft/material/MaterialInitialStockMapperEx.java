package com.wansensoft.material;

import com.wansensoft.entities.material.MaterialInitialStock;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MaterialInitialStockMapperEx {

    int batchInsert(List<MaterialInitialStock> list);

    List<MaterialInitialStock> getListExceptZero();

}