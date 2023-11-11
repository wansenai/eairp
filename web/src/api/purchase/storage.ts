import {defHttp} from '/@/utils/http/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    QueryPurchaseStorageReq,
    AddOrUpdatePurchaseStorageReq,
    PurchaseStorageDetailData
} from "@/api/purchase/model/storageModel"

enum API {
    PageList = '/purchase/storage/pageList',
    AddOrUpdate = '/purchase/storage/addOrUpdate',
    GetDetail = '/purchase/storage/detail',
    UpdateStatus = '/purchase/storage/updateStatus',
    Delete = '/purchase/storage/delete',
}

export function getPurchaseStoragePageList(params: QueryPurchaseStorageReq) {
    return defHttp.get<BaseDataResp<PurchaseStorageDetailData>>(
        {
            url: API.PageList,
            params,
        }
    );
}

export function addOrUpdatePurchaseStorage(params: AddOrUpdatePurchaseStorageReq) {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdate,
            params,
        }
    );
}

export function getPurchaseStorageDetail(id: string) {
    return defHttp.get<BaseDataResp<AddOrUpdatePurchaseStorageReq>>(
        {
            url: `${API.GetDetail}/${id}`,
        }
    );
}

export function updatePurchaseStorageStatus(ids: number[], status: number ) {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}/${ids}/${status}`,
        }
    );
}

export function deletePurchaseStorage(ids: number[]) {
    return defHttp.put<BaseResp>(
        {
            url: `${API.Delete}/${ids}`,
        }
    );
}