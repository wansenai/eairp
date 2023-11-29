import {defHttp} from '/@/utils/http/axios';
import {BaseDataResp} from "@/api/model/baseModel";
import {MenuListResp, AddOrUpdateMenuReq} from "@/api/sys/model/menuModel";
import {ErrorMessageMode, SuccessMessageMode} from "#/axios";
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

export const addOrUpdateMenu = (params: AddOrUpdateMenuReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'notice') => {
    return defHttp.post<BaseDataResp<any>>(
        {url: Api.AddOrUpdateMenu, params},
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    )
}

export const deleteMenu = (id: number, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'notice') => {
    return defHttp.post<BaseDataResp<any>>(
        {url: `${Api.DeleteMenu}?id=${id}`},
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    )
}
