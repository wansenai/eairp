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

import {
    addOrUpdateDeptReq,
    DeptListItem,
    GetDeptInfoModel,
} from './model/dpetModel';

import { defHttp } from '/@/utils/http/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {ErrorMessageMode} from "#/axios";

enum Api {
    DeptList = '/dept/list',
    UserBindDept = '/dept/userBindDept',
    AddOrUpdateDept = '/dept/addOrUpdate',
    DeleteDept = '/dept/delete',
}
export function getDeptList(params?: DeptListItem) {
    return defHttp.get<BaseDataResp<GetDeptInfoModel>>({url: Api.DeptList, params})
}
export function getUserBindDept() {
    return defHttp.get<BaseDataResp<GetDeptInfoModel>>({url: Api.UserBindDept})
}

export function addOrUpdateDept(params: addOrUpdateDeptReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>({
            url: Api.AddOrUpdateDept, params},
        {
            errorMessageMode: mode,
            successMessageMode: mode,
        }
    )
}

export function deleteDept(id: number | string, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>({
            url: `${Api.DeleteDept}?id=${id}`},
        {
            errorMessageMode: mode,
            successMessageMode: mode,
        }
    )
}