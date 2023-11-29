import {defHttp} from '/@/utils/http/axios';
import {ErrorMessageMode, SuccessMessageMode} from '/#/axios';
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

export function getAccountPageList(params: QueryAccountReq) {
    return defHttp.post<BaseDataResp<AccountResp>>(
        {
            url: API.PageList,
            params,
        }
    );
}

export function addOrUpdateAccount(params: AddOrUpdateAccountReq, mode: ErrorMessageMode = 'notice', successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
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

export function updateAccountStatus(ids: number[], status: number, mode: ErrorMessageMode = 'notice', successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function deleteBatchAccount(ids: number[], mode: ErrorMessageMode = 'notice', successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.delete<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function getAccountList() {
    return defHttp.get<BaseDataResp<AccountResp>>(
        {
            url: API.List,
        }
    );
}