import {defHttp} from '/@/utils/http/axios';
import {ErrorMessageMode, SuccessMessageMode} from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    AddOrUpdateOtherStorageReq,
    QueryOtherStorageReq,
    OtherStorageResp,
    OtherStorageDetailResp,
} from "@/api/warehouse/model/storageModel";

enum API {
    PageList = '/warehouse/otherStorage/pageList',
    AddOrUpdateAccount = '/warehouse/otherStorage/addOrUpdate',
    DeleteBatch = '/warehouse/otherStorage/deleteByIds',
    UpdateStatus = '/warehouse/otherStorage/updateStatusByIds',
    GetDetail = '/warehouse/otherStorage/getDetailById',
    Export = '/warehouse/otherStorage/export',
    ExportDetail = '/warehouse/otherStorage/exportDetail',
}

export function getOtherStoragePageList(params: QueryOtherStorageReq) {
    return defHttp.post<BaseDataResp<OtherStorageResp>>(
        {
            url: API.PageList,
            params,
        }
    );
}

export function addOrUpdateOtherStorage(params: AddOrUpdateOtherStorageReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdateAccount,
            params,
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function updateOtherStorageStatus(ids: number[], status: number, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function deleteBatchOtherStorage(ids: number[], successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function getOtherStorageDetailById(id: number) {
    return defHttp.get<BaseDataResp<OtherStorageDetailResp>>(
        {
            url: `${API.GetDetail}/${id}`
        },
    );
}

export function exportOtherStorage(params: QueryOtherStorageReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.Export}`,
            params,
            responseType: "blob"
        }
    );
}

export function exportOtherStorageDetail(receiptNumber: string) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.ExportDetail}/${receiptNumber}`,
            responseType: "blob"
        }
    );
}