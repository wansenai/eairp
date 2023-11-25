import {defHttp} from '/@/utils/http/axios';
import { ErrorMessageMode } from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    AddOrUpdateOtherShipmentsReq,
    QueryOtherShipmentsReq,
    OtherShipmentsResp,
    OtherShipmentsDetailResp,
} from "@/api/warehouse/model/shipmentsModel";

enum API {
    PageList = '/warehouse/otherShipments/pageList',
    AddOrUpdateAccount = '/warehouse/otherShipments/addOrUpdate',
    DeleteBatch = '/warehouse/otherShipments/deleteByIds',
    UpdateStatus = '/warehouse/otherShipments/updateStatusByIds',
    GetDetail = '/warehouse/otherShipments/getDetailById',
}

export function getOtherShipmentsPageList(params: QueryOtherShipmentsReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<OtherShipmentsResp>>(
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

export function addOrUpdateOtherShipments(params: AddOrUpdateOtherShipmentsReq) {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdateAccount,
            params,
        },
    );
}

export function updateOtherShipmentsStatus(ids: number[], status: number) {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },
    );
}

export function deleteBatchOtherShipments(ids: number[]) {
    return defHttp.put<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },
    );
}

export function getOtherShipmentsDetailById(id: number) {
    return defHttp.get<BaseDataResp<OtherShipmentsDetailResp>>(
        {
            url: `${API.GetDetail}/${id}`
        },
    );
}