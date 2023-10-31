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
import {BaseDataResp} from "@/api/model/baseModel";
import {AddOrUpdateProductCategoryReq, ProductCategoryResp} from "@/api/product/model/productCategoryModel";

enum Api {
    List = '/product/category/list',
    addOrUpdate = '/product/category/addOrUpdate',
    deleteBatch = '/product/category/deleteBatch',
}

export function getCategoryList() {
    return defHttp.get<BaseDataResp<ProductCategoryResp>>(
        {
            url: Api.List,
        }
    );
}

export function addOrUpdateCategory(params: AddOrUpdateProductCategoryReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<AddOrUpdateProductCategoryReq>>(
        {
            url: Api.addOrUpdate,
            params,
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function deleteCategory(ids: number[], mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<string>>(
        {
            url: `${Api.deleteBatch}?ids=${ids}`,
        },
        {
            errorMessageMode: mode,
        },
    );
}