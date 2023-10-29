import {defHttp} from '/@/utils/http/axios';
import { ErrorMessageMode } from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    AccountResp,
    AddOrUpdateAccountReq,
    QueryAccountReq
} from "@/api/financial/model/accountModel";


enum API {
    PageList = '/financial/account/pageList',
    AddOrUpdateAccount = '/financial/account/addOrUpdate',
    DeleteBatch = '/financial/account/delete',
    UpdateStatus = '/financial/account/updateStatus',
    List = '/financial/account/list',
}

export function getAccountPageList(params: QueryAccountReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<AccountResp>>(
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

export function addOrUpdateAccount(params: AddOrUpdateAccountReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdateAccount,
            params,
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function updateAccountStatus(ids: number[], status: number, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function deleteBatchAccount(ids: number[], mode: ErrorMessageMode = 'notice') {
    return defHttp.delete<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function getAccountList() {
    return defHttp.get<BaseDataResp<AccountResp>>(
        {
            url: API.List,
        }
    );
}