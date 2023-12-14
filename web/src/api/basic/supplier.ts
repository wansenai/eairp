import {defHttp} from '/@/utils/http/axios';
import {ErrorMessageMode, SuccessMessageMode} from '/#/axios';
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
    Export = '/basic/supplier/export',
}

export function getSupplierPageList(params: QuerySupplierReq) {
    return defHttp.post<BaseDataResp<SupplierResp>>(
        {
            url: API.PageList,
            params,
        }
    );
}

export function getSupplierList() {
    return defHttp.get<BaseDataResp<SupplierResp>>(
        {
            url: API.List
        }
    );
}

export function addSupplier(params: AddSupplierReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseResp>(
        {
            url: API.AddSupplier,
            params,
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function updateSupplier(params: UpdateSupplierReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseResp>(
        {
            url: API.UpdateSupplier,
            params,
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function updateSupplierStatus(params: { ids: number[], status: number }, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseResp>(
        {
            url: API.UpdateStatus,
            params,
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function deleteBatchSuppliers(ids: number[], successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.delete<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function exportSupplier(params: QuerySupplierReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.Export}`,
            params,
            responseType: "blob"
        }
    );
}