export interface FileData {
    id: number | string;
    uid: number | string;
    fileName: string;
    fileUrl: string;
    fileType: string;
    fileSize: number;
}

export interface AdvanceChargeData {
    accountId: number | string;
    accountName: string;
    amount: number;
    remark: string;
}

export interface AddOrUpdateAdvanceReq {
    id: number | string | undefined;
    memberId: number | string;
    receiptDate: string;
    receiptNumber: string;
    financialPersonnelId: number | string;
    totalAmount: number;
    collectedAmount: number;
    remark: string;
    tableData: AdvanceChargeData[];
    fileDataList: FileData[];
    review: number;
}

export interface QueryAdvanceReq {
    receiptNumber: string;
    memberId: number | string;
    operatorId: number | string;
    financialPersonnelId: number | string;
    status: number;
    remark: string;
}

export interface AdvanceChargeResp {
    id: number | string;
    memberName: string;
    receiptNumber: string;
    receiptDate: string;
    operator: string;
    financialPersonnel: string;
    totalAmount: number;
    collectedAmount: number;
    status: number;
    remark: string;
}

export interface AdvanceChargeDetailResp {
    memberId: string;
    memberName: string;
    receiptNumber: string;
    receiptDate: string;
    financialPersonnel: string;
    financialPersonnelId: string;
    remark: string;
    totalAmount: number;
    collectedAmount: number;
    tableData: AdvanceChargeData[];
    files: FileData[];
}