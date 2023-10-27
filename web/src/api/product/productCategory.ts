import {defHttp} from '/@/utils/http/axios';
import { ErrorMessageMode } from '/#/axios';
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

export function addOrUpdateCategory(params: AddOrUpdateProductCategoryReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<AddOrUpdateProductCategoryReq>>(
        {
            url: Api.addOrUpdate,
            params,
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function deleteCategory(ids: number[], mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<string>>(
        {
            url: `${Api.deleteBatch}?ids=${ids}`,
        },
        {
            errorMessageMode: mode,
        },
    );
}