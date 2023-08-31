package com.wansensoft.mappers.material;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wansensoft.entities.material.MaterialAttribute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MaterialAttributeMapperEx extends BaseMapper<MaterialAttribute> {

    List<MaterialAttribute> selectByConditionMaterialAttribute(
            @Param("attributeName") String attributeName,
            @Param("offset") Integer offset,
            @Param("rows") Integer rows);

    Long countsByMaterialAttribute(
            @Param("attributeField") String attributeField);

    int batchDeleteMaterialAttributeByIds(
            @Param("ids") String ids[]);
}