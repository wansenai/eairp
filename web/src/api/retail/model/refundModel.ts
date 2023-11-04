import {ShipmentsData, FileDta} from './shipmentsModel';

interface AddOrUpdateRefundReq {
    id: number | string | undefined;
    memberId: number | string;
    accountId: number | string;
    receiptDate: string;
    receiptNumber: string;
    otherReceipt: string;
    paymentAmount: number;
    receiptAmount: number;
    backAmount: number;
    remark: string;
    status: number;
    tableData: ShipmentsData[];
    fileDataList: FileData[];
}

interface RefundResp {
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