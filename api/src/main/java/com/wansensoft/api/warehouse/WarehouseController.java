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
package com.wansensoft.api.warehouse;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansensoft.dto.basic.AddOrUpdateWarehouseDTO;
import com.wansensoft.dto.basic.QueryWarehouseDTO;
import com.wansensoft.service.warehouse.WarehouseService;
import com.wansensoft.utils.response.Response;
import com.wansensoft.vo.warehouse.WarehouseVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/basic/warehouse")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PostMapping("list")
    public Response<Page<WarehouseVO>> getWarehouseList(@RequestBody QueryWarehouseDTO warehouseDTO) {
        return warehouseService.getWarehouseList(warehouseDTO);
    }

    @GetMapping("getWarehouse")
    public Response<List<WarehouseVO>> getWarehouse() {
        return warehouseService.getWarehouse();
    }

    @PostMapping("addOrUpdate")
    public Response<String> addOrUpdateWarehouse(@RequestBody AddOrUpdateWarehouseDTO warehouseDTO) {
        return warehouseService.addOrUpdateWarehouse(warehouseDTO);
    }

    @DeleteMapping("delete")
    public Response<String> deleteWarehouse(@RequestParam("ids") List<Long> ids) {
        return warehouseService.deleteBatch(ids);
    }

    @PostMapping("updateStatus")
    public Response<String> updateStatus(@RequestParam("ids") List<Long> ids, @RequestParam("status") Integer status) {
        return warehouseService.updateBatchStatus(ids, status);
    }
}
