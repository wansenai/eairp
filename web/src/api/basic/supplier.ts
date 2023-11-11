import {defHttp} from '/@/utils/http/axios';
import { ErrorMessageMode } from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    SupplierResp,
    AddSupplierReq,
    UpdateSupplierReq,
    QuerySupplierReq
} from "@/api/basic/model/supplierModel";


enum API {
    PageList = '/basic/supplier/pageList',
    List = '/basic/supplier/list',
    AddSupplier = '/basic/supplier/add',
    UpdateSupplier = '/basic/supplier/update',
    DeleteBatch = '/basic/supplier/deleteBatch',
    UpdateStatus = '/basic/supplier/updateStatus',
}

export function getSupplierPageList(params: QuerySupplierReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<SupplierResp>>(
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

export function getSupplierList() {
    return defHttp.get<BaseDataResp<SupplierResp>>(
        {
            url: API.List
        }
    );
}

export function addSupplier(params: AddSupplierReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: API.AddSupplier,
            params,
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function updateSupplier(params: UpdateSupplierReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: API.UpdateSupplier,
            params,
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function updateSupplierStatus(params: { ids: number[], status: number }, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: API.UpdateStatus,
            params,
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function deleteBatchSuppliers(ids: number[], mode: ErrorMessageMode = 'notice') {
    return defHttp.delete<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },
        {
            errorMessageMode: mode,
        },
    );
}