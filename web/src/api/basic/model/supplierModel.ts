export interface SupplierResp {
    id: number | string;
    supplierName: string;
    contact: string;
    contactNumber: string;
    phoneNumber: string;
    address: string;
    email: string;
    status: number;
    firstQuarterAccountReceivable: number;
    secondQuarterAccountReceivable: number;
    thirdQuarterAccountReceivable: number;
    fourthQuarterAccountReceivable: number;
    totalAccountReceivable: number;
    firstQuarterAccountPayment: number;
    secondQuarterAccountPayment: number;
    thirdQuarterAccountPayment: number;
    fourthQuarterAccountPayment: number;
    totalAccountPayment: number;
    fax: string;
    taxNumber: string;
    bankName: string;
    accountNumber: string;
    taxRate: string;
    sort: number;
    remark: string;
    createTime: string;
}

export interface AddSupplierReq {
    supplierName: string;
    contact: string;
    contactNumber: string;
    phoneNumber: string;
    address: string;
    email: string;
    status: number;
    firstQuarterAccountReceivable: number;
    secondQuarterAccountReceivable: number;
    thirdQuarterAccountReceivable: number;
    fourthQuarterAccountReceivable: number;
    firstQuarterAccountPayment: number;
    secondQuarterAccountPayment: number;
    thirdQuarterAccountPayment: number;
    fourthQuarterAccountPayment: number;
    fax: string;
    taxNumber: string;
    bankName: string;
    accountNumber: string;
    taxRate: string;
    sort: number;
    remark: string;
}

export interface UpdateSupplierReq {
    id: number | string;
    supplierName: string;
    contact: string;
    contactNumber: string;
    phoneNumber: string;
    address: string;
    email: string;
    status: number;
    firstQuarterAccountReceivable: number;
    secondQuarterAccountReceivable: number;
    thirdQuarterAccountReceivable: number;
    fourthQuarterAccountReceivable: number;
    totalAccountReceivable: number;
    firstQuarterAccountPayment: number;
    secondQuarterAccountPayment: number;
    thirdQuarterAccountPayment: number;
    fourthQuarterAccountPayment: number;
    totalAccountPayment: number;
    fax: string;
    taxNumber: string;
    bankName: string;
    accountNumber: string;
    taxRate: string;
    sort: number;
    remark: string;
}

export interface QuerySupplierReq {
    supplierName: string;
    contact: string;
    contactNumber: string;
    phoneNumber: string;
}