package com.wansensoft.mappers.user;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wansensoft.entities.user.User;
import com.wansensoft.entities.user.UserEx;
import com.wansensoft.vo.TreeNodeEx;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapperEx extends BaseMapper<User> {

    List<UserEx> selectByConditionUser(
            @Param("userName") String userName,
            @Param("loginName") String loginName,
            @Param("offset") Integer offset,
            @Param("rows") Integer rows);

    Long countsByUser(
            @Param("userName") String userName,
            @Param("loginName") String loginName);

    @InterceptorIgnore(tenantLine = "true")
    List<User> getUserListByUserNameOrLoginName(@Param("userName") String userName,
                                                @Param("loginName") String loginName);

    int batDeleteOrUpdateUser(@Param("ids") String ids[], @Param("status") byte status);

    List<TreeNodeEx> getNodeTree();
    List<TreeNodeEx> getNextNodeTree(Map<String, Object> parameterMap);

    @InterceptorIgnore(tenantLine = "true")
    void disableUserByLimit(@Param("tenantId") Long tenantId);

    List<User> getListByOrgaId(
            @Param("id") Long id,
            @Param("orgaId") Long orgaId);

    @InterceptorIgnore(tenantLine = "true")
    User getUserByWeixinOpenId(
            @Param("weixinOpenId") String weixinOpenId);

    @InterceptorIgnore(tenantLine = "true")
    int updateUserWithWeixinOpenId(
            @Param("loginName") String loginName,
            @Param("password") String password,
            @Param("weixinOpenId") String weixinOpenId);
}