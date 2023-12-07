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

export interface ProductStockSkuReq {
    warehouseId: number | string;
    productInfo: string;
    productCategoryId: number | string;
    warehouseShelves: string;
    isExportDetail: boolean;
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
    stockList: ProductStockResp[];
}

export interface ProductStockSkuResp {
    id: string;
    productId: string;
    warehouseId: string;
    productBarcode: string;
    warehouseName: string;
    productName: string;
    productCategoryName: string;
    productStandard: string;
    productModel: string;
    productColor: string;
    productUnit: string;
    warehouseShelves: string;
    productWeight: number;
    unitPrice: number;
    retailPrice: number;
    salePrice: number;
    purchasePrice: number;
    initialStock: number;
    currentStock: number;
    stockAmount: number;
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

export interface QueryProductExtendPriceReq {
    productCategoryId: number | string;
    warehouseId: number | string;
    productName: string;
    enableSerialNumber: number;
    enableBatchNumber: number;
}

export interface ProductExtendPriceResp {
    id: number | string;
    productId: number | string;
    productCategoryId: number | string;
    warehouseId: number | string;
    barCode: string;
    productName: string;
    productStandard: string;
    productModel: string;
    productColor: string;
    productUnit: string;
    multiAttribute: string;
    stock: number;
    extendInfo: string;
    retailPrice: number;
}