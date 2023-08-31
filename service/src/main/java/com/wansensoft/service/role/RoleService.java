package com.wansensoft.service.role;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.role.Role;
import com.wansensoft.entities.role.RoleEx;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.List;

public interface RoleService extends IService<Role> {

    Role getRole(long id);

    List<Role> getRoleListByIds(String ids);

    List<Role> allList();

    List<RoleEx> select(String name, String description, int offset, int rows);

    Long countRole(String name, String description);

    int insertRole(JSONObject obj, HttpServletRequest request);

    int updateRole(JSONObject obj, HttpServletRequest request);

    int deleteRole(Long id, HttpServletRequest request);

    int batchDeleteRole(String ids, HttpServletRequest request);

    List<Role> findUserRole();

    int batchDeleteRoleByIds(String ids);

    Role getRoleWithoutTenant(Long roleId);

    int batchSetStatus(Boolean status, String ids);

    Object parseHomePriceByLimit(BigDecimal price, String type, String priceLimit, String emptyInfo, HttpServletRequest request);

    BigDecimal parseBillPriceByLimit(BigDecimal price, String billCategory, String priceLimit, HttpServletRequest request);

    Object parseMaterialPriceByLimit(BigDecimal price, String type, String emptyInfo, HttpServletRequest request);

    String getCurrentPriceLimit(HttpServletRequest request);
}
