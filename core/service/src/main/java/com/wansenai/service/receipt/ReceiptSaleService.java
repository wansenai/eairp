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
import com.wansenai.dto.receipt.sale.*;
import com.wansenai.entities.receipt.ReceiptSaleMain;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.receipt.sale.*;

import java.util.List;

public interface ReceiptSaleService extends IService<ReceiptSaleMain> {

    /**
     * Paging sales order data based on public query criteria
     * 根据公共查询条件分页销售订单数据
     *
     * @param querySaleOrderDTO Query common conditions
     *                          查询公共条件
     * @return Returns paginated data
     *        返回分页数据
     */
    Response<Page<SaleOrderVO>> getSaleOrderPage(QuerySaleOrderDTO querySaleOrderDTO);

    /**
     * Query sales order details data based on document ID
     * 根据单据id查询销售订单详情数据
     *
     * @param id Primary key id of sales order
     *           销售订单主键id
     * @return Returns sales order details data
     *          返回销售订单详情数据
     */
    Response<SaleOrderDetailVO> getSaleOrderDetail(Long id);

    /**
     * Query sales order details data based on sales order number
     * 根据销售订单编号查询销售订单详情数据
     *
     * @param receiptNumber Sales order number
     *                      销售订单编号
     * @return  Returns sales order details data
     *         返回销售订单详情数据
     */
    Response<SaleOrderDetailVO> getLinkSaleOrderDetail(String receiptNumber);

    /**
     * Add/Update Sales Order Data
     * 新增/修改 销售订单数据
     *
     * @param saleOrderDTO Sales order data
     *                     销售订单数据
     * @return Returns the result of the addition
     *         返回新增/修改结果
     */
    Response<String> addOrUpdateSaleOrder(SaleOrderDTO saleOrderDTO);

    /**
     * Batch delete sales order data based on ID set (logical deletion modification status)
     * 根据id集合批量删除销售订单数据 (逻辑删除修改状态)
     *
     * @param ids Sales order ID List
     *            销售订单主键id集合
     * @return Returns the result of the deletion
     *        返回删除结果
     */
    Response<String> deleteSaleOrder(List<Long> ids);

    /**
     * Batch modify the status of sales orders based on the ID set and new status
     * 根据id集合和新状态批量修改销售订单状态
     *
     * @param ids   Sales order ID List
     *              销售订单主键id集合
     * @param status Status
     *               状态
     * @return Returns the result of the modification
     */
    Response<String> updateSaleOrderStatus(List<Long> ids, Integer status);

    /**
     * Pagination query of sales shipments data
     * 销售出货数据分页查询
     *
     * @param shipmentsDTO Query common conditions
     *                     查询公共条件
     * @return Returns paginated data
     *       返回分页数据
     */
    Response<Page<SaleShipmentsVO>> getSaleShipmentsPage(QuerySaleShipmentsDTO shipmentsDTO);

    /**
     * Query the detailed data of sales outbound based on the document ID (primary key)
     * 根据单据id(主键)查询销售出货详细数据
     *
     * @param id Primary key id of sales shipments
     *           销售出货主键id
     * @return  Returns sales shipments details data
     *        返回销售出货详情数据
     */
    Response<SaleShipmentsDetailVO> getSaleShipmentsDetail(Long id);

    /**
     * Query sales delivery order details data based on sales delivery order number
     * 根据销售出货单编号查询销售出货单详情数据
     *
     * @param receiptNumber Sales delivery order number
     *                      销售出货单编号
     * @return Returns sales delivery order details data
     *        返回销售出货单详情数据
     */
    Response<SaleShipmentsDetailVO> getLinkSaleShipmentsDetail(String receiptNumber);

    /**
     * Add or modify sales delivery orders
     * 新增或修改销售出货单
     *
     * @param shipmentsDTO Sales delivery order data
     *                     销售出货单数据
     * @return Returns the result of the addition
     *          返回新增结果
     */
    Response<String> addOrUpdateSaleShipments(SaleShipmentsDTO shipmentsDTO);

    /**
     * Modify document data based on receipt ID set and status
     * 根据单据id集合和状态修改单据数据
     *
     * @param ids   Receipt ID List (primary key)
     *              单据id集合(主键)
     * @param status    Status
     *                  状态
     * @return  Returns the result of the modification
     *          返回修改结果
     */
    Response<String> updateSaleShipmentsStatus(List<Long> ids, Integer status);

    /**
     * Logical deletion of data based on document ID set (modification of deletion identification status)
     * 根据单据id集合逻辑删除数据(修改删除标识状态)
     *
     * @param ids  Receipt ID List (primary key)
     *             单据id集合(主键)
     * @return  Returns the result of the deletion
     *          返回删除结果
     */
    Response<String> deleteSaleShipments(List<Long> ids);

    /**
     * Query sales return pagination data
     * 查询销售退货分页数据
     *
     * @param refundDTO Query common conditions
     *                  查询公共条件
     * @return Returns paginated data
     *         返回分页数据
     */
    Response<Page<SaleRefundVO>> getSaleRefundPage(QuerySaleRefundDTO refundDTO);

    /**
     * Query sales refund data based on receipt ID
     * 根据单据id查询销售退货数据
     *
     * @param id Primary key id of sales refund
     *           销售退货主键id
     * @return  Returns sales refund data
     *         返回销售退货数据
     */
    Response<SaleRefundDetailVO> getSaleRefundDetail(Long id);

    /**
     * Query sales return order details data based on sales return order number
     * 根据销售退货单编号查询销售退货单详情数据
     *
     * @param receiptNumber Sales return order number
     *                      销售退货单编号
     * @return Returns sales return order details data
     *        返回销售退货单详情数据
     */
    Response<SaleRefundDetailVO> getLinkSaleRefundDetail(String receiptNumber);

    /**
     * Add or modify sales refund orders
     * 新增或修改销售退货单
     *
     * @param refundDTO Sales refund order data
     *                  销售退货单数据
     * @return  Returns the result of the addition
     *        返回新增/修改结果
     */
    Response<String> addOrUpdateSaleRefund(SaleRefundDTO refundDTO);

    /**
     * Logical deletion of sales refund data based on sales receipt ID list
     * 根据销售单据id集合逻辑删除销售退货数据
     *
     * @param ids Sales order ID List
     *            销售订单主键id集合
     * @return  Returns the result of the deletion
     *         返回删除结果
     */
    Response<String> deleteSaleRefund(List<Long> ids);

    /**
     * Modify sales refund data based on sales receipt ID list and status
     * 根据销售单据id集合和状态修改销售退货数据
     *
     * @param ids  Sales order ID List
     *             销售订单主键id集合
     * @param status Status
     *               状态
     * @return Returns the result of the modification
     *        返回修改结果
     */
    Response<String> updateSaleRefundStatus(List<Long> ids, Integer status);
}
