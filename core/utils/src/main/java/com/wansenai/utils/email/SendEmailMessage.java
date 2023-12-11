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
package com.wansenai.utils.email;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendEmailMessage {

    // 格式类型，如 text/html;charset=gbk
    private String type;
    // 发送人
    private String from;
    // 主题
    private String subject;
    // 内容
    private String text;
    // 接收人，多个接收人用逗号分隔
    private String recipient;
    // 发送时间
    private String datetime;
}
