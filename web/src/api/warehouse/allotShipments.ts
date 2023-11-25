import {defHttp} from '/@/utils/http/axios';
import { ErrorMessageMode } from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    AddOrUpdateAllotShipmentsReq,
    QueryAllotShipmentsReq,
    AllotShipmentsResp,
    AllotShipmentsDetailResp,
} from "@/api/warehouse/model/allotShipmentsModel";

enum API {
    PageList = '/warehouse/allotShipments/pageList',
    AddOrUpdateAccount = '/warehouse/allotShipments/addOrUpdate',
    DeleteBatch = '/warehouse/allotShipments/deleteByIds',
    UpdateStatus = '/warehouse/allotShipments/updateStatusByIds',
    GetDetail = '/warehouse/allotShipments/getDetailById',
}

export function getAllotShipmentsPageList(params: QueryAllotShipmentsReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<AllotShipmentsResp>>(
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

export function addOrUpdateAllotShipments(params: AddOrUpdateAllotShipmentsReq) {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdateAccount,
            params,
        },
    );
}

export function updateAllotShipmentsStatus(ids: number[], status: number) {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },
    );
}

export function deleteBatchAllotShipments(ids: number[]) {
    return defHttp.put<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },
    );
}

export function getAllotShipmentsDetailById(id: number) {
    return defHttp.get<BaseDataResp<AllotShipmentsDetailResp>>(
        {
            url: `${API.GetDetail}/${id}`
        },
    );
}