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
package com.wansentech.aggregateservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansentech.aggregateservice.UserAggregate;
import com.wansentech.entity.User;
import com.wansentech.mappers.UserMapper;
import org.springframework.stereotype.Service;

/**
 *
 * @author James Zow
 * @create 2023/3/5
 */
@Service
public class UserAggregateImpl extends ServiceImpl<UserMapper, User> implements UserAggregate{

}
