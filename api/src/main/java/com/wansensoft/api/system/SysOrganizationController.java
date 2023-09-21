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
package com.wansensoft.api.system;

import com.wansensoft.dto.organization.OrganizationListDto;
import com.wansensoft.service.system.ISysOrganizationService;
import com.wansensoft.utils.response.Response;
import com.wansensoft.vo.OrganizationListVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 机构表 前端控制器
 * </p>
 */
@RestController
@RequestMapping("/organization")
public class SysOrganizationController {

    private final ISysOrganizationService organizationService;

    public SysOrganizationController(ISysOrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping("userOrgRel")
    public Response<OrganizationListVo> userOrgRel(@RequestBody OrganizationListDto organizationListDto) {
        return organizationService.queryOrganization(organizationListDto);
    }
}
