export interface AccountResp {
    id: number | string;
    accountNumber: string;
    accountName: string;
    initialAmount: number;
    currentAmount: number;
    isDefault: number;
    status: number;
    sort: number;
    remark: string;
    createTime: string;
}

export interface AddOrUpdateAccountReq {
    id: number | string | undefined;
    accountNumber: string;
    accountName: string;
    initialAmount: number;
    currentAmount: number;
    isDefault: number;
    status: number;
    sort: number;
    remark: string;
}

export interface QueryAccountReq {
    accountNumber: string;
    accountName: string;
}