import {defHttp} from '/@/utils/http/axios';
import {ErrorMessageMode, SuccessMessageMode} from '/#/axios';
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

export function addOrUpdateAttribute(params: AddOrUpdateProductAttributeReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseResp>(
        {
            url: Api.addOrUpdate,
            params,
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function deleteBatchAttribute(ids: number[], successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.delete<BaseResp>(
        {
            url: `${Api.deleteBatch}?ids=${ids}`
        },
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        },
    );
}

export function getAttributeById(id: number) {
    return defHttp.get<BaseDataResp<ProductAttributeResp>>(
        {
            url: `${Api.GetAttributeById}?id=${id}`
        }
    );
}