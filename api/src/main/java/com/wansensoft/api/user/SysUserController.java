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

import com.wansensoft.dto.user.AccountLoginDto;
import com.wansensoft.dto.user.AccountRegisterDto;
import com.wansensoft.service.user.ISysUserService;
import com.wansensoft.utils.response.Response;
import com.wansensoft.utils.constants.ApiVersionConstants;
import com.wansensoft.vo.UserInfoVo;
import com.wansensoft.vo.UserRoleVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
    public Response<String> accountRegister(@RequestBody AccountRegisterDto accountRegisterDto) {
        return userService.accountRegister(accountRegisterDto);
    }

    @PostMapping(value = ApiVersionConstants.API_METHOD_VERSION_V2 + "login")
    public Response<UserInfoVo> accountLogin(@RequestBody AccountLoginDto accountLoginDto) {
        return userService.accountLogin(accountLoginDto);
    }

    @GetMapping(value = "info")
    public Response<UserInfoVo> info() {
        return userService.userInfo();
    }

    @GetMapping(value = "perm")
    public Response<List<UserRoleVo>> permission() {
        return userService.userRole();
    }

    @GetMapping(value = "logout")
    public Response<String> logout() {
        return userService.userLogout();
    }
}
