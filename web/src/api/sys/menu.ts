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
