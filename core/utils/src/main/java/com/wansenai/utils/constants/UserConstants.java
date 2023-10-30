/*
 * Copyright (C) 2023-2033 WanSen AI Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wansenai.utils.constants;

/**
 * <p>
 *  用户状态常量类
 *  User Status Constants
 * </p>
 */
public interface UserConstants {

    /** 用户状态启用 */
    int USER_STATUS_ENABLE = 0;

    /** 用户状态停用 */
    int USER_STATUS_DISABLE = 1;

    /** 用户状态封禁 */
    int USER_STATUS_BAN = 2;

    /** 默认密码 */
    String DEFAULT_PASSWORD = "123456";
}
