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

export interface QueryProductStockFlowReq {
    productId: number;
    warehouseId: number;
    productBarcode: number;
    receiptNumber: string;
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

export interface ProductStockFlowResp {
    receiptNumber: string;
    type: String;
    productBarcode: number;
    productName: string;
    warehouseName: string;
    productNumber: number;
    receiptDate: string;
}

export interface QueryAccountStatisticsReq {
    accountName: string;
    accountNumber: string;
}

export interface AccountStatisticsResp {
    accountId: string;
    accountName: string;
    accountNumber: string;
    initialAmount: number;
    thisMonthChangeAmount: number;
    currentAmount: number;
}

export interface AccountFlowResp {
    receiptNumber: string;
    subType: string;
    useType: string;
    name: string;
    amount: number;
    balance: number;
    receiptDate: string;
}

export interface QueryRetailStatisticsReq {
    productExtendInfo: string;
    memberId: number;
    warehouseId: number;
}

export interface RetailStatisticsResp {
    productBarcode: string;
    productName: string;
    productStandard: string;
    productModel: string;
    productExtendInfo: string;
    productUnit: string;
    retailNumber: number;
    retailAmount: number;
    retailRefundNumber: number;
    retailRefundAmount: number;
    retailLastAmount: number;
}

export interface QueryPurchaseStatisticsReq {
    productExtendInfo: string;
    supplierId: number;
    warehouseId: number;
}

export interface PurchaseStatisticsResp {
    productBarcode: string;
    productName: string;
    productStandard: string;
    productModel: string;
    productExtendInfo: string;
    productUnit: string;
    purchaseNumber: number;
    purchaseAmount: number;
    purchaseRefundNumber: number;
    purchaseRefundAmount: number;
    purchaseLastAmount: number;
}

export interface QuerySalesStatisticsReq {
    productExtendInfo: string;
    customerId: number;
    warehouseId: number;
}

export interface SalesStatisticsResp {
    productBarcode: string;
    productName: string;
    productStandard: string;
    productModel: string;
    productExtendInfo: string;
    productUnit: string;
    salesNumber: number;
    salesAmount: number;
    salesRefundNumber: number;
    salesRefundAmount: number;
    salesLastAmount: number;
}