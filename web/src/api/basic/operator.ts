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
    OperatorResp,
    AddOrUpdateOperatorReq,
    QueryOperatorReq
} from "@/api/basic/model/operatorModel";


enum API {
    PageList = '/basic/operator/pageList',
    AddOrUpdateOperator = '/basic/operator/addOrUpdate',
    DeleteBatch = '/basic/operator/delete',
    UpdateStatus = '/basic/operator/updateStatus',
    List = '/basic/operator/list',
}

export function getOperatorPageList(params: QueryOperatorReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<OperatorResp>>(
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

export function addOrUpdateOperator(params: AddOrUpdateOperatorReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdateOperator,
            params,
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function updateOperatorStatus(ids: number[], status: number, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function deleteBatchOperator(ids: number[], mode: ErrorMessageMode = 'notice') {
    return defHttp.delete<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function getOperatorList(type: string) {
    return defHttp.get<BaseDataResp<OperatorResp[]>>(
        {
            url: `${API.List}/${type}`
        },
    );
}