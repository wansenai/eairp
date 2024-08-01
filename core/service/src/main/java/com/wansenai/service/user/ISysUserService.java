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
package com.wansenai.service.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.dto.user.*;
import com.wansenai.utils.response.Response;
import com.wansenai.entities.user.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansenai.vo.UserInfoVO;
import com.wansenai.vo.UserListVO;
import com.wansenai.vo.UserRoleVO;
import org.springframework.web.multipart.MultipartFile;

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

    Response<UserInfoVO> emailLogin(EmailLoginDTO emailLoginDTO);

    Response<String> updatePassword(UpdatePasswordDto updatePasswordDto);

    Response<String> updatePasswordByEmail(UpdatePasswordByEmailDto updatePasswordByEmailDto);

    Response<String> resetPassword(ResetPasswordDTO resetPasswordDto);

    Response<UserInfoVO> userInfo();

    UserInfoVO getCurrentUser();

    Long getCurrentUserId();

    Long getCurrentTenantId();

    String getCurrentUserName();

    String getUserSystemLanguage(Long userId);

    Response<List<UserRoleVO>> userRole();

    Response<String> userLogout();

    Response<Page<UserListVO>> userList(UserListDTO pageDto);

    Response<List<UserListVO>> userListAll();

    Response<String> updateUser(UpdateUserDTO updateUserDTO);

    Response<String> updateStatus(UpdateUserDTO updateUserDTO);

    Response<String> uploadAvatar(MultipartFile file, Long userId, String name);

    Response<String> addOrUpdate(AddOrUpdateUserDTO addOrUpdateUserDTO);

    Response<String> deleteUser(List<Long> ids);

    Response<String> resetPassword(Long id);

    Response<List<UserInfoVO>> operator();

    Response<String> resetPhoneNumber(ResetPhoneDTO resetPhoneDTO);

    Response<String> resetEmail(ResetEmailDTO resetEmailDTO);
}
