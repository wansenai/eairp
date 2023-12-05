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
package com.wansenai.api.warehouse;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.dto.warehouse.AllotReceiptDTO;
import com.wansenai.dto.warehouse.QueryAllotReceiptDTO;
import com.wansenai.service.warehouse.AllotShipmentsService;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.warehouse.AllotReceiptDetailVO;
import com.wansenai.vo.warehouse.AllotReceiptVO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("warehouse/allotShipments")
public class AllotShipmentsController {

    private final AllotShipmentsService allotShipmentsService;

    public AllotShipmentsController(AllotShipmentsService allotShipmentsService) {
        this.allotShipmentsService = allotShipmentsService;
    }

    @PostMapping("addOrUpdate")
    public Response<String> addOrUpdateAllotShipments(@RequestBody AllotReceiptDTO allotReceiptDTO) {
        return allotShipmentsService.addOrUpdateAllotReceipt(allotReceiptDTO);
    }

    @PostMapping("pageList")
    public Response<Page<AllotReceiptVO>> getAllotShipmentsPageList(@RequestBody QueryAllotReceiptDTO queryAllotReceiptDTO) {
        return allotShipmentsService.getAllotReceiptPageList(queryAllotReceiptDTO);
    }

    @GetMapping("getDetailById/{id}")
    public Response<AllotReceiptDetailVO> getAllotShipmentsDetailById(@PathVariable("id") Long id) {
        return allotShipmentsService.getAllotReceiptDetail(id);
    }

    @PutMapping("deleteByIds")
    public Response<String> deleteAllotShipmentsByIds(@RequestParam("ids") List<Long> ids) {
        return allotShipmentsService.deleteBatchAllotReceipt(ids);
    }

    @PutMapping("updateStatusByIds")
    public Response<String> updateAllotShipmentsStatusByIds(@RequestParam("ids") List<Long> ids, @RequestParam("status") Integer status) {
        return allotShipmentsService.updateAllotReceiptStatus(ids, status);
    }

    @GetMapping("export")
    public void exportAllotShipments(@ModelAttribute QueryAllotReceiptDTO queryAllotReceiptDTO, HttpServletResponse response) throws Exception {
        allotShipmentsService.exportAllotReceipt(queryAllotReceiptDTO, response);
    }

    @GetMapping("exportDetail/{receiptNumber}")
    public void exportAllotShipmentsDetail(@PathVariable("receiptNumber") String receiptNumber, HttpServletResponse response) throws Exception {
        allotShipmentsService.exportAllotReceiptDetail(receiptNumber, response);
    }
}
