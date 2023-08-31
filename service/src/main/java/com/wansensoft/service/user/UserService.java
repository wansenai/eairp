package com.wansensoft.service.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.role.Role;
import com.wansensoft.entities.user.User;
import com.wansensoft.entities.user.UserEx;
import com.wansensoft.vo.TreeNodeEx;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {

    User getUser(long id);

    List<User> getUserListByIds(String ids);

    List<User> getUser();

    List<UserEx> select(String userName, String loginName, int offset, int rows);

    Long countUser(String userName, String loginName);

    int insertUser(JSONObject obj, HttpServletRequest request);

    int updateUser(JSONObject obj, HttpServletRequest request);

    int updateUserByObj(User user);

    int resetPwd(String md5Pwd, Long id);

    int deleteUser(Long id, HttpServletRequest request);

    int batchDeleteUser(String ids, HttpServletRequest request);

    int batDeleteUser(String ids);

    Map<String, Object> login(User userParam, HttpServletRequest request);

    int validateUser(String loginName, String password);

    User getUserByLoginName(String loginName);

    int checkIsNameExist(Long id, String name);

    User getCurrentUser();

    Long getIdByLoginName(String loginName);

    void addUserAndOrgUserRel(UserEx ue, HttpServletRequest request);

    UserEx addUser(UserEx ue);

    UserEx registerUser(UserEx ue, Integer manageRoleId, HttpServletRequest request);

    void updateUserTenant(User user);

    void updateUserAndOrgUserRel(UserEx ue, HttpServletRequest request);

    UserEx updateUser(UserEx ue);

    void checkLoginName(UserEx userEx);

    List<User> getUserListByloginName(String loginName);

    List<TreeNodeEx> getOrganizationUserTree();

    Role getRoleTypeByUserId(long userId);

    Long getUserId(HttpServletRequest request);

    JSONArray getBtnStrArrById(Long userId);

    int batchSetStatus(Byte status, String ids, HttpServletRequest request);

    User getUserByWeixinCode(String weixinCode);

    int weixinBind(String loginName, String password, String weixinCode);
}
