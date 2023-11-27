import {FileData} from "@/api/financial/model/advanceModel";

export interface AssembleStockData {
    id: number | string;
    type: string;
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

export interface AddOrUpdateAssembleReq {
    id: number | undefined;
    receiptNumber: string;
    receiptDate: string;
    remark: string;
    status: number;
    files: FileData[];
    tableData: AssembleStockData[];
}

export interface QueryAssembleReq {
    receiptNumber: string;
    productInfo: string;
    operatorId: number;
    warehouseId: number;
    otherReceipt: string;
    status: number;
    remark: string;
}

export interface AssembleResp {
    id: string | undefined;
    receiptNumber: string;
    productInfo: string;
    receiptDate: string;
    operator: string;
    productNumber: number;
    totalAmount: number;
    status: number;
}

export interface AssembleDetailResp {
    id: string | undefined;
    receiptNumber: string;
    receiptDate: string;
    remark: string;
    status: number;
    files: FileData[];
    tableData: AssembleStockData[];
}