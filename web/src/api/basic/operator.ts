import {defHttp} from '/@/utils/http/axios';
import { ErrorMessageMode } from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    OperatorResp,
    AddOrUpdateOperatorReq,
    QueryOperatorReq
} from "@/api/basic/model/operatorModel";


enum API {
    PageList = '/basic/operator/pageList',
    AddOrUpdateOperator = '/basic/operator/addOrUpdate',
    DeleteBatch = '/basic/operator/delete',
    UpdateStatus = '/basic/operator/updateStatus',
    List = '/basic/operator/list',
}

export function getOperatorPageList(params: QueryOperatorReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<OperatorResp>>(
        {
            url: API.PageList,
            params,
        },
        {
            errorMessageMode: mode,
            successMessageMode: mode,
        },
    );
}

export function addOrUpdateOperator(params: AddOrUpdateOperatorReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdateOperator,
            params,
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function updateOperatorStatus(ids: number[], status: number, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function deleteBatchOperator(ids: number[], mode: ErrorMessageMode = 'notice') {
    return defHttp.delete<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function getOperatorList(type: string) {
    return defHttp.get<BaseDataResp<OperatorResp[]>>(
        {
            url: `${API.List}/${type}`
        },
    );
}