import {ShipmentsData, FileData} from '/@/api/retail/model/shipmentsModel';

export interface AddOrUpdateRefundReq {
    id: number | string | undefined;
    memberId: string;
    accountId: string;
    receiptDate: string;
    receiptNumber: string;
    otherReceipt: string;
    paymentAmount: number;
    receiptAmount: number;
    backAmount: number;
    remark: string;
    status: number;
    tableData: ShipmentsData[];
    files: FileData[];
}

export interface AddOrUpdateRefundResp {
    id: number | string | undefined;
    memberId: string;
    memberName: string;
    accountId: string;
    accountName: string;
    receiptDate: string;
    receiptNumber: string;
    otherReceipt: string;
    paymentAmount: number;
    receiptAmount: number;
    backAmount: number;
    remark: string;
    status: number;
    tableData: ShipmentsData[];
    files: FileData[];
}

export interface RefundResp {
    id: number | string;
    memberName: string;
    receiptNumber: string;
    receiptDate: string;
    productInfo: string;
    operator: string;
    productNumber: number;
    totalPrice: number;
    paymentAmount: number;
    backAmount: string;
    status: number;
}