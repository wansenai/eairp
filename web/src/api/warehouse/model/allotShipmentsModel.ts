import {FileData} from "@/api/financial/model/advanceModel";

export interface AllotShipmentsData {
    id: number | string;
    warehouseId: number | string;
    warehouseName: string;
    otherWarehouseId: number | string;
    otherWarehouseName: string;
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

export interface AddOrUpdateAllotShipmentsReq {
    id: number | undefined;
    receiptNumber: string;
    receiptDate: string;
    remark: string;
    status: number;
    files: FileData[];
    tableData: AllotShipmentsData[];
}

export interface QueryAllotShipmentsReq {
    receiptNumber: string;
    productInfo: string;
    operatorId: number;
    otherReceipt: string;
    status: number;
    remark: string;
}

export interface AllotShipmentsResp {
    id: string | undefined;
    receiptNumber: string;
    productInfo: string;
    receiptDate: string;
    operator: string;
    productNumber: number;
    totalAmount: number;
    status: number;
}

export interface AllotShipmentsDetailResp {
    id: string | undefined;
    receiptNumber: string;
    receiptDate: string;
    remark: string;
    status: number;
    files: FileData[];
    tableData: AllotShipmentsData[];
}