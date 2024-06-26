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
package com.wansenai.service.tenant;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.dto.tenant.AddOrUpdateTenantDTO;
import com.wansenai.dto.tenant.TenantListDTO;
import com.wansenai.entities.tenant.SysTenant;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.TenantInfoVO;

/**
 * <p>
 * 租户 服务类
 * </p>
 */
public interface ISysTenantService extends IService<SysTenant> {

    Response<Page<TenantInfoVO>> tenantList(TenantListDTO tenantListDTO);

    Response<String> addOrUpdate(AddOrUpdateTenantDTO addOrUpdateTenantDTO);

    Response<String> checkAddUser();

    Boolean checkTenantExpire(Long tenantId);

    Response<String> update(AddOrUpdateTenantDTO updateDTO);

    Response<String> delete(String tenantId);
}
