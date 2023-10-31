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

import {defHttp} from '/@/utils/http/axios';
import { ErrorMessageMode } from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    MemberResp,
    AddOrUpdateMemberReq,
    QueryMemberReq
} from "@/api/basic/model/memberModel";


enum API {
    List = '/basic/member/list',
    PageList = '/basic/member/pageList',
    AddOrUpdateMember = '/basic/member/addOrUpdate',
    DeleteBatch = '/basic/member/deleteBatch',
    UpdateStatus = '/basic/member/updateStatus',
}

export function getMemberPageList(params: QueryMemberReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<MemberResp>>(
        {
            url: API.PageList,
            params,
        },
        {
            errorMessageMode: mode,
            successMessageMode: mode,
        },
    );
}

export function addOrUpdateMember(params: AddOrUpdateMemberReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdateMember,
            params,
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function updateMemberStatus(ids: number[], status: number, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function deleteBatchMember(ids: number[], mode: ErrorMessageMode = 'notice') {
    return defHttp.delete<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function getMemberList() {
    return defHttp.get<BaseDataResp<MemberResp>>(
        {
            url: API.List
        }
    );
}