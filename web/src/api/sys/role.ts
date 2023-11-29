import {ErrorMessageMode, SuccessMessageMode} from "#/axios";
import {defHttp} from "@/utils/http/axios";
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    addOrUpdateRoleInfoReq,
    addOrUpdateRolePermissionReq,
    GetRoleInfoModel,
    queryRoleListReq
} from "@/api/sys/model/roleModel";

enum Api {
    List = '/sysRole/list',
    PageList = '/sysRole/PageList',
    UpdateStatus = '/sysRole/updateStatus',
    AddOrUpdateRole = '/sysRole/addOrUpdateRole',
    DeleteRole = '/sysRole/deleteRole',
    RolePermission = '/sysRole/permission'
}

export function getRoleList(errorMode: ErrorMessageMode = 'notice') {
    return defHttp.get<BaseResp>(
        {url: Api.List},
        {
            errorMessageMode: errorMode,
        }
    )
}

export function getPageList(params: queryRoleListReq) {
    return defHttp.post<BaseDataResp<GetRoleInfoModel>>(
        {url: Api.PageList, params},
    )
}

export function setRoleStatus(id: string, status: number, successMode: SuccessMessageMode ='notice', errorMode: ErrorMessageMode = 'notice'){
    return defHttp.post<BaseResp>(
        {url: `${Api.UpdateStatus}?id=${id}&status=${status}`},
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        },
    )
}

export function addOrUpdateRole(params: addOrUpdateRoleInfoReq, successMode: SuccessMessageMode ='notice',  errorMode: ErrorMessageMode = 'notice'){
    return defHttp.post<BaseResp>(
        {url: Api.AddOrUpdateRole, params: params},
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    )
}

export function deleteRole(id: string, successMode: SuccessMessageMode ='notice',  errorMode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {url: `${Api.DeleteRole}?id=${id}`},
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    )
}

export function rolePermission(params: addOrUpdateRolePermissionReq, successMode: SuccessMessageMode ='notice',  errorMode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {url: Api.RolePermission, params: params},
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    )
}