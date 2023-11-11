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
package com.wansenai.api.receipt;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.dto.receipt.purchase.PurchaseOrderDTO;
import com.wansenai.dto.receipt.purchase.QueryPurchaseOrderDTO;
import com.wansenai.service.receipt.ReceiptPurchaseService;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.receipt.purchase.PurchaseOrderDetailVO;
import com.wansenai.vo.receipt.purchase.PurchaseOrderVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("purchase")
public class PurchaseController {

    private final ReceiptPurchaseService purchaseService;

    public PurchaseController(ReceiptPurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping("/order/pageList")
    public Response<Page<PurchaseOrderVO>> pageList(@ModelAttribute QueryPurchaseOrderDTO queryPurchaseOrderDTO) {
        return purchaseService.getPurchaseOrderPage(queryPurchaseOrderDTO);
    }

    @PostMapping("/order/addOrUpdate")
    public Response<String> addOrUpdate(@RequestBody PurchaseOrderDTO purchaseOrderDTO) {
        return purchaseService.addOrUpdatePurchaseOrder(purchaseOrderDTO);
    }

    @GetMapping("/order/detail/{id}")
    public Response<PurchaseOrderDetailVO> detail(@PathVariable("id") Long id) {
        return purchaseService.getPurchaseOrderDetail(id);
    }

    @PutMapping("/order/updateStatus/{ids}/{status}")
    public Response<String> updateStatus(@PathVariable("ids") List<Long> ids, @PathVariable("status") Integer status) {
        return purchaseService.updatePurchaseOrderStatus(ids, status);
    }

    @PutMapping("/order/delete/{ids}")
    public Response<String> delete(@PathVariable("ids") List<Long> ids) {
        return purchaseService.deletePurchaseOrder(ids);
    }
}
