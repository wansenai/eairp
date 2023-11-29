import {defHttp} from '/@/utils/http/axios';
import {ErrorMessageMode, SuccessMessageMode} from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    WarehouseResp,
    AddOrUpdateWarehouseReq,
    QueryWarehouseReq
} from "@/api/basic/model/warehouseModel";


enum API {
    PageList = '/basic/warehouse/pageList',
    AddOrUpdateWarehouse = '/basic/warehouse/addOrUpdate',
    DeleteBatch = '/basic/warehouse/delete',
    UpdateStatus = '/basic/warehouse/updateStatus',
    GetWarehouse = '/basic/warehouse/getWarehouse',
    List = '/basic/warehouse/list',
    GetDefaultWarehouse = '/basic/warehouse/getDefaultWarehouse',
}

export function getWarehousePageList(params: QueryWarehouseReq) {
    return defHttp.post<BaseDataResp<WarehouseResp>>(
        {
            url: API.PageList,
            params,
        }
    );
}

export function addOrUpdateWarehouse(params: AddOrUpdateWarehouseReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdateWarehouse,
            params,
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function updateWarehouseStatus(ids: number[], status: number, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function deleteBatchWarehouse(ids: number[], successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.delete<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function getWarehouse() {
    return defHttp.get<BaseDataResp<WarehouseResp>>(
        {
            url: API.GetWarehouse,
        }
    );
}

export function getWarehouseList() {
    return defHttp.get<BaseDataResp<WarehouseResp[]>>(
        {
            url: API.List,
        }
    );
}

export function getDefaultWarehouse() {
    return defHttp.get<BaseDataResp<WarehouseResp>>(
        {
            url: API.GetDefaultWarehouse,
        }
    );
}