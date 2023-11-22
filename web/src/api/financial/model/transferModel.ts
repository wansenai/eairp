import {FileData} from "@/api/financial/model/advanceModel";

export interface TransferData {
    accountId: number | string;
    accountName: string | undefined;
    transferAmount: number;
    remark: string;
}

export interface AddOrUpdateTransferReq {
    id: number | undefined;
    receiptDate: string;
    receiptNumber: string;
    financialPersonId: number;
    paymentAccountId: number;
    paymentAmount: number;
    remark: string;
    status: number;
    files: FileData[];
    tableData: TransferData[];
}

export interface QueryTransferReq {
    receiptNumber: string;
    financialPersonId: number;
    accountId: number;
    status: number;
    remark: string;
}

export interface TransferResp {
    id: string | undefined;
    receiptNumber: string;
    receiptDate: string;
    financialPerson: string;
    paymentAccountName: string;
    paymentAmount: number;
    remark: string;
    status: number;
}

export interface TransferDetailResp {
    receiptDate: string;
    receiptNumber: string;
    financialPersonId: number;
    financialPersonName: string;
    paymentAccountId: number;
    paymentAccountName: string;
    paymentAmount: number;
    remark: string;
    status: number;
    files: FileData[];
    tableData: TransferData[];
}