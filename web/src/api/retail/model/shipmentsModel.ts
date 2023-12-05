export interface FileData {
    id: number | string;
    uid: string;
    fileName: string;
    fileUrl: string;
    fileType: string;
    fileSize: number;
    status: string;
}

export interface ShipmentsData {
    warehouseId: number | string;
    barCode: string | number;
    productId: number | string;
    productName: string;
    productStandard: string;
    productUnit: string;
    stock: number;
    productNumber: number;
    unitPrice: number;
    amount: number;
}

export interface AddOrUpdateShipmentsReq {
    id: number | string | undefined;
    memberId: number | string;
    accountId: number | string;
    receiptDate: string;
    receiptNumber: string;
    receiptType: string;
    collectionAmount: number;
    receiptAmount: number;
    backAmount: number;
    remark: string;
    status: number;
    tableData: ShipmentsData[];
    fileDataList: FileData[];
}

export interface AddOrUpdateShipmentsResp {
    id: number | string | undefined;
    memberId: number | string;
    memberName: string;
    paymentType: string;
    accountId: number | string;
    accountName: string;
    receiptDate: string;
    receiptNumber: string;
    receiptType: string;
    collectionAmount: number;
    receiptAmount: number;
    backAmount: number;
    remark: string;
    status: number;
    tableData: ShipmentsData[];
    fileDataList: FileData[];
}

export interface QueryShipmentsReq {
    receiptNumber: string;
    productInfo: string;
    memberId: number | string;
    warehouseId: number | string;
    accountId: number | string;
    operatorId: number | string;
    status: number;
    remark: string;
}

export interface ShipmentsResp {
    id: number | string;
    memberName: string;
    receiptNumber: string;
    receiptDate: string;
    productInfo: string;
    operator: string;
    productNumber: number;
    totalPrice: number;
    collectionAmount: number;
    backAmount: string;
    status: number;
}