export interface RetailStatisticalResp {
    todaySales: number;
    yesterdaySales: number;
    todayRetailSales: number;
    yesterdayRetailSales: number;
    todayPurchase: number;
    yesterdayPurchase: number;
    monthSales: number;
    monthRetailSales: number;
    monthPurchase: number;
    yearSales: number;
    yearRetailSales: number;
    yearPurchase: number;
}

export interface QueryProductStockReq {
    warehouseId: number | string;
    productInfo: string;
    productCategoryId: number | string;
    warehouseShelves: string;
}

export interface ProductStockResp {
    id: string;
    productBarcode: string;
    productName: string;
    productCategoryName: string;
    productStandard: string;
    productModel: string;
    productColor: string;
    productUnit: string;
    warehouseShelves: string;
    productWeight: number;
    unitPrice: number;
    initialStock: number;
    currentStock: number;
    stockAmount: number;
}