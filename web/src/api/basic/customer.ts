import {defHttp} from '/@/utils/http/axios';
import { ErrorMessageMode } from '/#/axios';
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
}

export function getCustomerPageList(params: QueryCustomerReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<CustomerResp>>(
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

export function getCustomerList() {
    return defHttp.get<BaseDataResp<CustomerResp>>(
        {
            url: API.List
        }
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