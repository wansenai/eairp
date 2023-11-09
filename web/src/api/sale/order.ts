import {defHttp} from '/@/utils/http/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    QuerySaleOrderReq,
    AddOrUpdateReceiptReq,
    SaleDetailData
} from "@/api/sale/model/orderModel"

enum API {
    PageList = '/sale/order/pageList',
    AddOrUpdate = '/sale/order/addOrUpdate',
    GetDetail = '/sale/order/detail',
    UpdateStatus = '/sale/order/updateStatus',
    Delete = '/sale/order/delete',
}

export function getSaleOrderPageList(params: QuerySaleOrderReq) {
    return defHttp.get<BaseDataResp<SaleDetailData>>(
        {
            url: API.PageList,
            params,
        }
    );
}

export function addOrUpdateSaleOrder(params: AddOrUpdateReceiptReq) {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdate,
            params,
        }
    );
}

export function getSaleOrderDetail(id: string) {
    return defHttp.get<BaseDataResp<AddOrUpdateReceiptReq>>(
        {
            url: `${API.GetDetail}/${id}`,
        }
    );
}

export function updateSaleOrderStatus(ids: number[], status: number ) {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}/${ids}/${status}`,
        }
    );
}

export function deleteSaleOrder(ids: number[]) {
    return defHttp.put<BaseResp>(
        {
            url: `${API.Delete}/${ids}`,
        }
    );
}