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
package com.wansensoft.api.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansensoft.dto.user.*;
import com.wansensoft.service.user.ISysUserService;
import com.wansensoft.utils.response.Response;
import com.wansensoft.vo.UserInfoVO;
import com.wansensoft.vo.UserListVO;
import com.wansensoft.vo.UserRoleVO;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@RestController
@RequestMapping("/user")
public class SysUserController {

    private final ISysUserService userService;

    public SysUserController(ISysUserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public Response<String> accountRegister(@RequestBody AccountRegisterDTO accountRegisterDto) {
        return userService.accountRegister(accountRegisterDto);
    }

    @PostMapping(value = "login")
    public Response<UserInfoVO> accountLogin(@RequestBody AccountLoginDTO accountLoginDto) {
        return userService.accountLogin(accountLoginDto);
    }

    @PostMapping(value = "mobileLogin")
    public Response<UserInfoVO> accountLogin(@RequestBody MobileLoginDTO mobileLoginDto) {
        return userService.mobileLogin(mobileLoginDto);
    }

    @PostMapping(value = "updatePassword")
    public Response<String> updatePassword(@RequestBody UpdatePasswordDto updatePasswordDto) {
        return userService.updatePassword(updatePasswordDto);
    }

    @GetMapping(value = "info")
    public Response<UserInfoVO> info() {
        return userService.userInfo();
    }

    @GetMapping(value = "perm")
    public Response<List<UserRoleVO>> permission() {
        return userService.userRole();
    }

    @GetMapping(value = "logout")
    public Response<String> logout() {
        return userService.userLogout();
    }

    @PostMapping(value = "list")
    public Response<Page<UserListVO>> list(@RequestBody UserListDTO userListDto) {
        return userService.userList(userListDto);
    }

    @PostMapping(value = "update")
    public Response<String> update(@RequestBody UpdateUserDTO updateUserDTO) {
        return userService.updateUser(updateUserDTO);
    }

}
