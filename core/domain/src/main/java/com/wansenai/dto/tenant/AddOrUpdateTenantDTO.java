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
package com.wansenai.dto.tenant;

import com.wansenai.dto.user.AddOrUpdateUserDTO;
import lombok.Data;

import java.util.List;

@Data
public class AddOrUpdateTenantDTO {

    private Long id;

    private String tenantName;

    private Integer userNumLimit;

    private Integer type;

    private Integer status;

    private String remark;

    private String expireTime;

    private String phoneNumber;

    private String username;

    private String password;

    private String email;

    private List<Long> deptId;

    private List<Long> roleId;
}
