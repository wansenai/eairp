package com.wansensoft.mappers.function;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wansensoft.entities.function.FunctionEx;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface FunctionMapperEx extends BaseMapper<FunctionEx> {

    List<FunctionEx> selectByConditionFunction(
            @Param("name") String name,
            @Param("type") String type,
            @Param("offset") Integer offset,
            @Param("rows") Integer rows);

    Long countsByFunction(
            @Param("name") String name,
            @Param("type") String type);

    int batchDeleteFunctionByIds(@Param("updateTime") Date updateTime, @Param("updater") Long updater, @Param("ids") String ids[]);
}