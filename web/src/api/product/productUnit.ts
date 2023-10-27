import {defHttp} from '/@/utils/http/axios';
import { ErrorMessageMode } from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {AddOrUpdateProductUnitReq, ProductUnitResp, ProductUnitQueryReq} from "@/api/product/model/productUnitModel";

enum Api {
    List = '/product/unit/list',
    AddOrUpdate = '/product/unit/addOrUpdate',
    DeleteBatch = '/product/unit/deleteBatch',
    UpdateStatus = '/product/unit/updateUnitStatus',
}

export function getUnitList(params: ProductUnitQueryReq) {
    return defHttp.post<BaseDataResp<ProductUnitResp>>(
        {
            url: Api.List,
            params,
        }
    );
}

export function addOrUpdateUnit(params: AddOrUpdateProductUnitReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: Api.AddOrUpdate,
            params,
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function deleteBatchUnits(ids: number[], mode: ErrorMessageMode = 'notice') {
    return defHttp.delete<BaseResp>(
        {
            url: `${Api.DeleteBatch}?ids=${ids}`
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function updateUnitStatus(params: {id: number, status: number}, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: Api.UpdateStatus,
            params,
        },
        {
            errorMessageMode: mode,
        },
    );
}