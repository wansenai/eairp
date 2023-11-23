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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansenai.dto.receipt.purchase.*;
import com.wansenai.entities.receipt.ReceiptPurchaseMain;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.receipt.purchase.*;

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
     * Query purchase order details data based on purchase order number
     * 根据采购订单单号查询采购订单详情数据
     *
     * @param receiptNumber Purchase order number
     *                      采购订单单号
     * @return  Returns purchase order details data
     *       返回采购订单详情数据
     */
    Response<PurchaseOrderDetailVO> getLinkPurchaseOrderDetail(String receiptNumber);

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

    /**
     * Pagination query of purchase receipt warehousing based on public conditions
     * 根据公共条件分页查询采购入库单数据
     *
     * @param queryPurchaseStorageDTO Query common conditions
     *                                查询公共条件
     * @return Returns paginated data
     *        返回分页数据
     */
    Response<Page<PurchaseStorageVO>> getPurchaseStoragePage(QueryPurchaseStorageDTO queryPurchaseStorageDTO);

    /**
     * Query the detailed data of the purchase receipt order based on the ID
     * 根据ID查询采购入库单详情数据
     *
     * @param id Purchase receipt order ID
     *           采购入库单ID
     * @return Returns purchase receipt order details data
     *      返回采购入库单详情数据
     */
    Response<PurchaseStorageDetailVO> getPurchaseStorageDetail(Long id);

    /**
     * Query the detailed data of the purchase receipt order based on the purchase receipt order number
     * 根据采购入库单单号查询采购入库单详情数据
     *
     * @param receiptNumber Purchase receipt order number
     *                      采购入库单单号
     * @return Returns purchase receipt order details data
     *     返回采购入库单详情数据
     */
    Response<PurchaseStorageDetailVO> getLinkPurchaseStorageDetail(String receiptNumber);

    /**
     * Add/Modify Purchase Receipt Data
     * 添加/修改采购入库数据
     *
     * @param purchaseStorageDTO Purchase receipt data object
     *                         采购入库数据对象
     * @return Return to Add/Modify Status Results
     *      返回添加/修改状态结果
     */
    Response<String> addOrUpdatePurchaseStorage(PurchaseStorageDTO purchaseStorageDTO);

    /**
     * Batch delete purchase receipt order data based on ID (logical deletion, modification, and deletion identification)
     * 根据ID批量删除采购入库单数据（逻辑删除，修改删除标识）
     *
     * @param ids Purchase receipt order ID set
     *            采购入库单ID集合
     * @return Return to delete status results
     *       返回删除状态结果
     */
    Response<String> deletePurchaseStorage(List<Long> ids);

    /**
     * Modify the purchase receipt order data based on the ID set
     * 根据ID集合修改采购入库单数据
     *
     * @param ids Purchase receipt order ID set
     *            采购入库单ID集合
     *
     * @param status Status to be modified
     *               要修改的状态
     * @return Return to modify status results
     */
    Response<String> updatePurchaseStorageStatus(List<Long> ids, Integer status);

    /**
     * Pagination query of purchase return order data based on public query criteria
     * 根据公共查询条件分页查询采购退货单数据
     *
     * @param queryPurchaseRefundDTO Query common conditions
     *                               查询公共条件
     * @return Returns paginated data
     *       返回分页数据
     */
    Response<Page<PurchaseRefundVO>> getPurchaseRefundPage(QueryPurchaseRefundDTO queryPurchaseRefundDTO);

    /**
     * Query purchase return details data based on receipt ID
     * 根据单据ID查询采购退货详情数据
     *
     * @param id Receipt ID
     *           单据ID
     * @return Returns purchase return details data
     *     返回采购退货详情数据
     */
    Response<PurchaseRefundDetailVO> getPurchaseRefundDetail(Long id);

    /**
     * Query the detailed data of the purchase return order based on the purchase return order number
     * 根据采购退货单单号查询采购退货单详情数据
     *
     * @param receiptNumber Purchase return order number
     *                      采购退货单单号
     * @return Returns purchase return order details data
     *     返回采购退货单详情数据
     */
    Response<PurchaseRefundDetailVO> getLinkPurchaseRefundDetail(String receiptNumber);

    /**
     * Add/Modify Purchase Return Order Data
     * 添加/修改采购退货单数据
     *
     * @param purchaseRefundDTO Purchase return order data object
     *                          采购退货单数据对象
     * @return Return to Add/Modify Status Results
     *          返回添加/修改状态结果
     */
    Response<String> addOrUpdatePurchaseRefund(PurchaseRefundDTO purchaseRefundDTO);

    /**
     * Batch delete purchase return data based on purchase return document ID (logical deletion, modification, and deletion identification)
     * 根据采购退货单据ID批量删除采购退货数据（逻辑删除，修改删除标识）
     *
     * @param ids Purchase return document ID set
     *            采购退货单据ID集合
     * @return Return to delete status results
     *       返回删除状态结果
     */
    Response<String> deletePurchaseRefund(List<Long> ids);

    /**
     * Batch modify status based on purchase return document ID
     * 根据采购退货单据ID批量修改状态
     *
     * @param ids Purchase return document ID set
     *            采购退货单据ID集合
     * @param status Status to be modified
     *               要修改的状态
     * @return  Return to modify status results
     *         返回修改状态结果
     */
    Response<String> updatePurchaseRefundStatus(List<Long> ids, Integer status);

    /**
     * Query purchase arrears receipt based on general conditions
     * 根据公共条件查询采购欠款单据
     *
     * @param arrearsDTO Query common conditions
     *                   查询公共条件
     * @return Returns paginated data
     *      返回分页数据
     */
    Response<Page<PurchaseArrearsVO>> getPurchaseArrearsPage(QueryPurchaseArrearsDTO arrearsDTO);
}
