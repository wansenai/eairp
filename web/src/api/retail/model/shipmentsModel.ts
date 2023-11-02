interface FileData {
    id: number | string;
    fileName: string;
    fileUrl: string;
    fileType: string;
    fileSize: number;
}

interface ShipmentsData {
    warehouseId: number | string;
    barcode: string;
    productId: number | string;
    productNumber: number;
    unitPrice: number;
    amount: number;
    remark: string;
}

interface AddOrUpdateShipmentsReq {
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
    tableData: ShipmentsData[];
    fileDataList: FileData[];
}

interface QueryShipmentsReq {
    receiptNumber: string;
    productInfo: string;
    memberId: number | string;
    warehouseId: number | string;
    accountId: number | string;
    operatorId: number | string;
    status: number;
    remark: string;
}

interface ShipmentsResp {
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