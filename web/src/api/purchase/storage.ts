import {defHttp} from '/@/utils/http/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    QueryPurchaseStorageReq,
    AddOrUpdatePurchaseStorageReq,
    PurchaseStorageDetailData
} from "@/api/purchase/model/storageModel"
import {ErrorMessageMode, SuccessMessageMode} from "#/axios";

enum API {
    PageList = '/purchase/storage/pageList',
    AddOrUpdate = '/purchase/storage/addOrUpdate',
    GetDetail = '/purchase/storage/detail',
    UpdateStatus = '/purchase/storage/updateStatus',
    Delete = '/purchase/storage/delete',
    GetLinkStorageDetail = '/purchase/storage/getLinkStorageDetail',
    Export = '/purchase/storage/export',
}

export function getPurchaseStoragePageList(params: QueryPurchaseStorageReq) {
    return defHttp.get<BaseDataResp<PurchaseStorageDetailData>>(
        {
            url: API.PageList,
            params,
        }
    );
}

export function addOrUpdatePurchaseStorage(params: AddOrUpdatePurchaseStorageReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
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

export function getPurchaseStorageDetail(id: string) {
    return defHttp.get<BaseDataResp<AddOrUpdatePurchaseStorageReq>>(
        {
            url: `${API.GetDetail}/${id}`,
        }
    );
}

export function updatePurchaseStorageStatus(ids: number[], status: number, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}/${ids}/${status}`,
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function deletePurchaseStorage(ids: number[], successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.Delete}/${ids}`,
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function getLinkStorageDetail(receiptNumber: string) {
    return defHttp.get<BaseDataResp<PurchaseStorageDetailData>>(
        {
            url: `${API.GetLinkStorageDetail}/${receiptNumber}`,
        }
    );
}

export function exportStorage(params: QueryPurchaseStorageReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.Export}`,
            params,
            responseType: "blob"
        }
    );
}