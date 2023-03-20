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
package com.wansentech.user.impl;

import com.wansentech.Response;
import com.wansentech.SnowflakeIdUtil;
import com.wansentech.aggregateservice.UserAggregate;
import com.wansentech.dao.po.UserRegisterPo;
import com.wansentech.enums.CodeEnum;
import com.wansentech.mappers.UserMapper;
import com.wansentech.service.bo.UserRegisterBo;
import com.wansentech.user.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * user interface (Service layer)
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserAggregate userAggregate;

    public UserServiceImpl(UserMapper userMapper, UserAggregate userAggregate) {
        this.userMapper = userMapper;
        this.userAggregate = userAggregate;
    }

    @Override
    public Response<String> addUser(UserRegisterBo userRegisterBo) {
        // check exist user
        boolean existUserName = userAggregate.existUser(userRegisterBo.getUserName());
        if (existUserName) {
            return Response.responseMsg(CodeEnum.USER_EXISTS);
        }

        UserRegisterPo userRegisterPo = UserRegisterPo.builder()
                .id(SnowflakeIdUtil.nextId())
                .name(userRegisterBo.getName())
                .userName(userRegisterBo.getUserName())
                .password(userRegisterBo.getPassword())
                .email(userRegisterBo.getEmail())
                .phoneNumber(userRegisterBo.getPhoneNumber()).
                status(0).
                remark(userRegisterBo.getRemark()).
                tenantId(userRegisterBo.getTenantId()).build();

        boolean result = userAggregate.insertUser(userRegisterPo);
        if (result) {
            return Response.responseMsg(CodeEnum.REGISTER_SUCCESS);
        }

        return Response.fail();
    }
}
