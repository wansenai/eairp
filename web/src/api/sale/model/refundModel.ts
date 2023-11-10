import {FileData} from '/@/api/retail/model/shipmentsModel';

export interface QuerySaleRefundReq {
    receiptNumber: string;
    productInfo: string;
    customerId: number | string;
    operatorId: number | string;
    otherReceipt: string;
    arrearsStatus: number;
    status: number;
    remark: string;
}

export interface SaleRefundTableData {
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

export interface AddOrUpdateReceiptSaleRefundReq {
    id: number | string | undefined;
    customerId: string;
    receiptNumber: string;
    receiptDate: string;
    otherReceipt: string;
    operatorIds: number[];
    refundOfferRate: number;
    refundOfferAmount: number;
    refundLastAmount: number;
    otherAmount: number;
    thisRefundAmount: number;
    thisArrearsAmount: number;
    accountId: number | string;
    multipleAccountIds: number[];
    multipleAccountAmounts: number[];
    status: number;
    remark: string;
    tableData: SaleRefundTableData[];
    files: FileData[];
}

export interface SaleRefundDetailData {
    customerId: number | string;
    receiptDate: string;
    receiptNumber: string;
    operatorIds: number[];
    refundOfferRate: number;
    refundOfferAmount: number;
    refundLastAmount: number;
    otherAmount: number;
    thisRefundAmount: number;
    thisArrearsAmount: number;
    accountIds: number;
    multipleAccountAmounts: number[];
    multipleAccountIds: number[];
    remark: string;
    tableData: SaleRefundTableData[];
    files: FileData[];
}