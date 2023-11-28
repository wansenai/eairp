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

export interface QueryShipmentsDetailStatisticsReq {
    receiptNumber: string;
    productInfo: string;
    relatedPersonId: number;
    warehouseId: number;
    operatorId: number;
    remark: string;
}

export interface ShipmentsDetailStatisticsResp {
    receiptNumber: string;
    productBarcode: string;
    productName: string;
    productStandard: string;
    productModel: string;
    productUnit: string;
    type: string;
    name: string;
    productNumber: number;
    unitPrice: number;
    amount: number;
    taxRate: number;
    taxAmount: number;
    createTime: string;
}

export interface QueryStorageDetailStatisticsReq {
    receiptNumber: string;
    productInfo: string;
    relatedPersonId: number;
    warehouseId: number;
    operatorId: number;
    remark: string;
}

export interface RelatedPersonResp {
    id: string;
    type: string;
    name: string;
}

export interface StorageDetailStatisticsResp {
    receiptNumber: string;
    productBarcode: string;
    productName: string;
    productStandard: string;
    productModel: string;
    productUnit: string;
    type: string;
    name: string;
    productNumber: number;
    unitPrice: number;
    amount: number;
    taxRate: number;
    taxAmount: number;
    createTime: string;
}

export interface QueryShipmentsSummaryStatisticsReq {
    productInfo: string;
    relatedPersonId: number;
    warehouseId: number;
}

export interface ShipmentsSummaryStatisticsResp {
    productBarcode: string;
    productName: string;
    warehouseName: string;
    productStandard: string;
    productCategoryName: string;
    productModel: string;
    productUnit: string;
    shipmentsNumber: number;
    shipmentsAmount: number;
}

export interface QueryStorageSummaryStatisticsReq {
    productInfo: string;
    relatedPersonId: number;
    warehouseId: number;
}

export interface StorageSummaryStatisticsResp {
    productBarcode: string;
    productName: string;
    warehouseName: string;
    productStandard: string;
    productCategoryName: string;
    productModel: string;
    productUnit: string;
    storageNumber: number;
    storageAmount: number;
}

export interface QueryCustomerBillReq {
    customerId: number | string;
}

export interface CustomerBillStatisticsResp {
    id: string;
    customerId: string;
    customerName: string;
    contactName: string;
    contactPhone: string;
    email: string;
    firstQuarterReceivable: number;
    secondQuarterReceivable: number;
    thirdQuarterReceivable: number;
    fourthQuarterReceivable: number;
    totalQuarterReceivable: number;
    totalQuarterArrears: number;
    remainingReceivableArrears: number;
}

export interface QueryCustomerBillDetailReq {
    receiptNumber: number | string;
    productInfo: string;
}

export interface CustomerBillDetailStatisticsResp {
    receiptNumber: string;
    customerName: string;
    productInfo: string;
    receiptDate: string;
    operator: string;
    thisReceiptArrears: number;
    receivedArrears: number;
    receivableArrears: number;
}

export interface QuerySupplierBillReq {
    supplierId: number | string;
}

export interface SupplierBillStatisticsResp {
    id: string;
    supplierId: string;
    supplierName: string;
    contactName: string;
    contactPhone: string;
    email: string;
    firstQuarterPayment: number;
    secondQuarterPayment: number;
    thirdQuarterPayment: number;
    fourthQuarterPayment: number;
    totalPayment: number;
    totalArrears: number;
    remainingPaymentArrears: number;
}

export interface QuerySupplierBillDetailReq {
    receiptNumber: number | string;
    productInfo: string;
}

export interface SupplierBillDetailStatisticsResp {
    receiptNumber: string;
    supplierName: string;
    productInfo: string;
    receiptDate: string;
    operator: string;
    thisReceiptArrears: number;
    prepaidArrears: number;
    paymentArrears: number;
}