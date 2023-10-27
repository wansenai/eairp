import {defHttp} from '/@/utils/http/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    AddProductReq,
    ProductInfoDetailResp,
    QueryProductReq,
    UpdateBatchProductInfoReq
} from "@/api/product/model/productModel";
import {ErrorMessageMode} from "#/axios";

enum Api {
    getBarCode = '/product/getBarCode',
    addProduct = '/product/addOrUpdateProduct',
    getProductInfo = '/product/getProductInfo',
    getProductInfoDetail = '/product/getProductInfoDetail',
    deleteProduct = '/product/deleteProduct',
    updateProductStatus = '/product/updateProductStatus',
    updateBatchProductInfo = '/product/updateBatchProductInfo',
}

export function getBarCode() {
    return defHttp.get<BaseDataResp<number>>(
        {
            url: Api.getBarCode,
        }
    );
}

export function addOrUpdateProduct(params: AddProductReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: Api.addProduct,
            params
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function getProductInfo(params: QueryProductReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: Api.getProductInfo,
            params
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function getProductInfoDetail(productId: number) {
    return defHttp.get<BaseDataResp<ProductInfoDetailResp>>(
        {
            url: `${Api.getProductInfoDetail}/${productId}`,
        }
    );
}

export function deleteProduct(productIds: number[], mode: ErrorMessageMode = 'notice') {
    return defHttp.delete<BaseResp>(
        {
            url: `${Api.deleteProduct}/${productIds}`,
        },{
            errorMessageMode: mode,
        }
    );
}

export function updateProductStatus(productIds: number[], status: number, mode: ErrorMessageMode = 'notice') {
    return defHttp.put<BaseResp>(
        {
            url: `${Api.updateProductStatus}/${productIds}/${status}`,
        },{
            errorMessageMode: mode,
        }
    );
}

export function updateBatchProductInfo(params: UpdateBatchProductInfoReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.put<BaseResp>(
        {
            url: Api.updateBatchProductInfo,
            params
        },{
            errorMessageMode: mode,
        }
    );
}