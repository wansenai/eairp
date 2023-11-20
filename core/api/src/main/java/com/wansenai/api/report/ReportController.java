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
package com.wansenai.api.report;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.dto.report.*;
import com.wansenai.service.receipt.ReceiptService;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.receipt.retail.StatisticalDataVO;
import com.wansenai.vo.report.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReceiptService receiptService;

    public ReportController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @GetMapping("homePage/statistics")
    public Response<StatisticalDataVO> getStatisticalData() {
        return receiptService.getStatisticalData();
    }

    @PostMapping("productStock")
    public Response<IPage<ProductStockVO>> getProductStock(@RequestBody QueryProductStockDTO queryProductStockDTO) {
        return receiptService.getProductStock(queryProductStockDTO);
    }

    @PostMapping("productStockFlow")
    public Response<Page<StockFlowVO>> getStockFlow(@RequestBody QueryStockFlowDTO stockFlowDTO) {
        return receiptService.getStockFlow(stockFlowDTO);
    }

    @PostMapping("accountStatistics")
    public Response<Page<AccountStatisticsVO>> getAccountStatistics(@RequestBody QueryAccountStatisticsDTO accountStatisticsDTO) {
        return receiptService.getAccountStatistics(accountStatisticsDTO);
    }

    @GetMapping("accountFlow")
    public Response<Page<AccountFlowVO>> getAccountFlow(
            @RequestParam("accountId") Long accountId,
            @RequestParam(value = "page", defaultValue = "1") Long page,
            @RequestParam(value = "size", defaultValue = "10") Long pageSize
    ) {
        return receiptService.getAccountFlow(accountId, page, pageSize);
    }

    @PostMapping("retailStatistics")
    public Response<Page<RetailReportVO>> getRetailStatistics(@RequestBody QueryRetailReportDTO queryRetailReportDTO) {
        return receiptService.getRetailStatistics(queryRetailReportDTO);
    }

    @PostMapping("purchaseStatistics")
    public Response<Page<PurchaseReportVO>> getPurchaseStatistics(@RequestBody QueryPurchaseReportDTO queryPurchaseReportDTO) {
        return receiptService.getPurchaseStatistics(queryPurchaseReportDTO);
    }

    @PostMapping("salesStatistics")
    public Response<Page<SalesReportVO>> getSalesStatistics(@RequestBody QuerySalesReportDTO querySalesReportDTO) {
        return receiptService.getSalesStatistics(querySalesReportDTO);
    }

    @GetMapping("relatedPerson")
    public Response<List<RelatedPersonVO>> getRelatedPerson() {
        return receiptService.getRelatedPerson();
    }

    @PostMapping("shipmentsDetail")
    public Response<Page<ShipmentsDetailVO>> getShipmentsDetail(@RequestBody QueryShipmentsDetailDTO queryShipmentsDetailDTO) {
        return receiptService.getShipmentsDetail(queryShipmentsDetailDTO);
    }

    @PostMapping("storageDetail")
    public Response<Page<StorageDetailVO>> getStorageDetail(@RequestBody QueryStorageDetailDTO queryStorageDetailDTO) {
        return receiptService.getStorageDetail(queryStorageDetailDTO);
    }

    @PostMapping("shipmentsSummary")
    public Response<Page<ShipmentsSummaryVO>> getShipmentsSummary(@RequestBody QueryShipmentsSummaryDTO queryShipmentsSummaryDTO) {
        return receiptService.getShipmentsSummary(queryShipmentsSummaryDTO);
    }

    @PostMapping("storageSummary")
    public Response<Page<StorageSummaryVO>> getStorageSummary(@RequestBody QueryStorageSummaryDTO queryStorageSummaryDTO) {
        return receiptService.getStorageSummary(queryStorageSummaryDTO);
    }
}
