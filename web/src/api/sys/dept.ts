import {
    addOrUpdateDeptReq,
    DeptListItem,
    GetDeptInfoModel,
} from './model/dpetModel';

import { defHttp } from '/@/utils/http/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {ErrorMessageMode, SuccessMessageMode} from "#/axios";

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

export function addOrUpdateDept(params: addOrUpdateDeptReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>({
            url: Api.AddOrUpdateDept, params},
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    )
}

export function deleteDept(id: number | string, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>({
            url: `${Api.DeleteDept}?id=${id}`},
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    )
}