import {
    addOrUpdateDeptReq,
    DeptListItem,
    GetDeptInfoModel,
} from './model/dpetModel';

import { defHttp } from '/@/utils/http/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {ErrorMessageMode} from "#/axios";

enum Api {
    DeptList = '/dept/list',
    UserBindDept = '/dept/userBindDept',
    AddOrUpdateDept = '/dept/addOrUpdate',
    DeleteDept = '/dept/delete',
}
export function getDeptList(params?: DeptListItem) {
    return defHttp.get<BaseDataResp<GetDeptInfoModel>>({url: Api.DeptList, params})
}
export function getUserBindDept() {
    return defHttp.get<BaseDataResp<GetDeptInfoModel>>({url: Api.UserBindDept})
}

export function addOrUpdateDept(params: addOrUpdateDeptReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>({
            url: Api.AddOrUpdateDept, params},
        {
            errorMessageMode: mode,
            successMessageMode: mode,
        }
    )
}

export function deleteDept(id: number | string, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>({
            url: `${Api.DeleteDept}?id=${id}`},
        {
            errorMessageMode: mode,
            successMessageMode: mode,
        }
    )
}