import {defHttp} from '/@/utils/http/axios';
import {ErrorMessageMode, SuccessMessageMode} from '/#/axios';
import {BaseDataResp} from "@/api/model/baseModel";
import {AddOrUpdateProductCategoryReq, ProductCategoryResp} from "@/api/product/model/productCategoryModel";

enum Api {
    List = '/product/category/list',
    addOrUpdate = '/product/category/addOrUpdate',
    deleteBatch = '/product/category/deleteBatch',
}

export function getCategoryList() {
    return defHttp.get<BaseDataResp<ProductCategoryResp>>(
        {
            url: Api.List,
        }
    );
}

export function addOrUpdateCategory(params: AddOrUpdateProductCategoryReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseDataResp<AddOrUpdateProductCategoryReq>>(
        {
            url: Api.addOrUpdate,
            params,
        },
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        },
    );
}

export function deleteCategory(ids: number[], successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseDataResp<string>>(
        {
            url: `${Api.deleteBatch}?ids=${ids}`,
        },
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        },
    );
}