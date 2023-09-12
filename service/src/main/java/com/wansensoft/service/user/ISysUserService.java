package com.wansensoft.service.user;

import com.wansensoft.dto.login.AccountLoginDto;
import com.wansensoft.entities.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.utils.response.Response;
import com.wansensoft.vo.UserInfoVo;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
public interface ISysUserService extends IService<SysUser> {

    Response<UserInfoVo> accountLogin(AccountLoginDto accountLoginDto);

    Response<UserInfoVo> userInfo();

    UserInfoVo getCurrentUser();
}
