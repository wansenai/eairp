import {defHttp} from '/@/utils/http/axios';
import { ErrorMessageMode } from '/#/axios';
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
}

export function getIncomePageList(params: QueryIncomeReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<IncomeResp>>(
        {
            url: API.PageList,
            params,
        },
        {
            errorMessageMode: mode,
            successMessageMode: mode,
        },
    );
}

export function addOrUpdateIncome(params: AddOrUpdateIncomeReq) {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdateAccount,
            params,
        },
    );
}

export function updateIncomeStatus(ids: number[], status: number) {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },
    );
}

export function deleteBatchIncome(ids: number[]) {
    return defHttp.put<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },
    );
}

export function getIncomeDetailById(id: number) {
    return defHttp.get<BaseDataResp<IncomeDetailResp>>(
        {
            url: `${API.GetDetail}/${id}`
        },
    );
}