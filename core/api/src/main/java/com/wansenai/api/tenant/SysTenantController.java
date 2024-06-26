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
package com.wansenai.api.tenant;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.dto.tenant.AddOrUpdateTenantDTO;
import com.wansenai.dto.tenant.TenantListDTO;
import com.wansenai.service.tenant.ISysTenantService;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.TenantInfoVO;
import org.springframework.web.bind.annotation.*;

/**
 * 租户 前端控制器
 */
@RestController
@RequestMapping("/tenant")
public class SysTenantController {

    private final ISysTenantService tenantService;

    public SysTenantController(ISysTenantService tenantService) {
        this.tenantService = tenantService;
    }

    @PostMapping(value = "list")
    public Response<Page<TenantInfoVO>> tenantList(@RequestBody TenantListDTO tenantListDTO) {
        return tenantService.tenantList(tenantListDTO);
    }

    @PostMapping(value = "addOrUpdate")
    public Response<String> addOrUpdate(@RequestBody AddOrUpdateTenantDTO addOrUpdateTenantDTO) {
        return tenantService.addOrUpdate(addOrUpdateTenantDTO);
    }

    @GetMapping(value = "checkAddUser")
    public Response<String> checkAddUser() {
        return tenantService.checkAddUser();
    }

    @PostMapping(value= "update")
    public Response<String> update(@RequestBody AddOrUpdateTenantDTO updateDTO) {
        return tenantService.update(updateDTO);
    }

    @PostMapping(value = "delete")
    public Response<String> delete(@RequestParam(value = "tenantId") String tenantId) {
        return tenantService.delete(tenantId);
    }
}
