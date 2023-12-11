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

public interface EmailConstant {

    /** 邮箱域 */
    String EMAIL_HOST = "smtp.163.com";

    String EMAIL_USER_NAME = "wanstech@163.com";

    /** 邮箱授权码 */
    String EMAIL_PASSWORD = "YSWXSMYYQSVDSBYL";

    /** 邮件HMAC256加密算法头字符串 */
    String EMAIL_HMAC256_TOKEN_HEAD = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";

    /** 邮件HMAC256加密算法ayload字符串 */
    String EMAIL_HMAC256_TOKEN_PAYLOAD = "eyJsb2dpbkVtYWlsIjoiemhhb3dlaUBpaHVwLm9yZy5jbiIsImV4cCI6MTYwMDg0MDUyOSwidXNlcm5hbWUiOiJ6aGFvd2VpIn0";
}
