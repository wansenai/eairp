import {defHttp} from '/@/utils/http/axios';
import {ErrorMessageMode, SuccessMessageMode} from '/#/axios';
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

export function addOrUpdateUnit(params: AddOrUpdateProductUnitReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseResp>(
        {
            url: Api.AddOrUpdate,
            params,
        },
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function deleteBatchUnits(ids: number[], successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.delete<BaseResp>(
        {
            url: `${Api.DeleteBatch}?ids=${ids}`
        },
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function updateUnitStatus(params: {id: number, status: number}, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseResp>(
        {
            url: Api.UpdateStatus,
            params,
        },
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}