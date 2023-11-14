import {FileData} from '/@/api/retail/model/shipmentsModel';

export interface QueryPurchaseRefundReq {
    receiptNumber: string;
    productInfo: string;
    supplierId: number | string;
    operatorId: number | string;
    status: number;
    remark: string;
}

export interface PurchaseRefundData {
    warehouseId: number | string;
    productId: number | string;
    barCode: number | string;
    productName: string;
    productUnit: string;
    productStandard: string;
    stock: number;
    productNumber: number;
    unitPrice: number;
    amount: number;
    taxRate: number;
    taxAmount: number;
    taxTotalPrice: number;
    remark: string;
}

export interface AddOrUpdatePurchaseRefundReq {
    id: number | string | undefined;
    supplierId: string;
    receiptNumber: string;
    receiptDate: string;
    refundOfferRate: number;
    refundOfferAmount: number;
    refundLastAmount: number;
    otherAmount: number;
    otherReceipt: string;
    thisRefundAmount: number;
    thisArrearsAmount: number;
    accountId: number | string;
    multipleAccountIds: number[];
    multipleAccountAmounts: number[];
    status: number;
    remark: string;
    tableData: PurchaseRefundData[];
    files: FileData[];
}

export interface PurchaseRefundDetailData {
    supplierId: number | string;
    supplierName: string;
    accountId: number;
    accountName: string;
    otherReceipt: string;
    receiptDate: string;
    receiptNumber: string;
    multipleAccountAmounts: number[];
    multipleAccountIds: number[];
    refundOfferRate: number;
    refundOfferAmount: number;
    refundLastAmount: number;
    otherAmount: number;
    thisRefundAmount: number;
    thisArrearsAmount: number;
    remark: string;
    tableData: PurchaseRefundData[];
    files: FileData[];
}