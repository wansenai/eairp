/*
 * Copyright 2023-2033 WanSen AI Team, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://opensource.wansenai.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.wansensoft.service.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansensoft.dto.user.*;
import com.wansensoft.entities.user.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.utils.response.Response;
import com.wansensoft.vo.UserInfoVO;
import com.wansensoft.vo.UserListVO;
import com.wansensoft.vo.UserRoleVO;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 */
public interface ISysUserService extends IService<SysUser> {

    Response<String> accountRegister(AccountRegisterDTO accountRegisterDto);

    Response<UserInfoVO> accountLogin(AccountLoginDTO accountLoginDto);

    Response<UserInfoVO> mobileLogin(MobileLoginDTO mobileLoginDto);

    Response<String> updatePassword(UpdatePasswordDto updatePasswordDto);

    Response<UserInfoVO> userInfo();

    UserInfoVO getCurrentUser();

    Long getCurrentUserId();

    Long getCurrentTenantId();

    String getCurrentUserName();

    Response<List<UserRoleVO>> userRole();

    Response<String> userLogout();

    Response<Page<UserListVO>> userList(UserListDTO pageDto);

    Response<List<UserListVO>> userListAll();

    Response<String> updateUser(UpdateUserDTO updateUserDTO);

    Response<String> addOrUpdate(AddOrUpdateUserDTO addOrUpdateUserDTO);

    Response<String> deleteUser(List<Long> ids);

    Response<String> resetPassword(Long id);
}
