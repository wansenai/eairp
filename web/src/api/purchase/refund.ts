import {defHttp} from '/@/utils/http/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    QueryPurchaseRefundReq,
    AddOrUpdatePurchaseRefundReq,
    PurchaseRefundDetailData
} from "@/api/purchase/model/refundModel"
import {ErrorMessageMode, SuccessMessageMode} from "#/axios";

enum API {
    PageList = '/purchase/refund/pageList',
    AddOrUpdate = '/purchase/refund/addOrUpdate',
    GetDetail = '/purchase/refund/detail',
    UpdateStatus = '/purchase/refund/updateStatus',
    Delete = '/purchase/refund/delete',
    GetLinkRefundDetail = '/purchase/refund/getLinkRefundDetail',
    Export = '/purchase/refund/export',
}

export function getPurchaseRefundPageList(params: QueryPurchaseRefundReq) {
    return defHttp.get<BaseDataResp<PurchaseRefundDetailData>>(
        {
            url: API.PageList,
            params,
        }
    );
}

export function addOrUpdatePurchaseRefund(params: AddOrUpdatePurchaseRefundReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdate,
            params,
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
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

export function updatePurchaseRefundStatus(ids: number[], status: number, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}/${ids}/${status}`,
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function deletePurchaseRefund(ids: number[], successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.Delete}/${ids}`,
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
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

export function exportRefund(params: QueryPurchaseRefundReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.Export}`,
            params,
            responseType: "blob"
        }
    );
}