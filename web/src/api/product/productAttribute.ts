import {defHttp} from '/@/utils/http/axios';
import { ErrorMessageMode } from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {AddOrUpdateProductAttributeReq, ProductAttributeResp, ProductAttributeListReq} from "@/api/product/model/productAttributeModel";

enum Api {
    List = '/product/attribute/list',
    addOrUpdate = '/product/attribute/addOrUpdate',
    deleteBatch = '/product/attribute/deleteBatch',
    GetAttributeById = '/product/attribute/getValuesById',
}

export function getAttributeList(params: ProductAttributeListReq) {
    return defHttp.post<BaseDataResp<ProductAttributeResp>>(
        {
            url: Api.List,
            params,
        }
    );
}

export function addOrUpdateAttribute(params: AddOrUpdateProductAttributeReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: Api.addOrUpdate,
            params,
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function deleteBatchAttribute(ids: number[], mode: ErrorMessageMode = 'notice') {
    return defHttp.delete<BaseResp>(
        {
            url: `${Api.deleteBatch}?ids=${ids}`
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function getAttributeById(id: number, mode: ErrorMessageMode = 'notice') {
    return defHttp.get<BaseDataResp<ProductAttributeResp>>(
        {
            url: `${Api.GetAttributeById}?id=${id}`
        },
        {
            errorMessageMode: mode,
        },
    );
}