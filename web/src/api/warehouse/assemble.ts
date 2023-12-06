import {defHttp} from '/@/utils/http/axios';
import {ErrorMessageMode, SuccessMessageMode} from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    AddOrUpdateAssembleReq,
    QueryAssembleReq,
    AssembleResp,
    AssembleDetailResp,
} from "@/api/warehouse/model/assembleModel";

enum API {
    PageList = '/warehouse/assemble/pageList',
    AddOrUpdateAccount = '/warehouse/assemble/addOrUpdate',
    DeleteBatch = '/warehouse/assemble/deleteByIds',
    UpdateStatus = '/warehouse/assemble/updateStatusByIds',
    GetDetail = '/warehouse/assemble/getDetailById',
    Export = '/warehouse/assemble/export',
    ExportDetail = '/warehouse/assemble/exportDetail',
}

export function getAssemblePageList(params: QueryAssembleReq) {
    return defHttp.post<BaseDataResp<AssembleResp>>(
        {
            url: API.PageList,
            params,
        }
    );
}

export function addOrUpdateAssemble(params: AddOrUpdateAssembleReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
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

export function updateAssembleStatus(ids: number[], status: number, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function deleteBatchAssemble(ids: number[], successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function getAssembleDetailById(id: number) {
    return defHttp.get<BaseDataResp<AssembleDetailResp>>(
        {
            url: `${API.GetDetail}/${id}`
        },
    );
}

export function exportAssemble(params: QueryAssembleReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.Export}`,
            params,
            responseType: "blob"
        }
    );
}

export function exportAssembleDetail(receiptNumber: string) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.ExportDetail}/${receiptNumber}`,
            responseType: "blob"
        }
    );
}