import {defHttp} from '/@/utils/http/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    AddProductReq,
    ProductInfoDetailResp,
    QueryProductReq,
    UpdateBatchProductInfoReq,
    ProductExtendPriceResp,
    QueryProductExtendPriceReq,
} from "@/api/product/model/productModel";
import {ErrorMessageMode, SuccessMessageMode} from "#/axios";

enum Api {
    getProductCode = '/product/getProductCode',
    addProduct = '/product/addOrUpdateProduct',
    getProductInfo = '/product/getProductInfo',
    getProductInfoDetail = '/product/getProductInfoDetail',
    deleteProduct = '/product/deleteProduct',
    updateProductStatus = '/product/updateProductStatus',
    updateBatchProductInfo = '/product/updateBatchProductInfo',
    getProductSku = '/product/sku/pageList',
    getProductSkuByBarCode = '/product/sku/getProduct',
}

export function getProductCode() {
    return defHttp.get<BaseDataResp<number>>(
        {
            url: Api.getProductCode,
        }
    );
}

export function addOrUpdateProduct(params: AddProductReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseResp>(
        {
            url: Api.addProduct,
            params
        },
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        },
    );
}

export function getProductInfo(params: QueryProductReq) {
    return defHttp.post<BaseResp>(
        {
            url: Api.getProductInfo,
            params
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

export function deleteProduct(productIds: number[], successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.delete<BaseResp>(
        {
            url: `${Api.deleteProduct}/${productIds}`,
        },
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        },
    );
}

export function updateProductStatus(productIds: number[], status: number, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${Api.updateProductStatus}/${productIds}/${status}`,
        },
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        },
    );
}

export function updateBatchProductInfo(params: UpdateBatchProductInfoReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: Api.updateBatchProductInfo,
            params
        },
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        },
    );
}

export function getProductSkuPage(params: QueryProductExtendPriceReq) {
    return defHttp.post<BaseResp>(
        {
            url: Api.getProductSku,
            params
        }
    );
}

export function getProductSkuByBarCode(barCode: number | string, warehouseId: number | string) {
    return defHttp.get<BaseDataResp<ProductExtendPriceResp>>(
        {
            url: `${Api.getProductSkuByBarCode}/${barCode}/${warehouseId}`,
        }
    );
}