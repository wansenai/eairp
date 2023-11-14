import {FileData} from '/@/api/retail/model/shipmentsModel';

export interface QueryPurchaseOrderReq {
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

export interface AddOrUpdateReceiptReq {
    id: number | string | undefined;
    supplierId: string;
    receiptNumber: string;
    receiptDate: string;
    discountRate: number;
    discountAmount: number;
    discountLastAmount: number;
    deposit: number;
    accountId: number | string;
    multipleAccountIds: number[];
    multipleAccountAmounts: number[];
    status: number;
    remark: string;
    tableData: PurchaseData[];
    files: FileData[];
}

export interface LinkReceiptDetailResp {
    id: number | string | undefined;
    supplierId: string;
    supplierName: string;
    receiptNumber: string;
    receiptDate: string;
    discountRate: number;
    discountAmount: number;
    discountLastAmount: number;
    deposit: number;
    accountId: number | string;
    accountName: string;
    multipleAccountIds: number[];
    multipleAccountAmounts: number[];
    status: number;
    remark: string;
    tableData: PurchaseData[];
    files: FileData[];
}

export interface PurchaseDetailData {
    supplierId: number | string;
    accountId: number;
    receiptDate: string;
    receiptNumber: string;
    multipleAccountAmounts: number[];
    multipleAccountIds: number[];
    discountRate: number;
    discountAmount: number;
    discountLastAmount: number;
    deposit: number;
    remark: string;
    tableData: PurchaseData[];
    files: FileData[];
}