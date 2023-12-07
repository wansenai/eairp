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
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    public Response<IPage<ProductStockSkuVO>> getProductStock(@RequestBody QueryProductStockDTO queryProductStockDTO) {
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

    @PostMapping("customerBill")
    public Response<Page<CustomerBillVO>> getCustomerBill(@RequestBody QueryCustomerBillDTO queryCustomerBillDTO) {
        return receiptService.getCustomerBill(queryCustomerBillDTO);
    }

    @PostMapping("customerBillDetail")
    public Response<Page<CustomerBillDetailVO>> getCustomerBillDetail(@RequestBody QueryCustomerBillDetailDTO queryCustomerBillDetailDTO) {
        return receiptService.getCustomerBillDetail(queryCustomerBillDetailDTO);
    }

    @PostMapping("supplierBill")
    public Response<Page<SupplierBillVO>> getSupplierBill(@RequestBody QuerySupplierBillDTO querySupplierBillDTO) {
        return receiptService.getSupplierBill(querySupplierBillDTO);
    }

    @PostMapping("supplierBillDetail")
    public Response<Page<SupplierBillDetailVO>> getSupplierBillDetail(@RequestBody QuerySupplierBillDetailDTO querySupplierBillDetailDTO) {
        return receiptService.getSupplierBillDetail(querySupplierBillDetailDTO);
    }

    @GetMapping("/productStock/export")
    public void exportProductStockExcel(@ModelAttribute QueryProductStockDTO queryProductStockDTO, HttpServletResponse response) throws IOException {
        receiptService.exportProductStockExcel(queryProductStockDTO, response);
    }

    @GetMapping("/accountStatistics/export")
    public void exportAccountStatisticsExcel(@ModelAttribute QueryAccountStatisticsDTO queryAccountStatisticsDTO, HttpServletResponse response) throws IOException {
        receiptService.exportAccountStatisticsExcel(queryAccountStatisticsDTO, response);
    }

    @GetMapping("/retailStatistics/export")
    public void exportRetailStatisticsExcel(@ModelAttribute QueryRetailReportDTO queryRetailReportDTO, HttpServletResponse response) throws IOException {
        receiptService.exportRetailStatisticsExcel(queryRetailReportDTO, response);
    }

    @GetMapping("/purchaseStatistics/export")
    public void exportPurchaseStatisticsExcel(@ModelAttribute QueryPurchaseReportDTO queryPurchaseReportDTO, HttpServletResponse response) throws IOException {
        receiptService.exportPurchaseStatisticsExcel(queryPurchaseReportDTO, response);
    }

    @GetMapping("/salesStatistics/export")
    public void exportSalesStatisticsExcel(@ModelAttribute QuerySalesReportDTO querySalesReportDTO, HttpServletResponse response) throws IOException {
        receiptService.exportSalesStatisticsExcel(querySalesReportDTO, response);
    }

    @GetMapping("/shipmentsDetail/export")
    public void exportShipmentsDetailExcel(@ModelAttribute QueryShipmentsDetailDTO queryShipmentsDetailDTO, HttpServletResponse response) throws IOException {
        receiptService.exportShipmentsDetailExcel(queryShipmentsDetailDTO, response);
    }

    @GetMapping("/storageDetail/export")
    public void exportStorageDetailExcel(@ModelAttribute QueryStorageDetailDTO queryStorageDetailDTO, HttpServletResponse response) throws IOException {
        receiptService.exportStorageDetailExcel(queryStorageDetailDTO, response);
    }

    @GetMapping("/shipmentsSummary/export")
    public void exportShipmentsSummaryExcel(@ModelAttribute QueryShipmentsSummaryDTO queryShipmentsSummaryDTO, HttpServletResponse response) throws IOException {
        receiptService.exportShipmentsSummaryExcel(queryShipmentsSummaryDTO, response);
    }

    @GetMapping("/storageSummary/export")
    public void exportStorageSummaryExcel(@ModelAttribute QueryStorageSummaryDTO queryStorageSummaryDTO, HttpServletResponse response) throws IOException {
        receiptService.exportStorageSummaryExcel(queryStorageSummaryDTO, response);
    }

    @GetMapping("/customerBill/export")
    public void exportCustomerBillExcel(@ModelAttribute QueryCustomerBillDTO queryCustomerBillDTO, HttpServletResponse response) throws IOException {
        receiptService.exportCustomerBillExcel(queryCustomerBillDTO, response);
    }

    @GetMapping("/supplierBill/export")
    public void exportSupplierBillExcel(@ModelAttribute QuerySupplierBillDTO querySupplierBillDTO, HttpServletResponse response) throws IOException {
        receiptService.exportSupplierBillExcel(querySupplierBillDTO, response);
    }
}
