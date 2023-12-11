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
package com.wansenai.utils.constants;

public interface SecurityConstants {

    /**
     * 验证码缓存前缀
     */
    String REGISTER_VERIFY_CODE_CACHE_PREFIX = "AUTH:VERIFY_CODE:REGISTER:";

    String LOGIN_VERIFY_CODE_CACHE_PREFIX = "AUTH:VERIFY_CODE:LOGIN:";

    String UPDATE_PASSWORD_VERIFY_CODE_CACHE_PREFIX = "AUTH:VERIFY_CODE:UPDATE_PASSWORD:";


    String UPDATE_PHONE_VERIFY_CODE_CACHE_PREFIX = "AUTH:VERIFY_CODE:UPDATE_PHONE:";

    String EMAIL_VERIFY_CODE_CACHE_PREFIX = "AUTH:VERIFY_CODE:EMAIL:";

    String EMAIL_RESET_VERIFY_CODE_CACHE_PREFIX = "AUTH:VERIFY_CODE:EMAIL_RESET:";

    /**
     * 用户权限集合缓存前缀
     */
    String USER_PERMS_CACHE_PREFIX = "AUTH:USER_PERMS:";

    /**
     * 黑名单Token缓存前缀
     */
    String BLACK_TOKEN_CACHE_PREFIX = "AUTH:BLACK_TOKEN:";
}
