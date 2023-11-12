import {defHttp} from '/@/utils/http/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    QueryPurchaseOrderReq,
    AddOrUpdateReceiptReq,
    PurchaseDetailData
} from "@/api/purchase/model/orderModel"

enum API {
    PageList = '/purchase/order/pageList',
    AddOrUpdate = '/purchase/order/addOrUpdate',
    GetDetail = '/purchase/order/detail',
    UpdateStatus = '/purchase/order/updateStatus',
    Delete = '/purchase/order/delete',
}

export function getPurchaseOrderPageList(params: QueryPurchaseOrderReq) {
    return defHttp.get<BaseDataResp<PurchaseDetailData>>(
        {
            url: API.PageList,
            params,
        }
    );
}

export function addOrUpdatePurchaseOrder(params: AddOrUpdateReceiptReq) {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdate,
            params,
        }
    );
}

export function getPurchaseOrderDetail(id: string) {
    return defHttp.get<BaseDataResp<AddOrUpdateReceiptReq>>(
        {
            url: `${API.GetDetail}/${id}`,
        }
    );
}

export function updatePurchaseOrderStatus(ids: number[], status: number ) {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}/${ids}/${status}`,
        }
    );
}

export function deletePurchaseOrder(ids: number[]) {
    return defHttp.put<BaseResp>(
        {
            url: `${API.Delete}/${ids}`,
        }
    );
}