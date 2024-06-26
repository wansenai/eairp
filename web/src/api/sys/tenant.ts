import { defHttp } from '@/utils/http/axios';

import {addOrUpdateTenantReq, queryTenantListReq, TenantInfoModel} from './model/tenantModel';
import { ErrorMessageMode, SuccessMessageMode } from '#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";

enum Api {
    List= '/tenant/list',
    AddOrUpdate = '/tenant/addOrUpdate',
    Delete = '/tenant/delete',
    CheckAddUser = '/tenant/checkAddUser',
    Update = '/tenant/update',
}

export function getTenantList(params: queryTenantListReq) {
    return defHttp.post<BaseDataResp<TenantInfoModel>>(
        {url: Api.List, params},
    );
}

export function addOrUpdateTenant(params: addOrUpdateTenantReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {url: Api.AddOrUpdate, params},
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function checkAddUser() {
    return defHttp.get<BaseResp>(
        {url: Api.CheckAddUser},
    );
}

export function deleteTenant(tenantId: string) {
    return defHttp.post<BaseResp>(
        {url: `${Api.Delete}?tenantId=${tenantId}`},
    );
}
export function updateStatus(params: { id: any; status: number }, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {url: Api.Update, params},
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        },
    )
}