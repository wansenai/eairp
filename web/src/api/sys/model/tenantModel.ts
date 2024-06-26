export interface TenantInfoModel {
    id: number;
    tenantId: number;
    tenantName: number;
    userNumLimit: number;
    type: number;
    status: number;
    remark: string;
    createTime: string;
}

export interface queryTenantListReq {
    loginUser: string;
    tenantName: string;
    type: number;
    status: number;
}

export interface addOrUpdateTenantReq {
    id: number | string;
    username: string;
    password: string;
    type: number;
    status: number;
    userNumLimit: number;
    tenantName: string;
    expireTime: string;
    email: string;
    phoneNumber: string;
    roleId: number;
    deptId: number;
    remark: string;
}