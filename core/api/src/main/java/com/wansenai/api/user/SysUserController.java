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
package com.wansenai.api.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.dto.user.*;
import com.wansenai.dto.user.*;
import com.wansenai.service.user.ISysUserService;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.UserInfoVO;
import com.wansenai.vo.UserListVO;
import com.wansenai.vo.UserRoleVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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

    @GetMapping(value = "operator")
    public Response<List<UserInfoVO>> operator() {
        return userService.operator();
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

    @GetMapping(value = "listAll")
    public Response<List<UserListVO>> listAll() {
        return userService.userListAll();
    }

    @PostMapping(value = "update")
    public Response<String> update(@RequestBody UpdateUserDTO updateUserDTO) {
        return userService.updateUser(updateUserDTO);
    }

    @PostMapping("uploadAvatar")
    public Response<String> uploadAvatar(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId, @RequestParam("name") String name) {
        return userService.uploadAvatar(file, userId, name);
    }

    @PostMapping(value = "addOrUpdate")
    public Response<String> addOrUpdate(@RequestBody AddOrUpdateUserDTO addOrUpdateUserDTO) {
        return userService.addOrUpdate(addOrUpdateUserDTO);
    }

    @PostMapping(value = "delete")
    public Response<String> deleteUser(@RequestParam(value = "ids") List<Long> ids) {
        return userService.deleteUser(ids);
    }

    @PostMapping(value = "resetPassword")
    public Response<String> resetPassword(@RequestParam(value = "id") Long id) {
        return userService.resetPassword(id);
    }
}
