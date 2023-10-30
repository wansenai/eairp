/*
 * Copyright (C) 2023-2033 WanSen AI Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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