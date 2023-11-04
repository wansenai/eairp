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
import com.wansenai.dto.receipt.QueryShipmentsDTO;
import com.wansenai.dto.receipt.RetailShipmentsDTO;
import com.wansenai.vo.receipt.RetailShipmentsDetailVO;
import com.wansenai.entities.receipt.ReceiptMain;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.receipt.RetailShipmentsVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface ReceiptService extends IService<ReceiptMain> {

    /**
     * Query retail shipment orders with pagination.
     * 分页查询零售出库单
     *
     * @param shipmentsDTO Query common conditions
     *                    查询公共条件
     * @return Returns paginated data
     *         返回分页数据
     */
    Response<Page<RetailShipmentsVO>> getRetailShipments(QueryShipmentsDTO shipmentsDTO);

    /**
     * Add/modify retail shipment orders.
     * 新增/修改 零售出库单
     *
     * @param shipmentsDTO Retail shipment order data
     *                     零售出库单数据
     * @return Returns the result of the addition
     *         返回新增结果
     */
    Response<String> addOrUpdateRetailShipments(RetailShipmentsDTO shipmentsDTO);

    /**
     * Batch delete retail shipment orders.
     * 批量删除零售出库单
     *
     * @param ids Collection of primary key ids of retail shipment orders
     *            零售出库单主键id集合
     * @return Returns the result of the deletion
     *         返回删除结果
     */
    Response<String> deleteRetailShipments(List<Long> ids);

    /**
     * Query retail shipment order details by id.
     * 根据id查询零售出库详情数据单
     *
     * @param id Primary key id of retail shipment order
     *           零售出库单主键id
     * @return Returns retail shipment order data
     *         返回零售出库单数据
     */
    Response<RetailShipmentsDetailVO> getRetailShipmentsDetail(Long id);

    /**
     * According to the id collection and status, the status of the retail shipment order is modified in batches.
     * <p>
     * 根据id集合和状态批量修改零售出库单状态
     *
     * @param ids     retail shipment order primary key id
     *               零售出库单主键id
     * @param status Status
     *               状态
     * @return Returns the modification result
     *         返回修改结果
     */
    Response<String> updateRetailShipmentsStatus(List<Long> ids, Integer status);

}
