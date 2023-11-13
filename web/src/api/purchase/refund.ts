import {defHttp} from '/@/utils/http/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    QueryPurchaseRefundReq,
    AddOrUpdatePurchaseRefundReq,
    PurchaseRefundDetailData
} from "@/api/purchase/model/refundModel"

enum API {
    PageList = '/purchase/refund/pageList',
    AddOrUpdate = '/purchase/refund/addOrUpdate',
    GetDetail = '/purchase/refund/detail',
    UpdateStatus = '/purchase/refund/updateStatus',
    Delete = '/purchase/refund/delete',
    GetLinkRefundDetail = '/purchase/refund/getLinkRefundDetail',
}

export function getPurchaseRefundPageList(params: QueryPurchaseRefundReq) {
    return defHttp.get<BaseDataResp<PurchaseRefundDetailData>>(
        {
            url: API.PageList,
            params,
        }
    );
}

export function addOrUpdatePurchaseRefund(params: AddOrUpdatePurchaseRefundReq) {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdate,
            params,
        }
    );
}

export function getPurchaseRefundDetail(id: string) {
    return defHttp.get<BaseDataResp<AddOrUpdatePurchaseRefundReq>>(
        {
            url: `${API.GetDetail}/${id}`,
        }
    );
}

export function updatePurchaseRefundStatus(ids: number[], status: number ) {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}/${ids}/${status}`,
        }
    );
}

export function deletePurchaseRefund(ids: number[]) {
    return defHttp.put<BaseResp>(
        {
            url: `${API.Delete}/${ids}`,
        }
    );
}

export function getLinkRefundDetail(receiptNumber: string) {
    return defHttp.get<BaseDataResp<PurchaseRefundDetailData>>(
        {
            url: `${API.GetLinkRefundDetail}/${receiptNumber}`,
        }
    );
}