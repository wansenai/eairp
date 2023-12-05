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
package com.wansenai.api.system;

import com.wansenai.dto.system.SystemConfigDTO;
import com.wansenai.service.system.SysConfigService;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.SystemConfigVO;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/sys/config")
public class SysConfigController {

    private final SysConfigService sysConfigService;

    public SysConfigController(SysConfigService sysConfigService) {
        this.sysConfigService = sysConfigService;
    }

    @GetMapping("getCompanyInfo")
    public Response<SystemConfigVO> getSystemConfigInfo() {
        return sysConfigService.getSystemConfigInfo();
    }

    @PostMapping("addOrUpdate")
    public Response<String> addOrUpdateCompanyInfo(@RequestBody SystemConfigDTO systemConfigDTO) {
        return sysConfigService.addOrUpdateCompanyInfo(systemConfigDTO);
    }
}
