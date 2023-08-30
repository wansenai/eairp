package com.wansensoft.erp.datasource.mappers;

import com.wansensoft.erp.datasource.entities.MaterialCurrentStock;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MaterialCurrentStockMapperEx {

    int batchInsert(List<MaterialCurrentStock> list);

    List<MaterialCurrentStock> getCurrentStockMapByIdList(
            @Param("materialIdList") List<Long> materialIdList);
}