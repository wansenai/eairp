import {FileData} from '/@/api/retail/model/shipmentsModel';

export interface QueryPurchaseStorageReq {
    receiptNumber: string;
    productInfo: string;
    supplierId: number | string;
    operatorId: number | string;
    status: number;
    remark: string;
}

export interface PurchaseData {
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

export interface AddOrUpdatePurchaseStorageReq {
    id: number | string | undefined;
    supplierId: string;
    receiptNumber: string;
    receiptDate: string;
    paymentRate: number;
    paymentAmount: number;
    paymentLastAmount: number;
    otherAmount: number;
    thisPaymentAmount: number;
    thisArrearsAmount: number;
    accountId: number | string;
    multipleAccountIds: number[];
    multipleAccountAmounts: number[];
    status: number;
    remark: string;
    tableData: PurchaseData[];
    files: FileData[];
}

export interface PurchaseStorageDetailData {
    supplierId: number | string;
    accountId: number;
    receiptDate: string;
    receiptNumber: string;
    multipleAccountAmounts: number[];
    multipleAccountIds: number[];
    paymentRate: number;
    paymentAmount: number;
    paymentLastAmount: number;
    otherAmount: number;
    thisPaymentAmount: number;
    thisArrearsAmount: number;
    remark: string;
    tableData: PurchaseData[];
    files: FileData[];
}