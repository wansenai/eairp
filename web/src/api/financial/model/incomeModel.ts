import {FileData} from "@/api/financial/model/advanceModel";

export interface IncomeData {
    accountId: number | string;
    accountName: string | undefined;
    amount: number;
    remark: string;
    incomeExpenseId: string | number;
    incomeExpenseAmount: number;
}

export interface AddOrUpdateIncomeReq {
    id: number | undefined;
    relatedPersonId: number;
    receiptDate: string;
    receiptNumber: string;
    financialPersonId: number;
    incomeAccountId: number;
    incomeAmount: number;
    remark: string;
    status: number;
    files: FileData[];
    tableData: IncomeData[];
}

export interface QueryIncomeReq {
    receiptNumber: string;
    relatedPersonId: number;
    financialPersonId: number;
    accountId: number;
    status: number;
    remark: string;
}

export interface IncomeResp {
    id: string | undefined;
    name: string;
    receiptNumber: string;
    receiptDate: string;
    financialPerson: string;
    incomeAccountName: string;
    incomeAmount: number;
    remark: string;
    status: number;
}

export interface IncomeDetailResp {
    id: string | undefined;
    relatedPersonId: number;
    receiptDate: string;
    receiptNumber: string;
    financialPersonId: number;
    incomeAccountId: number;
    incomeAmount: number;
    remark: string;
    files: FileData[];
    tableData: IncomeData[];
}