import {defHttp} from '/@/utils/http/axios';
import { ErrorMessageMode } from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    AddOrUpdateExpenseReq,
    QueryExpenseReq,
    ExpenseResp,
    ExpenseDetailResp,
} from "@/api/financial/model/expenseModel";

enum API {
    PageList = '/financial/expense/pageList',
    AddOrUpdateAccount = '/financial/expense/addOrUpdate',
    DeleteBatch = '/financial/expense/deleteByIds',
    UpdateStatus = '/financial/expense/updateStatusByIds',
    GetDetail = '/financial/expense/getDetailById',
}

export function getExpensePageList(params: QueryExpenseReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<ExpenseResp>>(
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

export function addOrUpdateExpense(params: AddOrUpdateExpenseReq) {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdateAccount,
            params,
        },
    );
}

export function updateExpenseStatus(ids: number[], status: number) {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },
    );
}

export function deleteBatchExpense(ids: number[]) {
    return defHttp.put<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },
    );
}

export function getExpenseDetailById(id: number) {
    return defHttp.get<BaseDataResp<ExpenseDetailResp>>(
        {
            url: `${API.GetDetail}/${id}`
        },
    );
}