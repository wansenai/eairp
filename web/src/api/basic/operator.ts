import {defHttp} from '/@/utils/http/axios';
import { ErrorMessageMode } from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    OperatorResp,
    AddOrUpdateOperatorReq,
    QueryOperatorReq
} from "@/api/basic/model/operatorModel";


enum API {
    List = '/basic/operator/list',
    AddOrUpdateOperator = '/basic/operator/addOrUpdate',
    DeleteBatch = '/basic/operator/delete',
    UpdateStatus = '/basic/operator/updateStatus',
}

export function getOperatorList(params: QueryOperatorReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<OperatorResp>>(
        {
            url: API.List,
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