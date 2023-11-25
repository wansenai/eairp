import {defHttp} from '/@/utils/http/axios';
import { ErrorMessageMode } from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    AddOrUpdateOtherStorageReq,
    QueryOtherStorageReq,
    OtherStorageResp,
    OtherStorageDetailResp,
} from "@/api/warehouse/model/storageModel";

enum API {
    PageList = '/warehouse/otherStorage/pageList',
    AddOrUpdateAccount = '/warehouse/otherStorage/addOrUpdate',
    DeleteBatch = '/warehouse/otherStorage/deleteByIds',
    UpdateStatus = '/warehouse/otherStorage/updateStatusByIds',
    GetDetail = '/warehouse/otherStorage/getDetailById',
}

export function getOtherStoragePageList(params: QueryOtherStorageReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<OtherStorageResp>>(
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

export function addOrUpdateOtherStorage(params: AddOrUpdateOtherStorageReq) {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdateAccount,
            params,
        },
    );
}

export function updateOtherStorageStatus(ids: number[], status: number) {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },
    );
}

export function deleteBatchOtherStorage(ids: number[]) {
    return defHttp.put<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },
    );
}

export function getOtherStorageDetailById(id: number) {
    return defHttp.get<BaseDataResp<OtherStorageDetailResp>>(
        {
            url: `${API.GetDetail}/${id}`
        },
    );
}