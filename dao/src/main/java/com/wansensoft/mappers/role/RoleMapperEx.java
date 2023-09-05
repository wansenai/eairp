package com.wansensoft.mappers.role;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wansensoft.entities.role.Role;
import com.wansensoft.entities.role.RoleEx;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface RoleMapperEx extends BaseMapper<Role> {

    List<RoleEx> selectByConditionRole(
            @Param("name") String name,
            @Param("description") String description,
            @Param("offset") Integer offset,
            @Param("rows") Integer rows);

    Long countsByRole(
            @Param("name") String name,
            @Param("description") String description);

    int batchDeleteRoleByIds(@Param("updateTime") Date updateTime, @Param("updater") Long updater, @Param("ids") String ids[]);


    @InterceptorIgnore(tenantLine = "true")
    Role getRoleWithoutTenant(
            @Param("roleId") Long roleId);
}