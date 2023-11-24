import {FileData} from "@/api/financial/model/advanceModel";

export interface OtherStorageData {
    id: number | string;
    warehouseId: number | string;
    warehouseName: string;
    barCode: string;
    productId: number | string;
    productName: string;
    productModel: string;
    productUnit: string;
    productStandard: string;
    stock: number;
    productNumber: number;
    unitPrice: number;
    amount: number;
    remark: string;
}

export interface AddOrUpdateOtherStorageReq {
    id: number | undefined;
    supplierId: number;
    receiptNumber: string;
    receiptDate: string;
    remark: string;
    status: number;
    files: FileData[];
    tableData: OtherStorageData[];
}

export interface QueryOtherStorageReq {
    receiptNumber: string;
    productInfo: string;
    supplierId: number;
    warehouseId: number;
    operatorId: number;
    otherReceipt: string;
    status: number;
    remark: string;
}

export interface OtherStorageResp {
    id: string | undefined;
    supplierName: string;
    receiptNumber: string;
    productInfo: string;
    receiptDate: string;
    operator: string;
    productNumber: number;
    totalAmount: number;
    status: number;
}

export interface OtherStorageDetailResp {
    id: string | undefined;
    supplierId: string;
    supplierName: string;
    receiptNumber: string;
    receiptDate: string;
    remark: string;
    status: number;
    files: FileData[];
    tableData: OtherStorageData[];
}