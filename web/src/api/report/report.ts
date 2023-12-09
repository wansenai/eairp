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
    ExportProductStockData = '/report/productStock/export',
    ExportAccountStatistics = '/report/accountStatistics/export',
    ExportRetailStatistics = '/report/retailStatistics/export',
    ExportPurchaseStatistics = '/report/purchaseStatistics/export',
    ExportSalesStatistics = '/report/salesStatistics/export',
    ExportShipmentsDetail = '/report/shipmentsDetail/export',
    ExportStorageDetail = '/report/storageDetail/export',
    ExportShipmentsSummary = '/report/shipmentsSummary/export',
    ExportStorageSummary = '/report/storageSummary/export',
    ExportCustomerBill = '/report/customerBill/export',
    ExportSupplierBill = '/report/supplierBill/export',
    ExportProductStockFlowData = '/report/productStockFlow/export',
    ExportCustomerBillDetail = '/report/customerBillDetail/export',
    ExportSupplierBillDetail = '/report/supplierBillDetail/export',
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

export function exportAccountStatistics(params: QueryAccountStatisticsReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.ExportAccountStatistics}`,
            params,
            responseType: "blob"
        }
    );
}

export function exportRetailStatistics(params: QueryRetailStatisticsReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.ExportRetailStatistics}`,
            params,
            responseType: "blob"
        }
    );
}

export function exportPurchaseStatistics(params: QueryPurchaseStatisticsReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.ExportPurchaseStatistics}`,
            params,
            responseType: "blob"
        }
    );
}

export function exportSalesStatistics(params: QuerySalesStatisticsReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.ExportSalesStatistics}`,
            params,
            responseType: "blob"
        }
    );
}

export function exportShipmentsDetail(params: QueryShipmentsDetailStatisticsReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.ExportShipmentsDetail}`,
            params,
            responseType: "blob"
        }
    );
}

export function exportStorageDetail(params: QueryStorageDetailStatisticsReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.ExportStorageDetail}`,
            params,
            responseType: "blob"
        }
    );
}

export function exportShipmentsSummary(params: QueryShipmentsSummaryStatisticsReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.ExportShipmentsSummary}`,
            params,
            responseType: "blob"
        }
    );
}

export function exportStorageSummary(params: QueryStorageSummaryStatisticsReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.ExportStorageSummary}`,
            params,
            responseType: "blob"
        }
    );
}

export function exportCustomerBill(params: QueryCustomerBillReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.ExportCustomerBill}`,
            params,
            responseType: "blob"
        }
    );
}

export function exportSupplierBill(params: QuerySupplierBillReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.ExportSupplierBill}`,
            params,
            responseType: "blob"
        }
    );
}

export function exportStockFlow(params: QueryProductStockFlowReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.ExportProductStockFlowData}`,
            params,
            responseType: "blob"
        }
    );
}

export function exportCustomerBillDetail(params: QueryCustomerBillDetailReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.ExportCustomerBillDetail}`,
            params,
            responseType: "blob"
        }
    );
}

export function exportSupplierBillDetail(params: QuerySupplierBillDetailReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.ExportSupplierBillDetail}`,
            params,
            responseType: "blob"
        }
    );
}