package com.wansensoft.mappers.tenant;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wansensoft.entities.tenant.Tenant;
import com.wansensoft.entities.tenant.TenantExample;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TenantMapper extends BaseMapper<Tenant> {
    long countByExample(TenantExample example);

    int deleteByExample(TenantExample example);

    int deleteByPrimaryKey(Long id);

    int insertTenant(Tenant record);

    int insertSelective(Tenant record);

    List<Tenant> getTenantByTenantId(TenantExample example);

    Tenant selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Tenant record, @Param("example") TenantExample example);

    int updateByExample(@Param("record") Tenant record, @Param("example") TenantExample example);

    int updateByPrimaryKeySelective(Tenant record);

    int updateByPrimaryKey(Tenant record);
}