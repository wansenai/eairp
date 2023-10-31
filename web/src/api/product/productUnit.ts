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
import {AddOrUpdateProductUnitReq, ProductUnitResp, ProductUnitQueryReq} from "@/api/product/model/productUnitModel";

enum Api {
    List = '/product/unit/list',
    AddOrUpdate = '/product/unit/addOrUpdate',
    DeleteBatch = '/product/unit/deleteBatch',
    UpdateStatus = '/product/unit/updateUnitStatus',
}

export function getUnitList(params: ProductUnitQueryReq) {
    return defHttp.post<BaseDataResp<ProductUnitResp>>(
        {
            url: Api.List,
            params,
        }
    );
}

export function addOrUpdateUnit(params: AddOrUpdateProductUnitReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: Api.AddOrUpdate,
            params,
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function deleteBatchUnits(ids: number[], mode: ErrorMessageMode = 'notice') {
    return defHttp.delete<BaseResp>(
        {
            url: `${Api.DeleteBatch}?ids=${ids}`
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function updateUnitStatus(params: {id: number, status: number}, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: Api.UpdateStatus,
            params,
        },
        {
            errorMessageMode: mode,
        },
    );
}