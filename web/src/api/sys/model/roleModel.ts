export interface GetRoleInfoModel {
    // 角色id
    id: number | string,
    // 角色名称
    roleName: string,
    // 角色类型
    type: string,
    // 价格屏蔽 1-屏蔽采购价 2-屏蔽零售价 3-屏蔽销售价
    priceLimit: number,
    // 状态 0-启用 1-停用
    status: number,
    // 描述
    description: string,
    // 创建时间
    createTime: string
}

export interface queryRoleListReq {
    roleName: string;
    status: number;
}

export interface addOrUpdateRoleInfoReq {
    id: string | undefined
    roleName: string,
    type: string,
    priceLimit: number,
    status: number,
    description: string,
}

export interface addOrUpdateRolePermissionReq {
    id: string | undefined,
    menuIds: number[]
}