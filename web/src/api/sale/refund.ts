import {defHttp} from '/@/utils/http/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    QuerySaleRefundReq,
    AddOrUpdateReceiptSaleRefundReq,
    SaleRefundDetailData,
    LinkReceiptSaleRefundDetailResp
} from "@/api/sale/model/refundModel"
import {ErrorMessageMode, SuccessMessageMode} from "#/axios";

enum API {
    PageList = '/sale/refund/pageList',
    AddOrUpdate = '/sale/refund/addOrUpdate',
    GetDetail = '/sale/refund/detail',
    UpdateStatus = '/sale/refund/updateStatus',
    Delete = '/sale/refund/delete',
    GetLinkRefundDetail = '/sale/refund/getLinkRefundDetail',
    Export = '/sale/refund/export',
    ExportDetail = '/sale/refund/exportDetail',
}

export function getSaleRefundPageList(params: QuerySaleRefundReq) {
    return defHttp.get<BaseDataResp<SaleRefundDetailData>>(
        {
            url: API.PageList,
            params,
        }
    );
}

export function addOrUpdateSaleRefund(params: AddOrUpdateReceiptSaleRefundReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
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

export function getSaleRefundDetail(id: string) {
    return defHttp.get<BaseDataResp<AddOrUpdateReceiptSaleRefundReq>>(
        {
            url: `${API.GetDetail}/${id}`,
        }
    );
}

export function updateSaleRefundStatus(ids: number[], status: number, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}/${ids}/${status}`,
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function deleteSaleRefund(ids: number[], successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
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
    return defHttp.get<BaseDataResp<LinkReceiptSaleRefundDetailResp>>(
        {
            url: `${API.GetLinkRefundDetail}/${receiptNumber}`,
        }
    );
}

export function exportRefund(params: QuerySaleRefundReq) {
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