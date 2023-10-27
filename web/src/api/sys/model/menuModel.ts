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
