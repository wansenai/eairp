export interface MemberResp {
    id: number | string;
    memberNumber: string;
    memberName: string;
    phoneNumber: string;
    email: string;
    advancePayment: number;
    status: number;
    remark: string;
    sort: number;
    createTime: string;
}

export interface AddOrUpdateMemberReq {
    id: number | string | undefined;
    memberNumber: string;
    memberName: string;
    phoneNumber: string;
    email: string;
    advancePayment: number;
    status: number;
    remark: string;
    sort: number;
}

export interface QueryMemberReq {
    memberNumber: string;
    phoneNumber: string;
}