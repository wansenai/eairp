export interface OperatorResp {
    id: number | string;
    name: string;
    type: string;
    status: number;
    sort: number;
    remark: string;
    createTime: string;
}

export interface AddOrUpdateOperatorReq {
    id: number | string | undefined;
    name: string;
    type: number;
    status: number;
    remark: string;
    sort: number;
}

export interface QueryOperatorReq {
    name: string;
    type: string;
}