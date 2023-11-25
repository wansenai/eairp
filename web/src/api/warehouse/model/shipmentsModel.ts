import {FileData} from "@/api/financial/model/advanceModel";

export interface OtherShipmentsData {
    id: number | string;
    customerId: number | string;
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

export interface AddOrUpdateOtherShipmentsReq {
    id: number | undefined;
    customerId: number;
    receiptNumber: string;
    receiptDate: string;
    remark: string;
    status: number;
    files: FileData[];
    tableData: OtherShipmentsData[];
}

export interface QueryOtherShipmentsReq {
    receiptNumber: string;
    productInfo: string;
    customerId: number;
    warehouseId: number;
    operatorId: number;
    otherReceipt: string;
    status: number;
    remark: string;
}

export interface OtherShipmentsResp {
    id: string | undefined;
    customerName: string;
    receiptNumber: string;
    productInfo: string;
    receiptDate: string;
    operator: string;
    productNumber: number;
    totalAmount: number;
    status: number;
}

export interface OtherShipmentsDetailResp {
    id: string | undefined;
    customerId: string;
    customerName: string;
    receiptNumber: string;
    receiptDate: string;
    remark: string;
    status: number;
    files: FileData[];
    tableData: OtherShipmentsData[];
}