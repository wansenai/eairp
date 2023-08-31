package com.wansensoft.mappers.tenant;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wansensoft.entities.tenant.Tenant;
import com.wansensoft.entities.tenant.TenantEx;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TenantMapperEx extends BaseMapper<Tenant> {

    List<TenantEx> selectByConditionTenant(
            @Param("loginName") String loginName,
            @Param("type") String type,
            @Param("enabled") String enabled,
            @Param("remark") String remark,
            @Param("offset") Integer offset,
            @Param("rows") Integer rows);

    Long countsByTenant(
            @Param("loginName") String loginName,
            @Param("type") String type,
            @Param("enabled") String enabled,
            @Param("remark") String remark);
}