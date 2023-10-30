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

import {ErrorMessageMode} from "#/axios";
import {defHttp} from "@/utils/http/axios";
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    addOrUpdateRoleInfoReq,
    addOrUpdateRolePermissionReq,
    GetRoleInfoModel,
    queryRoleListReq
} from "@/api/sys/model/roleModel";

enum Api {
    List = '/sysRole/list',
    PageList = '/sysRole/PageList',
    UpdateStatus = '/sysRole/updateStatus',
    AddOrUpdateRole = '/sysRole/addOrUpdateRole',
    DeleteRole = '/sysRole/deleteRole',
    RolePermission = '/sysRole/permission'
}

export function getRoleList(mode: ErrorMessageMode = 'notice') {
    return defHttp.get<BaseResp>(
        {url: Api.List},
        {
            errorMessageMode: mode,
            successMessageMode: mode,
        }
    )
}

export function getPageList(params: queryRoleListReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<GetRoleInfoModel>>(
        {url: Api.PageList, params},
        {
            errorMessageMode: mode,
            successMessageMode: mode,
        }
    )
}

export function setRoleStatus(id: string, status: number, mode: ErrorMessageMode = 'notice'){
    return defHttp.post<BaseResp>(
        {url: `${Api.UpdateStatus}?id=${id}&status=${status}`},
        {
            errorMessageMode: mode,
            successMessageMode: mode,
        },
    )
}

export function addOrUpdateRole(params: addOrUpdateRoleInfoReq, mode: ErrorMessageMode = 'notice'){
    return defHttp.post<BaseResp>(
        {url: Api.AddOrUpdateRole, params: params},
        {
            errorMessageMode: mode,
            successMessageMode: mode,
        }
    )
}

export function deleteRole(id: string, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {url: `${Api.DeleteRole}?id=${id}`},
        {
            errorMessageMode: mode,
            successMessageMode: mode,
        }
    )
}

export function rolePermission(params: addOrUpdateRolePermissionReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {url: Api.RolePermission, params: params},
        {
            errorMessageMode: mode,
            successMessageMode: mode,
        }
    )
}