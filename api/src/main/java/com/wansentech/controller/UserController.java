/*
 * Copyright 2023 wansentech.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wansentech.controller;

import com.wansentech.Response;
import com.wansentech.api.dto.UserRegisterDto;
import com.wansentech.enums.CodeEnum;
import com.wansentech.service.bo.UserRegisterBo;
import com.wansentech.user.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Response<String> UserRegister(@RequestBody UserRegisterDto userRegisterDto){
        var userRegisterBo = UserRegisterBo.builder()
                .name(userRegisterDto.getUserName())
                .userName(userRegisterDto.getUserName())
                .password(userRegisterDto.getPassword())
                .email(userRegisterDto.getEmail())
                .phoneNumber(userRegisterDto.getPhoneNumber()).build();

        return userService.addUser(userRegisterBo);
    }
}
