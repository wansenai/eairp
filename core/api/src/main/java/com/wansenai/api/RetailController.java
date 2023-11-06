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
package com.wansenai.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.dto.receipt.QueryRetailRefundDTO;
import com.wansenai.dto.receipt.QueryShipmentsDTO;
import com.wansenai.dto.receipt.RetailRefundDTO;
import com.wansenai.dto.receipt.RetailShipmentsDTO;
import com.wansenai.service.receipt.ReceiptService;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.receipt.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/retail")
public class RetailController {

    private final ReceiptService receiptService;

    public RetailController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @PostMapping("/shipments/pageList")
    public Response<Page<RetailShipmentsVO>> pageList(@RequestBody QueryShipmentsDTO queryShipmentsDTO) {
        return receiptService.getRetailShipmentsPage(queryShipmentsDTO);
    }

    @PostMapping("/shipments/list")
    public Response<List<RetailShipmentsVO>> getList(@RequestBody QueryShipmentsDTO queryShipmentsDTO) {
        return receiptService.getRetailShipmentsList(queryShipmentsDTO);
    }

    @PostMapping("/shipments/addOrUpdate")
    public Response<String> addOrUpdate(@RequestBody RetailShipmentsDTO retailShipmentsDTO) {
        return receiptService.addOrUpdateRetailShipments(retailShipmentsDTO);
    }

    @PostMapping("/shipments/deleteByIds")
    public Response<String> deleteByIds(@RequestParam("ids") List<Long> ids) {
        return receiptService.deleteRetailShipments(ids);
    }

    @PutMapping("/shipments/updateStatus")
    public Response<String> updateStatus(@RequestParam("ids") List<Long> ids, @RequestParam("status") Integer status) {
        return receiptService.updateRetailShipmentsStatus(ids, status);
    }

    @GetMapping("/shipments/detail/{id}")
    public Response<RetailShipmentsDetailVO> detail(@PathVariable("id") Long id) {
        return receiptService.getRetailShipmentsDetail(id);
    }

    @PostMapping("/refund/pageList")
    public Response<Page<RetailRefundVO>> refundPageList(@RequestBody QueryRetailRefundDTO refundDTO) {
        return receiptService.getRetailRefund(refundDTO);
    }

    @PostMapping("/refund/addOrUpdate")
    public Response<String> refundAddOrUpdate(@RequestBody RetailRefundDTO refundDTO) {
        return receiptService.addOrUpdateRetailRefund(refundDTO);
    }

    @GetMapping("/refund/detail/{id}")
    public Response<RetailRefundDetailVO> refundDetail(@PathVariable("id") Long id) {
        return receiptService.getRetailRefundDetail(id);
    }
}
