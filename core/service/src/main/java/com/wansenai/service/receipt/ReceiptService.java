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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.dto.receipt.QueryReceiptDTO;
import com.wansenai.dto.report.*;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.receipt.ReceiptDetailVO;
import com.wansenai.vo.receipt.ReceiptVO;
import com.wansenai.vo.receipt.retail.StatisticalDataVO;
import com.wansenai.vo.report.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public interface ReceiptService {

    /**
     * Query the data summary of the six columns on the tenant's homepage.
     * <p>
     * 根据id集合和状态批量修改零售出库单状态
     *
     * @return Return to homepage data summary
     *         返回首页数据汇总
     */
    Response<StatisticalDataVO> getStatisticalData();

    Response<Page<ReceiptVO>> otherReceipt(QueryReceiptDTO receiptDTO);

    Response<Page<ReceiptDetailVO>> getOtherDetail(QueryReceiptDTO receiptDTO);

    Response<IPage<ProductStockVO>> getProductStock(QueryProductStockDTO queryProductStockDTO);

    Response<Page<StockFlowVO>> getStockFlow(QueryStockFlowDTO queryStockFlowDTO);

    Response<Page<AccountStatisticsVO>> getAccountStatistics(QueryAccountStatisticsDTO accountStatisticsDTO);

    Response<Page<AccountFlowVO>> getAccountFlow(Long accountId, Long page, Long pageSize);

    Response<Page<RetailReportVO>> getRetailStatistics(QueryRetailReportDTO queryRetailReportDTO);

    Response<Page<PurchaseReportVO>> getPurchaseStatistics(QueryPurchaseReportDTO queryPurchaseReportDTO);

    Response<Page<SalesReportVO>> getSalesStatistics(QuerySalesReportDTO querySalesReportDTO);

    Response<Page<ShipmentsDetailVO>> getShipmentsDetail(QueryShipmentsDetailDTO queryShipmentsDetailDTO);

    Response<Page<StorageDetailVO>> getStorageDetail(QueryStorageDetailDTO queryStorageDetailDTO);

    Response<Page<ShipmentsSummaryVO>> getShipmentsSummary(QueryShipmentsSummaryDTO queryShipmentsSummaryDTO);

    Response<Page<StorageSummaryVO>> getStorageSummary(QueryStorageSummaryDTO queryStorageSummaryDTO);

    Response<List<RelatedPersonVO>> getRelatedPerson();

    Response<Page<CustomerBillVO>> getCustomerBill(QueryCustomerBillDTO queryCustomerBillDTO);

    Response<Page<CustomerBillDetailVO>> getCustomerBillDetail(QueryCustomerBillDetailDTO queryCustomerBillDetailDTO);

    Response<Page<SupplierBillVO>> getSupplierBill(QuerySupplierBillDTO querySupplierBillDTO);

    Response<Page<SupplierBillDetailVO>> getSupplierBillDetail(QuerySupplierBillDetailDTO querySupplierBillDetailDTO);

    void exportProductStockExcel(QueryProductStockDTO queryProductStockDTO, HttpServletResponse response) throws IOException;

    void exportAccountStatisticsExcel (QueryAccountStatisticsDTO queryAccountStatisticsDTO, HttpServletResponse response) throws IOException;

    void exportRetailStatisticsExcel (QueryRetailReportDTO queryRetailReportDTO, HttpServletResponse response) throws IOException;

    void exportPurchaseStatisticsExcel (QueryPurchaseReportDTO queryPurchaseReportDTO, HttpServletResponse response) throws IOException;

    void exportSalesStatisticsExcel (QuerySalesReportDTO querySalesReportDTO, HttpServletResponse response) throws IOException;

    void exportShipmentsDetailExcel (QueryShipmentsDetailDTO queryShipmentsDetailDTO, HttpServletResponse response) throws IOException;

    void exportStorageDetailExcel (QueryStorageDetailDTO queryStorageDetailDTO, HttpServletResponse response) throws IOException;

    void exportShipmentsSummaryExcel (QueryShipmentsSummaryDTO queryShipmentsSummaryDTO, HttpServletResponse response) throws IOException;

    void exportStorageSummaryExcel (QueryStorageSummaryDTO queryStorageSummaryDTO, HttpServletResponse response) throws IOException;

    void exportCustomerBillExcel (QueryCustomerBillDTO queryCustomerBillDTO, HttpServletResponse response) throws IOException;

    void exportSupplierBillExcel (QuerySupplierBillDTO querySupplierBillDTO, HttpServletResponse response) throws IOException;
}
