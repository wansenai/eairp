import {defHttp} from '/@/utils/http/axios';
import {ErrorMessageMode, SuccessMessageMode} from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    CustomerResp,
    AddOrUpdateCustomerReq,
    QueryCustomerReq
} from "@/api/basic/model/customerModel";


enum API {
    PageList = '/basic/customer/pageList',
    List = '/basic/customer/list',
    AddOrUpdateCustomer = '/basic/customer/addOrUpdate',
    DeleteBatch = '/basic/customer/deleteBatch',
    UpdateStatus = '/basic/customer/updateStatus',
    Export = '/basic/customer/export',
}

export function getCustomerPageList(params: QueryCustomerReq) {
    return defHttp.post<BaseDataResp<CustomerResp>>(
        {
            url: API.PageList,
            params,
        },
    );
}

export function getCustomerList() {
    return defHttp.get<BaseDataResp<CustomerResp>>(
        {
            url: API.List
        }
    );
}

export function addOrUpdateCustomer(params: AddOrUpdateCustomerReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdateCustomer,
            params,
        },
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        },
    );
}

export function updateCustomerStatus(ids: number[], status: number, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
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

export function deleteBatchCustomer(ids: number[], successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
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

export function exportCustomer(params: QueryCustomerReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.Export}`,
            params,
            responseType: "blob"
        }
    );
}