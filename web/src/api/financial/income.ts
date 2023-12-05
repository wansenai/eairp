import {defHttp} from '/@/utils/http/axios';
import {ErrorMessageMode, SuccessMessageMode} from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    IncomeDetailResp,
    AddOrUpdateIncomeReq,
    QueryIncomeReq, IncomeResp,
} from "@/api/financial/model/incomeModel";

enum API {
    PageList = '/financial/income/pageList',
    AddOrUpdateAccount = '/financial/income/addOrUpdate',
    DeleteBatch = '/financial/income/deleteByIds',
    UpdateStatus = '/financial/income/updateStatusByIds',
    GetDetail = '/financial/income/getDetailById',
    Export = '/financial/income/export',
    ExportDetail = '/financial/income/exportDetail',
}

export function getIncomePageList(params: QueryIncomeReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<IncomeResp>>(
        {
            url: API.PageList,
            params,
        }
    );
}

export function addOrUpdateIncome(params: AddOrUpdateIncomeReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
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

export function updateIncomeStatus(ids: number[], status: number, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function deleteBatchIncome(ids: number[], successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function getIncomeDetailById(id: number) {
    return defHttp.get<BaseDataResp<IncomeDetailResp>>(
        {
            url: `${API.GetDetail}/${id}`
        },
    );
}

export function exportIncome(params: QueryIncomeReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.Export}`,
            params,
            responseType: "blob"
        }
    );
}

export function exportIncomeDetail(receiptNumber: string) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.ExportDetail}/${receiptNumber}`,
            responseType: "blob"
        }
    );
}