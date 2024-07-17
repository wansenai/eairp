/*
 * Copyright 2024-2033 WanSen AI Team, Inc. All Rights Reserved.
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

public interface MessageConstants {

    // Default 1 month expiration time (seconds)
    Long SYSTEM_EXPIRE_DATE = 2626560L;

    String SYSTEM_MESSAGE_PREFIX = "MESSAGE:";

    String NOTICE_TYPE_SYSTEM = "SYSTEM:";

    Integer SYSTEM_MESSAGE_UNREAD = 0;

    Integer SYSTEM_MESSAGE_READ = 1;
}
