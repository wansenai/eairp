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
    PurchaseStatisticsResp, QuerySalesStatisticsReq, SalesStatisticsResp
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