/*
 * Copyright (C) 2023-2033 WanSen AI Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

export interface AddProductStockReq {
    productStockId: number | string;
    warehouseId: number | string;
    warehouseName: string;
    initStockQuantity: number;
    lowStockQuantity: number;
    highStockQuantity: number;
}

export interface AddProductPriceReq {
    productPriceId: number | string;
    barCode: number;
    productUnit: string;
    multiAttribute: string;
    purchasePrice: number;
    retailPrice: number;
    salesPrice: number;
    lowSalesPrice: number;
}

export interface AddProductImageReq {
    productImageId: number | string | null;
    uid: string | null;
    type: string | null | undefined;
    status: string | null | undefined;
    imageName: string | null;
    imageUrl: string | null;
    imageSize: number | null | undefined;
}

export interface AddProductReq {
    productId: number | string;
    productName: string;
    productStandard: string;
    productModel: string
    productUnit: string
    productUnitId: number | string;
    productColor: string
    productWeight: number | string;
    productExpiryNum: number | string;
    productCategoryId: number | string;
    enableSerialNumber: number | string;
    enableBatchNumber: number | string;
    warehouseShelves: string
    remark: string
    productManufacturer: string
    otherFieldOne: string
    otherFieldTwo: string
    otherFieldThree: string
    priceList: AddProductPriceReq[];
    stockList: AddProductStockReq[];
    imageList: AddProductImageReq[];
}

export interface QueryProductReq {
    productCategoryId: number | string
    keywords: string
    productColor: string
    extendInfo: string
    remark: string
    warehouseShelves: string
    status: number
    enableSerialNumber: number
    enableBatchNumber: number
}

export interface ProductInfoDetailResp {
    productId: string;
    productCategoryId: string;
    productUnitId: string;
    productUnit: string;
    productName: string;
    productStandard: string;
    productModel: string;
    productColor: string;
    productWeight: number;
    productExpiryNum: number;
    productCategoryName: string;
    enableSerialNumber: number;
    enableBatchNumber: number;
    warehouseShelves: string;
    productManufacturer: string;
    otherFieldOne: string;
    otherFieldTwo: string;
    otherFieldThree: string;
    remark: string;
    priceList: ProductPriceResp[];
    stockList: ProductStockResp[];
    imageList: ProductImageResp[];
}

export interface ProductImageResp {
    productImageId: string;
    imageName: string;
    imageUrl: string;
}

export interface ProductPriceResp {
    productPriceId: string;
    barCode: number;
    productUnit: string;
    multiAttribute: string;
    purchasePrice: number;
    retailPrice: number;
    salesPrice: number;
    lowSalesPrice: number;
}

export interface ProductStockResp {
    productStockId: number | string;
    warehouseId: number | string;
    warehouseName: string;
    initStockQuantity: number;
    lowStockQuantity: number;
    highStockQuantity: number;
}

export interface UpdateBatchProductInfoReq {
    productIds: number[];
    productCategoryId: number | string;
    productColor: string;
    productWeight: number;
    productExpiryNum: number;
    enableSerialNumber: string;
    enableBatchNumber: string;
    remark: string;
}