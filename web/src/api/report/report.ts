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
    GetStatisticalData = '/report/homePage/statistics',
    GetProductStockData = '/report/productStock',
    GetProductStockFlowData = '/report/productStockFlow',
    GetAccountStatistics = '/report/accountStatistics',
    GetAccountFlow = '/report/accountFlow',
    GetRetailStatistics = '/report/retailStatistics',
    GetPurchaseStatistics = '/report/purchaseStatistics',
    GetSalesStatistics = '/report/salesStatistics',
    GetShipmentsDetail = '/report/shipmentsDetail',
    GetStorageDetail = '/report/storageDetail',
    GetRelatedPerson = '/report/relatedPerson',
    GetShipmentsSummary = '/report/shipmentsSummary',
    GetStorageSummary = '/report/storageSummary',
    GetCustomerBill = '/report/customerBill',
    GetCustomerBillDetail = '/report/customerBillDetail',
    GetSupplierBill = '/report/supplierBill',
    GetSupplierBillDetail = '/report/supplierBillDetail',
    ExportProductStockData = '/report/productStockExport',
}


export function getStatistical() {
    return defHttp.get<BaseDataResp<RetailStatisticalResp>>(
        {
            url: API.GetStatisticalData,
        },
    );
}

export function getProductStock(params: QueryProductStockReq) {
    return defHttp.post<BaseDataResp<ProductStockResp>>(
        {
            url: API.GetProductStockData,
            params
        }
    );
}

export function getProductStockFlow(params: QueryProductStockFlowReq, productId: number) {
    return defHttp.post<BaseDataResp<ProductStockFlowResp>>(
        {
            url: API.GetProductStockFlowData,
            params
        }
    );
}

export function getAccountStatistics(params: QueryAccountStatisticsReq) {
    return defHttp.post<BaseDataResp<AccountStatisticsResp>>(
        {
            url: API.GetAccountStatistics,
            params
        }
    );
}

export function getAccountFlow(accountId: number) {
    return defHttp.get<BaseDataResp<AccountFlowResp>>(
        {
            url: API.GetAccountFlow,
            params: accountId
        }
    );
}

export function getRetailStatistics(params: QueryRetailStatisticsReq) {
    return defHttp.post<BaseDataResp<RetailStatisticsResp>>(
        {
            url: API.GetRetailStatistics,
            params
        }
    );
}

export function getPurchaseStatistics(params: QueryPurchaseStatisticsReq) {
    return defHttp.post<BaseDataResp<PurchaseStatisticsResp>>(
        {
            url: API.GetPurchaseStatistics,
            params
        }
    );
}

export function getSalesStatistics(params: QuerySalesStatisticsReq) {
    return defHttp.post<BaseDataResp<SalesStatisticsResp>>(
        {
            url: API.GetSalesStatistics,
            params
        }
    );
}

export function getShipmentsDetail(params: QueryShipmentsDetailStatisticsReq) {
    return defHttp.post<BaseDataResp<ShipmentsDetailStatisticsResp>>(
        {
            url: API.GetShipmentsDetail,
            params
        }
    );
}

export function getStorageDetail(params: QueryStorageDetailStatisticsReq) {
    return defHttp.post<BaseDataResp<StorageDetailStatisticsResp>>(
        {
            url: API.GetStorageDetail,
            params
        }
    );
}

export function getRelatedPerson() {
    return defHttp.get<BaseDataResp<RelatedPersonResp>>(
        {
            url: API.GetRelatedPerson,
        }
    );
}

export function getShipmentsSummary(params: QueryShipmentsSummaryStatisticsReq) {
    return defHttp.post<BaseDataResp<ShipmentsSummaryStatisticsResp>>(
        {
            url: API.GetShipmentsSummary,
            params
        }
    );
}

export function getStorageSummary(params: QueryStorageSummaryStatisticsReq) {
    return defHttp.post<BaseDataResp<StorageSummaryStatisticsResp>>(
        {
            url: API.GetStorageSummary,
            params
        }
    );
}

export function getCustomerBill(params: QueryCustomerBillReq) {
    return defHttp.post<BaseDataResp<CustomerBillStatisticsResp>>(
        {
            url: API.GetCustomerBill,
            params
        }
    );
}

export function getCustomerBillDetail(params: QueryCustomerBillDetailReq) {
    return defHttp.post<BaseDataResp<CustomerBillDetailStatisticsResp>>(
        {
            url: API.GetCustomerBillDetail,
            params
        }
    );
}

export function getSupplierBill(params: QuerySupplierBillReq) {
    return defHttp.post<BaseDataResp<SupplierBillStatisticsResp>>(
        {
            url: API.GetSupplierBill,
            params
        }
    );
}

export function getSupplierBillDetail(params: QuerySupplierBillDetailReq) {
    return defHttp.post<BaseDataResp<SupplierBillDetailStatisticsResp>>(
        {
            url: API.GetSupplierBillDetail,
            params
        }
    );
}

export function exportProductStock(params: QueryProductStockReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.ExportProductStockData}`,
            params,
            responseType: "blob"
        }
    );
}