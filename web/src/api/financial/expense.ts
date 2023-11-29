import {defHttp} from '/@/utils/http/axios';
import {ErrorMessageMode, SuccessMessageMode} from '/#/axios';
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

export function getExpensePageList(params: QueryExpenseReq) {
    return defHttp.post<BaseDataResp<ExpenseResp>>(
        {
            url: API.PageList,
            params,
        }
    );
}

export function addOrUpdateExpense(params: AddOrUpdateExpenseReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
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

export function updateExpenseStatus(ids: number[], status: number, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function deleteBatchExpense(ids: number[], successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function getExpenseDetailById(id: number) {
    return defHttp.get<BaseDataResp<ExpenseDetailResp>>(
        {
            url: `${API.GetDetail}/${id}`
        },
    );
}