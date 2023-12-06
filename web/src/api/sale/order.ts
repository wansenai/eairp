import {defHttp} from '/@/utils/http/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    QuerySaleOrderReq,
    AddOrUpdateReceiptReq,
    SaleDetailData,
    LinkReceiptSaleOrderDetailResp
} from "@/api/sale/model/orderModel"
import {ErrorMessageMode, SuccessMessageMode} from "#/axios";

enum API {
    PageList = '/sale/order/pageList',
    AddOrUpdate = '/sale/order/addOrUpdate',
    GetDetail = '/sale/order/detail',
    UpdateStatus = '/sale/order/updateStatus',
    Delete = '/sale/order/delete',
    GetLinkOrderDetail = '/sale/order/getLinkOrderDetail',
    Export = '/sale/order/export',
    ExportDetail = '/sale/order/exportDetail',
}

export function getSaleOrderPageList(params: QuerySaleOrderReq) {
    return defHttp.get<BaseDataResp<SaleDetailData>>(
        {
            url: API.PageList,
            params,
        }
    );
}

export function addOrUpdateSaleOrder(params: AddOrUpdateReceiptReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
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

export function getSaleOrderDetail(id: string) {
    return defHttp.get<BaseDataResp<AddOrUpdateReceiptReq>>(
        {
            url: `${API.GetDetail}/${id}`,
        }
    );
}

export function updateSaleOrderStatus(ids: number[], status: number, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}/${ids}/${status}`,
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function deleteSaleOrder(ids: number[], successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.Delete}/${ids}`,
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function getLinkOrderDetail(receiptNumber: string) {
    return defHttp.get<BaseDataResp<LinkReceiptSaleOrderDetailResp>>(
        {
            url: `${API.GetLinkOrderDetail}/${receiptNumber}`,
        }
    );
}

export function exportOrder(params: QuerySaleOrderReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.Export}`,
            params,
            responseType: "blob"
        }
    );
}

export function exportOrderDetail(receiptNumber: string) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.ExportDetail}/${receiptNumber}`,
            responseType: "blob"
        }
    );
}