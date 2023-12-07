import {FileData} from '/@/api/retail/model/shipmentsModel';

export interface QuerySaleOrderReq {
    receiptNumber: string;
    productInfo: string;
    customerId: number | string;
    operatorId: number | string;
    status: number;
    remark: string;
}

export interface SalesData {
    warehouseId: number | string;
    productId: number | string;
    barCode: number | string;
    productCode: string;
    productName: string;
    productUnit: string;
    productStandard: string;
    stock: number;
    productNumber: number;
    salePrice: number;
    amount: number;
    taxRate: number;
    taxAmount: number;
    taxTotalPrice: number;
    remark: string;
}

export interface AddOrUpdateReceiptReq {
    id: number | string | undefined;
    customerId: string;
    receiptNumber: string;
    receiptDate: string;
    operatorIds: number[];
    discountRate: number;
    discountAmount: number;
    discountLastAmount: number;
    deposit: number;
    accountId: number | string;
    multipleAccountIds: number[];
    multipleAccountAmounts: number[];
    status: number;
    remark: string;
    tableData: SalesData[];
    files: FileData[];
}

export interface LinkReceiptSaleOrderDetailResp {
    id: number | string | undefined;
    customerId: string;
    customerName: string;
    accountName: string;
    receiptNumber: string;
    receiptDate: string;
    operatorIds: number[];
    discountRate: number;
    discountAmount: number;
    discountLastAmount: number;
    deposit: number;
    accountId: number | string;
    multipleAccountIds: number[];
    multipleAccountAmounts: number[];
    status: number;
    remark: string;
    tableData: SalesData[];
    files: FileData[];
}

export interface SaleDetailData {
    customerId: number | string;
    receiptDate: string;
    receiptNumber: string;
    operatorIds: number[];
    discountRate: number;
    discountAmount: number;
    discountLastAmount: number;
    deposit: number;
    accountIds: number;
    remark: string;
    tableData: SalesData[];
    files: FileData[];
}