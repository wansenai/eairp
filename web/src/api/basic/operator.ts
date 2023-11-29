import {defHttp} from '/@/utils/http/axios';
import {ErrorMessageMode, SuccessMessageMode} from '/#/axios';
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

export function getOperatorPageList(params: QueryOperatorReq) {
    return defHttp.post<BaseDataResp<OperatorResp>>(
        {
            url: API.PageList,
            params,
        }
    );
}

export function addOrUpdateOperator(params: AddOrUpdateOperatorReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdateOperator,
            params,
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function updateOperatorStatus(ids: number[], status: number, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function deleteBatchOperator(ids: number[], successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.delete<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function getOperatorList(type: string) {
    return defHttp.get<BaseDataResp<OperatorResp>>(
        {
            url: `${API.List}/${type}`,
        }
    );
}