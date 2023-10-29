interface FileData {
    id: number | string;
    fileName: string;
    fileUrl: string;
    fileType: string;
    fileSize: number;
}

interface AdvanceChargeData {
    accountId: number | string;
    accountName: string;
    amount: number;
    remark: string;
}

interface AddOrUpdateAdvanceReq {
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

interface QueryAdvanceReq {
    receiptNumber: string;
    memberId: number | string;
    operatorId: number | string;
    financialPersonnelId: number | string;
    status: number;
    remark: string;
}

interface AdvanceChargeResp {
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

interface AdvanceChargeDetailResp {
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

export {
    AdvanceChargeData,
    AddOrUpdateAdvanceReq,
    QueryAdvanceReq,
    AdvanceChargeResp,
    AdvanceChargeDetailResp
}