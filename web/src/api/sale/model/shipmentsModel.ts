import {FileData} from '/@/api/retail/model/shipmentsModel';

export interface QuerySaleShipmentsReq {
    receiptNumber: string;
    productInfo: string;
    customerId: number | string;
    operatorId: number | string;
    otherReceipt: string;
    arrearsStatus: number;
    status: number;
    remark: string;
}

export interface tableData {
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

export interface AddOrUpdateReceiptSaleShipmentsReq {
    id: number | string | undefined;
    customerId: string;
    receiptNumber: string;
    receiptDate: string;
    otherReceipt: string;
    operatorIds: number[];
    collectOfferRate: number;
    collectOfferAmount: number;
    collectOfferLastAmount: number;
    otherAmount: number;
    thisCollectAmount: number;
    thisArrearsAmount: number;
    accountId: number | string;
    multipleAccountIds: number[];
    multipleAccountAmounts: number[];
    status: number;
    remark: string;
    tableData: tableData[];
    files: FileData[];
}

export interface SaleShipmentDetailData {
    customerId: number | string;
    receiptDate: string;
    receiptNumber: string;
    operatorIds: number[];
    collectOfferRate: number;
    collectOfferAmount: number;
    collectOfferLastAmount: number;
    otherAmount: number;
    thisCollectAmount: number;
    thisArrearsAmount: number;
    accountIds: number;
    multipleAccountAmounts: number[];
    multipleAccountIds: number[];
    remark: string;
    tableData: tableData[];
    files: FileData[];
}