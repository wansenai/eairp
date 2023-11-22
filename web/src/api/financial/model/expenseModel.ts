import {FileData} from "@/api/financial/model/advanceModel";

export interface ExpenseData {
    accountId: number | string;
    accountName: string | undefined;
    amount: number;
    remark: string;
    incomeExpenseId: string | number;
    incomeExpenseAmount: number;
}

export interface AddOrUpdateExpenseReq {
    id: number | undefined;
    relatedPersonId: number;
    receiptDate: string;
    receiptNumber: string;
    financialPersonId: number;
    expenseAccountId: number;
    expenseAmount: number;
    remark: string;
    status: number;
    files: FileData[];
    tableData: ExpenseData[];
}

export interface QueryExpenseReq {
    receiptNumber: string;
    relatedPersonId: number;
    financialPersonId: number;
    accountId: number;
    status: number;
    remark: string;
}

export interface ExpenseResp {
    id: string | undefined;
    name: string;
    receiptNumber: string;
    receiptDate: string;
    financialPerson: string;
    expenseAccountName: string;
    expenseAmount: number;
    remark: string;
    status: number;
}

export interface ExpenseDetailResp {
    id: string | undefined;
    relatedPersonId: number;
    receiptDate: string;
    receiptNumber: string;
    financialPersonId: number;
    expenseAccountId: number;
    expenseAmount: number;
    remark: string;
    files: FileData[];
    tableData: ExpenseData[];
}