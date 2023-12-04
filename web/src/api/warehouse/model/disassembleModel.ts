import {FileData} from "@/api/financial/model/advanceModel";

export interface DisAssembleStockData {
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

export interface AddOrUpdateDisAssembleReq {
    id: number | undefined;
    receiptNumber: string;
    receiptDate: string;
    remark: string;
    status: number;
    files: FileData[];
    tableData: DisAssembleStockData[];
}

export interface QueryDisAssembleReq {
    receiptNumber: string;
    productInfo: string;
    operatorId: number;
    warehouseId: number;
    otherReceipt: string;
    status: number;
    remark: string;
}

export interface ExportDisAssembleReq {
    receiptNumber: string;
    productInfo: string;
    operatorId: number;
    warehouseId: number;
    otherReceipt: string;
    status: number;
    remark: string;
    isExportDetail: boolean;
}

export interface DisAssembleResp {
    id: string | undefined;
    receiptNumber: string;
    productInfo: string;
    receiptDate: string;
    operator: string;
    productNumber: number;
    totalAmount: number;
    status: number;
}

export interface DisAssembleDetailResp {
    id: string | undefined;
    receiptNumber: string;
    receiptDate: string;
    remark: string;
    status: number;
    files: FileData[];
    tableData: DisAssembleStockData[];
}