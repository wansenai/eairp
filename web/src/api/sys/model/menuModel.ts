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

import type {RouteMeta} from 'vue-router';
import {BaseListResp} from '../../model/baseModel';

export interface RouteItem {
    path: string;
    component: any;
    meta: RouteMeta;
    name?: string;
    alias?: string | string[];
    redirect?: string;
    caseSensitive?: boolean;
    children?: RouteItem[];
}

export interface MenuPageResp {
    total: number;
    data: RouteItem[];
}

export interface MenuInfo {
    id: number;
    type?: number;
    parentId?: number;
    path?: string;
    name?: string;
    title?: string;
    redirect?: string;
    component?: string;
    sort?: number;
    disabled?: boolean;
    meta: Meta;
}

interface Meta {
    title?: string;
    icon?: string;
    hideMenu?: boolean;
    hideBreadcrumb?: boolean;
    ignoreKeepAlive?: boolean;
    hideTab?: boolean;
    frameSrc?: string;
    carryParam?: boolean;
    hideChildrenInMenu?: boolean;
    affix?: boolean;
    dynamicLevel?: number;
    realPath?: string;
}

export interface AddOrUpdateMenuReq {
    id: number;
    menuType: number;
    name: string;
    title: string;
    parentId: number;
    sort: number;
    icon: string;
    path: string;
    component: string;
    status: number;
    blank: number;
    ignoreKeepAlive: number;
    hideMenu: number;
}

export type MenuListResp = BaseListResp<MenuInfo>;

/**
 * @description: Get menu return value
 */
export type RoleMenuResp = BaseListResp<RouteItem>;
