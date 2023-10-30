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
import {BaseDataResp} from "@/api/model/baseModel";
import {MenuListResp, AddOrUpdateMenuReq} from "@/api/sys/model/menuModel";
import {ErrorMessageMode} from "#/axios";
enum Api {
    GetMenuList = '/sysRole/menu',
    AddOrUpdateMenu = '/menu/addOrUpdate',
    DeleteMenu = '/menu/delete',
}

/**
 * @description: Get user menu based on id
 */

export const getMenuList = () => {
    return defHttp.get<BaseDataResp<MenuListResp>>({url: Api.GetMenuList});
};

export const addOrUpdateMenu = (params: AddOrUpdateMenuReq, mode: ErrorMessageMode = 'notice') => {
    return defHttp.post<BaseDataResp<any>>(
        {url: Api.AddOrUpdateMenu, params},
        {
          errorMessageMode: mode,
          successMessageMode: mode,
        }
    )
}

export const deleteMenu = (id: number, mode: ErrorMessageMode = 'notice') => {
    return defHttp.post<BaseDataResp<any>>(
        {url: `${Api.DeleteMenu}?id=${id}`},
        {
            errorMessageMode: mode,
            successMessageMode: mode,
        }
    )
}
