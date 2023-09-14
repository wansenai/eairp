package com.wansensoft.service.user;

import com.wansensoft.dto.user.AccountLoginDto;
import com.wansensoft.dto.user.AccountRegisterDto;
import com.wansensoft.entities.user.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.utils.response.Response;
import com.wansensoft.vo.UserInfoVo;
import com.wansensoft.vo.UserRoleVo;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
public interface ISysUserService extends IService<SysUser> {

    Response<String> accountRegister(AccountRegisterDto accountRegisterDto);

    Response<UserInfoVo> accountLogin(AccountLoginDto accountLoginDto);

    Response<UserInfoVo> userInfo();

    UserInfoVo getCurrentUser();

    String getCurrentUserId();

    String getCurrentUserName();

    Response<List<UserRoleVo>> userRole();

    Response<String> userLogout();
}
