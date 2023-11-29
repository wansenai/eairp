import {defHttp} from '/@/utils/http/axios';
import {ErrorMessageMode, SuccessMessageMode} from '/#/axios';
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

export function getIncomeExpensePageList(params: QueryIncomeExpenseReq) {
    return defHttp.post<BaseDataResp<IncomeExpenseResp>>(
        {
            url: API.PageList,
            params,
        }
    );
}

export function getIncomeExpenseList(type: string) {
    return defHttp.get<BaseDataResp<IncomeExpenseResp>>(
        {
            url: `${API.List}/${type}`
        }
    );
}

export function addOrUpdateIncomeExpense(params: AddOrUpdateIncomeExpenseReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdateIncomeExpense,
            params,
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function deleteIncomeExpense(ids: number[], successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.delete<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        },
    );
}

export function updateIncomeExpenseStatus(ids: number[], status: number, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        },
    );
}
