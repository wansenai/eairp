
export interface Column {
    title: string;
    key: string;
    type: string;
    placeholder: string;
    validateRules?: { required: boolean; message: string }[];
    readonly?: boolean;
    defaultValue?: string | number;
}

export interface ProductAttributeModel {
    id: number | string;
    attributeName: string;
    attributeValue: string;
    remark: string;
    sort: number;
    disabled: boolean;
}

export interface ProductImageModel {
    productImageId: string;
    imageName: string;
    imageUrl: string;
}

export interface ProductPriceModel {
    key: number|string;
    productPriceId: string;
    barCode: number;
    productUnit: string;
    multiAttribute: string;
    purchasePrice: number;
    retailPrice: number;
    salesPrice: number;
    lowSalesPrice: number;
}

export interface ProductStockModel {
    key: number|string;
    productStockId: number | string;
    id: number | string;
    warehouseName: string;
    initStockQuantity: number;
    lowStockQuantity: number;
    highStockQuantity: number;
}

export interface MeTable {
    loading: boolean;
    dataSource: ProductPriceModel[];
    columns: Column[];
}

export interface Stock {
    loading: boolean;
    dataSource: ProductStockModel[];
    columns: Column[];
}

export interface ProductInfo {
    mfrs: string;
    otherField1: string;
    otherField2: string;
    otherField3: string;
}

export interface FormState {
    productId: number | string | undefined;
    productName: string | undefined;
    productStandard: string | undefined;
    productModel: string | undefined;
    productUnit: string | undefined;
    productUnitId: string | undefined;
    productColor: string | undefined;
    productWeight: number | undefined;
    productExpiryNum: number | undefined;
    productCategoryId: string | undefined;
    enableSerialNumber: number | undefined;
    enableBatchNumber: number | undefined;
    warehouseShelves: string | undefined;
    productManufacturer: string | undefined;
    otherFieldOne: string | undefined;
    otherFieldTwo: string | undefined;
    otherFieldThree: string | undefined;
    remark: string | undefined;
}

export interface Unit {
    id: number;
    computeUnit: string
    basicUnit: string;
    otherUnit: string;
    ratio: number;
    otherUnitTwo: string;
    ratioTwo: number;
    otherUnitThree: string;
    ratioThree: number;
}