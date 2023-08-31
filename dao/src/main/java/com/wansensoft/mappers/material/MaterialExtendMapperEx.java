package com.wansensoft.mappers.material;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wansensoft.entities.material.MaterialExtend;
import com.wansensoft.vo.MaterialExtendVo4List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MaterialExtendMapperEx extends BaseMapper<MaterialExtend> {

    int batchDeleteMaterialExtendByIds(@Param("ids") String ids[]);

    List<MaterialExtendVo4List> getDetailList(
            @Param("materialId") Long materialId);

    Long getMaxTimeByTenantAndTime(
            @Param("tenantId") Long tenantId,
            @Param("lastTime") Long lastTime,
            @Param("syncNum") Long syncNum);

    List<MaterialExtend> getListByMId(@Param("ids") Long ids[]);

    int batchDeleteMaterialExtendByMIds(@Param("ids") String ids[]);

    int specialUpdatePrice(MaterialExtend record);
}