import {FileData} from "@/api/financial/model/advanceModel";

export interface PaymentData {
    paymentId: number | string;
    purchaseReceiptNumber: string | undefined;
    paymentArrears: number;
    prepaidArrears: number;
    thisPaymentAmount: number;
    remark: string;
}

export interface AddOrUpdatePaymentReq {
    id: number | undefined;
    supplierId: number;
    receiptDate: string;
    receiptNumber: string;
    financialPersonId: number;
    paymentAccountId: number;
    totalPaymentAmount: number;
    discountAmount: number;
    actualPaymentAmount: number;
    remark: string;
    status: number;
    files: FileData[];
    tableData: PaymentData[];
}

export interface QueryPaymentReq {
    receiptNumber: string;
    financialPersonId: number;
    purchaseReceiptNumber: string;
    supplierId: number;
    accountId: number;
    status: number;
    remark: string;
}

export interface PaymentResp {
    id: string | undefined;
    supplierName: string;
    receiptNumber: string;
    receiptDate: string;
    financialPerson: string;
    paymentAccountName: string;
    totalPaymentAmount: number;
    discountAmount: number;
    actualPaymentAmount: number;
    remark: string;
    status: number;
}

export interface PaymentDetailResp {
    id: string;
    supplierId: number;
    supplierName: string;
    receiptDate: string;
    receiptNumber: string;
    financialPersonId: number;
    financialPersonName: string;
    paymentAccountId: number;
    paymentAccountName: string;
    totalPaymentAmount: number;
    discountAmount: number;
    actualPaymentAmount: number;
    remark: string;
    status: number;
    files: FileData[];
    tableData: PaymentData[];
}

export interface QueryPaymentArrearsReq {
    supplierId: number;
    receiptNumber: string;
    productInfo: string;
}

export interface PaymentArrearsResp {
    id: string;
    supplierName: string;
    receiptNumber: string;
    receiptDate: string;
    productInfo: string;
    operatorName: string;
    thisReceiptArrears: number;
    prepaidArrears: number;
    paymentArrears: number;
}