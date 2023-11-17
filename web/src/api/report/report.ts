import {defHttp} from '/@/utils/http/axios';
import {BaseDataResp} from "@/api/model/baseModel";
import {
    ProductStockResp,
    QueryProductStockReq,
    RetailStatisticalResp,
    ProductStockFlowResp,
    QueryProductStockFlowReq
} from "@/api/report/reportModel";

enum API {
    getStatisticalData = '/report/homePage/statistics',
    getProductStockData = '/report/productStock',
    getProductStockFlowData = '/report/productStockFlow'
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
    console.info("productId", productId);
    console.info("params", params);
    return defHttp.post<BaseDataResp<ProductStockFlowResp>>(
        {
            url: API.getProductStockFlowData,
            params
        }
    );
}