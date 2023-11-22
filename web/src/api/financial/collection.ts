import {defHttp} from '/@/utils/http/axios';
import { ErrorMessageMode } from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    AddOrUpdateCollectionReq,
    QueryCollectionReq,
    CollectionResp,
    CollectionDetailResp,
} from "@/api/financial/model/collectionModel";

enum API {
    PageList = '/financial/collection/pageList',
    AddOrUpdateAccount = '/financial/collection/addOrUpdate',
    DeleteBatch = '/financial/collection/deleteByIds',
    UpdateStatus = '/financial/collection/updateStatusByIds',
    GetDetail = '/financial/collection/getDetailById',
}

export function getCollectionPageList(params: QueryCollectionReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<CollectionResp>>(
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

export function addOrUpdateCollection(params: AddOrUpdateCollectionReq) {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdateAccount,
            params,
        },
    );
}

export function updateCollectionStatus(ids: number[], status: number) {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },
    );
}

export function deleteBatchCollection(ids: number[]) {
    return defHttp.put<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },
    );
}

export function getCollectionDetailById(id: number) {
    return defHttp.get<BaseDataResp<CollectionDetailResp>>(
        {
            url: `${API.GetDetail}/${id}`
        },
    );
}