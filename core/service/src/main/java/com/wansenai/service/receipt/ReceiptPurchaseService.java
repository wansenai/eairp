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
package com.wansenai.service.receipt;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wansenai.dto.receipt.purchase.PurchaseOrderDTO;
import com.wansenai.dto.receipt.purchase.QueryPurchaseOrderDTO;
import com.wansenai.entities.receipt.ReceiptPurchaseMain;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.receipt.purchase.PurchaseOrderDetailVO;
import com.wansenai.vo.receipt.purchase.PurchaseOrderVO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReceiptPurchaseService extends IService<ReceiptPurchaseMain> {

    /**
     * Paging and querying purchase order documents based on public conditions
     *  根据公共条件分页查询采购订单单据
     *
     * @param queryPurchaseOrderDTO Query common conditions
     *                              查询公共条件
     * @return  Returns paginated data
     *         返回分页数据
     */
    Response<Page<PurchaseOrderVO>> getPurchaseOrderPage(QueryPurchaseOrderDTO queryPurchaseOrderDTO);

    /**
     * Query purchase order details data based on purchase document ID
     * 根据采购单据ID查询采购订单详情数据
     *
     * @param id Purchase order ID
     *           采购订单ID
     * @return Returns purchase order details data
     *       返回采购订单详情数据
     */
    Response<PurchaseOrderDetailVO> getPurchaseOrderDetail(Long id);

    /**
     * Add/Update Purchase Order Data
     * 添加/修改采购订单数据
     *
     * @param purchaseOrderDTO Purchase order data object
     *                         采购订单数据对象
     * @return Return to Add/Modify Status Results
     *        返回添加/修改状态结果
     */
    Response<String> addOrUpdatePurchaseOrder(PurchaseOrderDTO purchaseOrderDTO);

    /**
     * Logically delete purchase order data based on the purchase order document ID set (modify deletion status)
     * 根据采购订单单据ID集合逻辑删除采购订单数据（修改删除状态）
     *
     * @param ids Purchase order document ID set
     *            采购订单单据ID集合
     * @return  Return to delete status results
     *          返回删除状态结果
     */
    Response<String> deletePurchaseOrder(List<Long> ids);

    /**
     * Modify status based on purchase order document ID set
     * 根据采购订单单据ID集合修改状态
     *
     * @param ids  Purchase order document ID set
     *             采购订单单据ID集合
     * @param status Status to be modified
     *               要修改的状态
     * @return  Return to modify status results
     *         返回修改状态结果
     */
    Response<String> updatePurchaseOrderStatus(List<Long> ids, Integer status);
}
