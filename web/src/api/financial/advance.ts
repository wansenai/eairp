import {defHttp} from '/@/utils/http/axios';
import { ErrorMessageMode } from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    AdvanceChargeResp,
    AddOrUpdateAdvanceReq,
    QueryAdvanceReq, AdvanceChargeDetailResp,
} from "@/api/financial/model/advanceModel";

enum API {
    PageList = '/financial/advance-charge/pageList',
    AddOrUpdateAccount = '/financial/advance-charge/addOrUpdate',
    DeleteBatch = '/financial/advance-charge/deleteByIds',
    UpdateStatus = '/financial/advance-charge/updateStatusByIds',
    GetDetail = '/financial/advance-charge/getDetailById',
}

export function getAdvancePageList(params: QueryAdvanceReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<AdvanceChargeResp>>(
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

export function addOrUpdateAdvance(params: AddOrUpdateAdvanceReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdateAccount,
            params,
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function updateAdvanceStatus(ids: number[] | string[], status: number, mode: ErrorMessageMode = 'notice') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function deleteBatchAdvance(ids: number[], mode: ErrorMessageMode = 'notice') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function getAdvanceDetail(id: number | string, mode: ErrorMessageMode = 'notice') {
    return defHttp.get<BaseDataResp<AdvanceChargeDetailResp>>(
        {
            url: `${API.GetDetail}/${id}`
        },
        {
            errorMessageMode: mode,
        },
    );
}