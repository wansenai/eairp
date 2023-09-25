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
package com.wansensoft.utils.constants;

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
