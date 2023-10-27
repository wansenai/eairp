import {defHttp} from '/@/utils/http/axios';
import { ErrorMessageMode } from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    CustomerResp,
    AddOrUpdateCustomerReq,
    QueryCustomerReq
} from "@/api/basic/model/customerModel";


enum API {
    List = '/basic/customer/list',
    AddOrUpdateCustomer = '/basic/customer/addOrUpdate',
    DeleteBatch = '/basic/customer/deleteBatch',
    UpdateStatus = '/basic/customer/updateStatus',
}

export function getCustomerList(params: QueryCustomerReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<CustomerResp>>(
        {
            url: API.List,
            params,
        },
        {
            errorMessageMode: mode,
            successMessageMode: mode,
        },
    );
}

export function addOrUpdateCustomer(params: AddOrUpdateCustomerReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdateCustomer,
            params,
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function updateCustomerStatus(ids: number[], status: number, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function deleteBatchCustomer(ids: number[], mode: ErrorMessageMode = 'notice') {
    return defHttp.delete<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },
        {
            errorMessageMode: mode,
        },
    );
}