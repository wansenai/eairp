export interface QueryIncomeExpenseReq {
    name: string;
    type: string;
    remark: string;
}

export interface AddOrUpdateIncomeExpenseReq {
    id: number | string | undefined;
    name: string;
    type: string;
    remark: string;
    status: number;
    sort: number;
}

export interface IncomeExpenseResp {
    id: string;
    name: string;
    type: string;
    remark: string;
    status: number;
    sort: number;
    createTime: string;
}