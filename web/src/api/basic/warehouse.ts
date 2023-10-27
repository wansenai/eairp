import {defHttp} from '/@/utils/http/axios';
import { ErrorMessageMode } from '/#/axios';
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
}

export function getWarehousePageList(params: QueryWarehouseReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<WarehouseResp>>(
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

export function addOrUpdateWarehouse(params: AddOrUpdateWarehouseReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdateWarehouse,
            params,
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function updateWarehouseStatus(ids: number[], status: number, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function deleteBatchWarehouse(ids: number[], mode: ErrorMessageMode = 'notice') {
    return defHttp.delete<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function getWarehouse() {
    return defHttp.get<BaseDataResp<WarehouseResp>>(
        {
            url: API.GetWarehouse,
        }
    );
}