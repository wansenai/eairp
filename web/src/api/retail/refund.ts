import {defHttp} from '/@/utils/http/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import { QueryShipmentsReq } from "@/api/retail/model/shipmentsModel"
import {
    AddOrUpdateRefundReq, AddOrUpdateRefundResp,
    RefundResp
} from "@/api/retail/model/refundModel"
import {ErrorMessageMode, SuccessMessageMode} from "#/axios";

enum API {
    PageList = '/retail/refund/pageList',
    AddOrUpdate = '/retail/refund/addOrUpdate',
    GetDetail = '/retail/refund/detail',
    UpdateStatus = '/retail/refund/updateStatus',
    Delete = '/retail/refund/deleteByIds',
    GetLinkRefundDetail = '/retail/refund/getLinkRefundDetail',
    Export = '/retail/refund/export',
    ExportDetail = '/retail/refund/exportDetail',
}

export function getRefundPageList(params: QueryShipmentsReq) {
    return defHttp.post<BaseDataResp<RefundResp>>(
        {
            url: API.PageList,
            params,
        }
    );
}

export function addOrUpdateRefund(params: AddOrUpdateRefundReq,
                                  successMode: SuccessMessageMode = 'message',
                                  errorMode: ErrorMessageMode = 'message',) {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdate,
            params,
        },
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function getRefundDetail(id: string) {
    return defHttp.get<BaseDataResp<AddOrUpdateRefundReq>>(
        {
            url: `${API.GetDetail}/${id}`,
        }
    );
}

export function getLinkRefundDetail(otherReceipt: string) {
    return defHttp.get<BaseDataResp<AddOrUpdateRefundResp>>(
        {
            url: `${API.GetLinkRefundDetail}/${otherReceipt}`,
        }
    );
}

export function updateRefundStatus(ids: string[], status: number, successMode: SuccessMessageMode = 'message', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`,
        },
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function deleteRefund(ids: string[], successMode: SuccessMessageMode = 'message', errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseResp>(
        {
            url: `${API.Delete}?ids=${ids}`,
        },
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function exportRefund(params: QueryShipmentsReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.Export}`,
            params,
            responseType: "blob"
        }
    );
}

export function exportRefundDetail(receiptNumber: string) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.ExportDetail}/${receiptNumber}`,
            responseType: "blob"
        }
    );
}