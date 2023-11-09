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
import com.wansenai.dto.receipt.retail.QueryRetailRefundDTO;
import com.wansenai.dto.receipt.retail.QueryShipmentsDTO;
import com.wansenai.dto.receipt.retail.RetailRefundDTO;
import com.wansenai.dto.receipt.retail.RetailShipmentsDTO;
import com.wansenai.service.receipt.ReceiptRetailService;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.receipt.retail.RetailRefundDetailVO;
import com.wansenai.vo.receipt.retail.RetailRefundVO;
import com.wansenai.vo.receipt.retail.RetailShipmentsDetailVO;
import com.wansenai.vo.receipt.retail.RetailShipmentsVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/retail")
public class RetailController {

    private final ReceiptRetailService receiptRetailService;

    public RetailController(ReceiptRetailService receiptRetailService) {
        this.receiptRetailService = receiptRetailService;
    }

    @PostMapping("/shipments/pageList")
    public Response<Page<RetailShipmentsVO>> pageList(@RequestBody QueryShipmentsDTO queryShipmentsDTO) {
        return receiptRetailService.getRetailShipmentsPage(queryShipmentsDTO);
    }

    @PostMapping("/shipments/list")
    public Response<List<RetailShipmentsVO>> getList(@RequestBody QueryShipmentsDTO queryShipmentsDTO) {
        return receiptRetailService.getRetailShipmentsList(queryShipmentsDTO);
    }

    @PostMapping("/shipments/addOrUpdate")
    public Response<String> addOrUpdate(@RequestBody RetailShipmentsDTO retailShipmentsDTO) {
        return receiptRetailService.addOrUpdateRetailShipments(retailShipmentsDTO);
    }

    @PostMapping("/shipments/deleteByIds")
    public Response<String> deleteByIds(@RequestParam("ids") List<Long> ids) {
        return receiptRetailService.deleteRetailShipments(ids);
    }

    @PutMapping("/shipments/updateStatus")
    public Response<String> updateStatus(@RequestParam("ids") List<Long> ids, @RequestParam("status") Integer status) {
        return receiptRetailService.updateRetailShipmentsStatus(ids, status);
    }

    @GetMapping("/shipments/detail/{id}")
    public Response<RetailShipmentsDetailVO> detail(@PathVariable("id") Long id) {
        return receiptRetailService.getRetailShipmentsDetail(id);
    }

    @PostMapping("/refund/pageList")
    public Response<Page<RetailRefundVO>> refundPageList(@RequestBody QueryRetailRefundDTO refundDTO) {
        return receiptRetailService.getRetailRefund(refundDTO);
    }

    @PostMapping("/refund/addOrUpdate")
    public Response<String> refundAddOrUpdate(@RequestBody RetailRefundDTO refundDTO) {
        return receiptRetailService.addOrUpdateRetailRefund(refundDTO);
    }

    @GetMapping("/refund/detail/{id}")
    public Response<RetailRefundDetailVO> refundDetail(@PathVariable("id") Long id) {
        return receiptRetailService.getRetailRefundDetail(id);
    }

    @PostMapping("/refund/deleteByIds")
    public Response<String> refundDeleteByIds(@RequestParam("ids") List<Long> ids) {
        return receiptRetailService.deleteRetailRefund(ids);
    }

    @PutMapping("/refund/updateStatus")
    public Response<String> refundUpdateStatus(@RequestParam("ids") List<Long> ids, @RequestParam("status") Integer status) {
        return receiptRetailService.updateRetailRefundStatus(ids, status);
    }
}
