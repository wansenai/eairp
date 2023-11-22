import {FileData} from "@/api/financial/model/advanceModel";

export interface CollectionData {
    collectionId: number | string;
    saleReceiptNumber: string | undefined;
    receivableArrears: number;
    receivedArrears: number;
    thisCollectionAmount: number;
    remark: string;
}

export interface AddOrUpdateCollectionReq {
    id: number | undefined;
    customerId: number;
    receiptDate: string;
    receiptNumber: string;
    financialPersonId: number;
    collectionAccountId: number;
    totalCollectionAmount: number;
    discountAmount: number;
    actualCollectionAmount: number;
    remark: string;
    status: number;
    files: FileData[];
    tableData: CollectionData[];
}

export interface QueryCollectionReq {
    receiptNumber: string;
    financialPersonId: number;
    saleReceiptNumber: string;
    customerId: number;
    accountId: number;
    status: number;
    remark: string;
}

export interface CollectionResp {
    id: string | undefined;
    customerName: string;
    receiptNumber: string;
    receiptDate: string;
    financialPerson: string;
    collectionAccountName: string;
    totalCollectionAmount: number;
    discountAmount: number;
    actualCollectionAmount: number;
    remark: string;
    status: number;
}

export interface CollectionDetailResp {
    customerId: number;
    customerName: string;
    receiptDate: string;
    receiptNumber: string;
    financialPersonId: number;
    financialPersonName: string;
    collectionAccountId: number;
    collectionAccountName: string;
    totalCollectionAmount: number;
    discountAmount: number;
    actualCollectionAmount: number;
    remark: string;
    status: number;
    files: FileData[];
    tableData: CollectionData[];
}