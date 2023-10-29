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

/**
 * 通用常量接口
 */
public interface CommonConstants {

    // 逻辑删除标识对应DB的delete_flag 未删除
    int NOT_DELETED = 0;

    // 逻辑删除标识对应DB的delete_flag 已删除
    int DELETED = 1;

    int STATUS_NORMAL = 0;

    int STATUS_DISABLE = 1;

    int IS_DEFAULT = 1;

    int NOT_DEFAULT = 0;

    int UNAUDITED = 0;

    int REVIEWED = 1;
}
