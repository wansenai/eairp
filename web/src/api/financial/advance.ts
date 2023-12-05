import {defHttp} from '/@/utils/http/axios';
import {ErrorMessageMode, SuccessMessageMode} from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    AdvanceChargeResp,
    AddOrUpdateAdvanceReq,
    QueryAdvanceReq, AdvanceChargeDetailResp,
} from "@/api/financial/model/advanceModel";

enum API {
    PageList = '/financial/advance-charge/pageList',
    AddOrUpdateAccount = '/financial/advance-charge/addOrUpdate',
    DeleteBatch = '/financial/advance-charge/deleteByIds',
    UpdateStatus = '/financial/advance-charge/updateStatusByIds',
    GetDetail = '/financial/advance-charge/getDetailById',
    Export = '/financial/advance-charge/export',
    ExportDetail = '/financial/advance-charge/exportDetail',
}

export function getAdvancePageList(params: QueryAdvanceReq, errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseDataResp<AdvanceChargeResp>>(
        {
            url: API.PageList,
            params,
        },
        {
            errorMessageMode: errorMode,
        },
    );
}

export function addOrUpdateAdvance(params: AddOrUpdateAdvanceReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
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

export function updateAdvanceStatus(ids: number[] | string[], status: number, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        },
    );
}

export function deleteBatchAdvance(ids: number[], successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        },
    );
}

export function getAdvanceDetail(id: number | string, mode: ErrorMessageMode = 'notice') {
    return defHttp.get<BaseDataResp<AdvanceChargeDetailResp>>(
        {
            url: `${API.GetDetail}/${id}`
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function exportAdvance(params: QueryAdvanceReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.Export}`,
            params,
            responseType: "blob"
        }
    );
}

export function exportAdvanceDetail(receiptNumber: string) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.ExportDetail}/${receiptNumber}`,
            responseType: "blob"
        }
    );
}