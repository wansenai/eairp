package com.wansensoft.erp.datasource.mappers;

import com.wansensoft.erp.datasource.entities.MaterialInitialStock;

import java.util.List;

public interface MaterialInitialStockMapperEx {

    int batchInsert(List<MaterialInitialStock> list);

    List<MaterialInitialStock> getListExceptZero();

}