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
package com.wansenai.api.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.dto.basic.AddOrUpdateOperatorDTO;
import com.wansenai.dto.basic.QueryOperatorDTO;
import com.wansenai.service.basic.IOperatorService;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.basic.OperatorVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/basic/operator")
public class OperatorController {

    private final IOperatorService operatorService;

    public OperatorController(IOperatorService operatorService) {
        this.operatorService = operatorService;
    }

    @PostMapping("pageList")
    public Response<Page<OperatorVO>> getOperatorPageList(@RequestBody QueryOperatorDTO queryOperatorDTO) {
        return operatorService.getOperatorPageList(queryOperatorDTO);
    }

    @PostMapping("addOrUpdate")
    public Response<String> addOrUpdateOperator(@RequestBody AddOrUpdateOperatorDTO addOrUpdateOperatorDTO) {
        return operatorService.addOrUpdateOperator(addOrUpdateOperatorDTO);
    }

    @DeleteMapping("delete")
    public Response<String> deleteOperator(@RequestParam("ids") List<Long> ids) {
        return operatorService.deleteBatchOperator(ids);
    }

    @PostMapping("updateStatus")
    public Response<String> updateOperatorStatus(@RequestParam("ids") List<Long> ids, @RequestParam("status") Integer status) {
        return operatorService.updateOperatorStatus(ids, status);
    }

    @GetMapping("list/{type}")
    public Response<List<OperatorVO>> getOperatorList(@PathVariable("type") String type) {
        return operatorService.getOperatorListByType(type);
    }
}
