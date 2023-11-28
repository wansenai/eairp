import {defHttp} from '/@/utils/http/axios';
import {BaseDataResp} from "@/api/model/baseModel";
import {
    ProductStockResp,
    QueryProductStockReq,
    RetailStatisticalResp,
    ProductStockFlowResp,
    QueryProductStockFlowReq,
    QueryAccountStatisticsReq,
    AccountStatisticsResp,
    AccountFlowResp,
    QueryRetailStatisticsReq,
    RetailStatisticsResp,
    QueryPurchaseStatisticsReq,
    PurchaseStatisticsResp,
    QuerySalesStatisticsReq,
    SalesStatisticsResp,
    QueryShipmentsDetailStatisticsReq,
    ShipmentsDetailStatisticsResp,
    QueryStorageDetailStatisticsReq,
    StorageDetailStatisticsResp,
    RelatedPersonResp,
    QueryShipmentsSummaryStatisticsReq,
    ShipmentsSummaryStatisticsResp,
    QueryStorageSummaryStatisticsReq,
    StorageSummaryStatisticsResp,
    CustomerBillStatisticsResp,
    QueryCustomerBillReq,
    QueryCustomerBillDetailReq,
    CustomerBillDetailStatisticsResp,
    SupplierBillStatisticsResp,
    QuerySupplierBillReq, QuerySupplierBillDetailReq, SupplierBillDetailStatisticsResp
} from "@/api/report/reportModel";

enum API {
    getStatisticalData = '/report/homePage/statistics',
    getProductStockData = '/report/productStock',
    getProductStockFlowData = '/report/productStockFlow',
    getAccountStatistics = '/report/accountStatistics',
    getAccountFlow = '/report/accountFlow',
    getRetailStatistics = '/report/retailStatistics',
    getPurchaseStatistics = '/report/purchaseStatistics',
    getSalesStatistics = '/report/salesStatistics',
    getShipmentsDetail = '/report/shipmentsDetail',
    getStorageDetail = '/report/storageDetail',
    getRelatedPerson = '/report/relatedPerson',
    getShipmentsSummary = '/report/shipmentsSummary',
    getStorageSummary = '/report/storageSummary',
    getCustomerBill = '/report/customerBill',
    getCustomerBillDetail = '/report/customerBillDetail',
    getSupplierBill = '/report/supplierBill',
    getSupplierBillDetail = '/report/supplierBillDetail',
}


export function getStatistical() {
    return defHttp.get<BaseDataResp<RetailStatisticalResp>>(
        {
            url: API.getStatisticalData,
        },
    );
}

export function getProductStock(params: QueryProductStockReq) {
    return defHttp.post<BaseDataResp<ProductStockResp>>(
        {
            url: API.getProductStockData,
            params
        }
    );
}

export function getProductStockFlow(params: QueryProductStockFlowReq, productId: number) {
    return defHttp.post<BaseDataResp<ProductStockFlowResp>>(
        {
            url: API.getProductStockFlowData,
            params
        }
    );
}

export function getAccountStatistics(params: QueryAccountStatisticsReq) {
    return defHttp.post<BaseDataResp<AccountStatisticsResp>>(
        {
            url: API.getAccountStatistics,
            params
        }
    );
}

export function getAccountFlow(accountId: number) {
    return defHttp.get<BaseDataResp<AccountFlowResp>>(
        {
            url: API.getAccountFlow,
            params: accountId
        }
    );
}

export function getRetailStatistics(params: QueryRetailStatisticsReq) {
    return defHttp.post<BaseDataResp<RetailStatisticsResp>>(
        {
            url: API.getRetailStatistics,
            params
        }
    );
}

export function getPurchaseStatistics(params: QueryPurchaseStatisticsReq) {
    return defHttp.post<BaseDataResp<PurchaseStatisticsResp>>(
        {
            url: API.getPurchaseStatistics,
            params
        }
    );
}

export function getSalesStatistics(params: QuerySalesStatisticsReq) {
    return defHttp.post<BaseDataResp<SalesStatisticsResp>>(
        {
            url: API.getSalesStatistics,
            params
        }
    );
}

export function getShipmentsDetail(params: QueryShipmentsDetailStatisticsReq) {
    return defHttp.post<BaseDataResp<ShipmentsDetailStatisticsResp>>(
        {
            url: API.getShipmentsDetail,
            params
        }
    );
}

export function getStorageDetail(params: QueryStorageDetailStatisticsReq) {
    return defHttp.post<BaseDataResp<StorageDetailStatisticsResp>>(
        {
            url: API.getStorageDetail,
            params
        }
    );
}

export function getRelatedPerson() {
    return defHttp.get<BaseDataResp<RelatedPersonResp>>(
        {
            url: API.getRelatedPerson,
        }
    );
}

export function getShipmentsSummary(params: QueryShipmentsSummaryStatisticsReq) {
    return defHttp.post<BaseDataResp<ShipmentsSummaryStatisticsResp>>(
        {
            url: API.getShipmentsSummary,
            params
        }
    );
}

export function getStorageSummary(params: QueryStorageSummaryStatisticsReq) {
    return defHttp.post<BaseDataResp<StorageSummaryStatisticsResp>>(
        {
            url: API.getStorageSummary,
            params
        }
    );
}

export function getCustomerBill(params: QueryCustomerBillReq) {
    return defHttp.post<BaseDataResp<CustomerBillStatisticsResp>>(
        {
            url: API.getCustomerBill,
            params
        }
    );
}

export function getCustomerBillDetail(params: QueryCustomerBillDetailReq) {
    return defHttp.post<BaseDataResp<CustomerBillDetailStatisticsResp>>(
        {
            url: API.getCustomerBillDetail,
            params
        }
    );
}

export function getSupplierBill(params: QuerySupplierBillReq) {
    return defHttp.post<BaseDataResp<SupplierBillStatisticsResp>>(
        {
            url: API.getSupplierBill,
            params
        }
    );
}

export function getSupplierBillDetail(params: QuerySupplierBillDetailReq) {
    return defHttp.post<BaseDataResp<SupplierBillDetailStatisticsResp>>(
        {
            url: API.getSupplierBillDetail,
            params
        }
    );
}