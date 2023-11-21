import {defHttp} from '/@/utils/http/axios';
import { ErrorMessageMode } from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    QueryIncomeExpenseReq,
    AddOrUpdateIncomeExpenseReq,
    IncomeExpenseResp,
} from "@/api/basic/model/incomeExpenseModel";

enum API {
    PageList = '/basic/incomeExpense/pageList',
    List = '/basic/incomeExpense/list',
    AddOrUpdateIncomeExpense = '/basic/incomeExpense/addOrUpdate',
    DeleteBatch = '/basic/incomeExpense/deleteBatch',
    UpdateStatus = '/basic/incomeExpense/updateStatus',
}

export function getIncomeExpensePageList(params: QueryIncomeExpenseReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<IncomeExpenseResp>>(
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

export function getIncomeExpenseList(type: string) {
    return defHttp.get<BaseDataResp<IncomeExpenseResp>>(
        {
            url: `${API.List}/${type}`
        }
    );
}

export function addOrUpdateIncomeExpense(params: AddOrUpdateIncomeExpenseReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdateIncomeExpense,
            params,
        }
    );
}

export function deleteIncomeExpense(ids: number[], mode: ErrorMessageMode = 'notice') {
    return defHttp.delete<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function updateIncomeExpenseStatus(ids: number[], status: number, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },
        {
            errorMessageMode: mode,
        },
    );
}
