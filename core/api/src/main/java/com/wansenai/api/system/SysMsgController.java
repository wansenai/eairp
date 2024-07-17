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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.dto.system.UpdateSystemMessageDTO;
import com.wansenai.service.system.ISysMsgService;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.SystemMessageVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 消息表 前端控制器
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@RestController
@RequestMapping("/sys/message")
public class SysMsgController {

    private final ISysMsgService sysMsgService;

    public SysMsgController(ISysMsgService sysMsgService) {
        this.sysMsgService = sysMsgService;
    }

    @GetMapping("list")
    public Response<List<SystemMessageVO>> messagePageList() {
        return sysMsgService.getMessagePageList();
    }

    @PostMapping("read")
    public Response<String> readMessage(@RequestBody UpdateSystemMessageDTO updateSystemMessageDTO) {
        return sysMsgService.updateMessageStatus(updateSystemMessageDTO);
    }
}
