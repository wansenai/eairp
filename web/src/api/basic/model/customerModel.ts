export interface CustomerResp {
    id: number | string;
    customerName: string;
    contact: string;
    phoneNumber: string;
    address: string;
    email: string;
    status: number;
    firstQuarterAccountReceivable: number;
    secondQuarterAccountReceivable: number;
    thirdQuarterAccountReceivable: number;
    fourthQuarterAccountReceivable: number;
    totalAccountReceivable: number;
    fax: string;
    taxNumber: string;
    bankName: string;
    accountNumber: string;
    taxRate: string;
    sort: number;
    remark: string;
    createTime: string;
}

export interface AddOrUpdateCustomerReq {
    id: number | string | undefined;
    customerName: string;
    contact: string;
    phoneNumber: string;
    address: string;
    email: string;
    status: number;
    firstQuarterAccountReceivable: number;
    secondQuarterAccountReceivable: number;
    thirdQuarterAccountReceivable: number;
    fourthQuarterAccountReceivable: number;
    fax: string;
    taxNumber: string;
    bankName: string;
    accountNumber: string;
    taxRate: string;
    sort: number;
    remark: string;
}

export interface QueryCustomerReq {
    customerName: string;
    contact: string;
    phoneNumber: string;
}