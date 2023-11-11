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
package com.wansenai.service.receipt.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansenai.dto.receipt.purchase.PurchaseOrderDTO;
import com.wansenai.dto.receipt.purchase.QueryPurchaseOrderDTO;
import com.wansenai.entities.receipt.ReceiptPurchaseMain;
import com.wansenai.mappers.receipt.ReceiptPurchaseMainMapper;
import com.wansenai.service.receipt.ReceiptPurchaseService;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.receipt.purchase.PurchaseOrderDetailVO;
import com.wansenai.vo.receipt.purchase.PurchaseOrderVO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceiptPurchaseServiceImpl extends ServiceImpl<ReceiptPurchaseMainMapper, ReceiptPurchaseMain> implements ReceiptPurchaseService {

    @Override
    public Response<Page<PurchaseOrderVO>> getPurchaseOrderPage(QueryPurchaseOrderDTO queryPurchaseOrderDTO) {
        return null;
    }

    @Override
    public Response<PurchaseOrderDetailVO> getPurchaseOrderDetail(Long id) {
        return null;
    }

    @Override
    public Response<String> addOrUpdatePurchaseOrder(PurchaseOrderDTO purchaseOrderDTO) {
        return null;
    }

    @Override
    public Response<String> deletePurchaseOrder(List<Long> ids) {
        return null;
    }

    @Override
    public Response<String> updatePurchaseOrderStatus(List<Long> ids, Integer status) {
        return null;
    }
}
