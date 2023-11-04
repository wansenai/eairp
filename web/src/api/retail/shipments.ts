import {defHttp} from '/@/utils/http/axios';
import { ErrorMessageMode } from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    AddOrUpdateShipmentsReq,
    QueryShipmentsReq,
    ShipmentsResp
} from "@/api/retail/model/shipmentsModel"

enum API {
    PageList = '/retail/shipments/pageList',
    AddOrUpdate = '/retail/shipments/addOrUpdate',
    DeleteBatch = '/retail/shipments/deleteByIds',
    UpdateStatus = '/retail/shipments/updateStatus',
    GetDetail = '/retail/shipments/detail',
}

export function getShipmentsPageList(params: QueryShipmentsReq) {
    return defHttp.post<BaseDataResp<ShipmentsResp>>(
        {
            url: API.PageList,
            params,
        }
    );
}

export function addOrUpdateShipments(params: AddOrUpdateShipmentsReq) {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdate,
            params,
        }
    );
}

export function deleteShipments(ids: string[]) {
    return defHttp.post<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`,
            params: ids,
        }
    );
}

export function updateShipmentsStatus(ids: string[], status: number) {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`,
        }
    );
}

export function getShipmentsDetail(id: string | number) {
    return defHttp.get<BaseDataResp<AddOrUpdateShipmentsReq>>(
        {
            url: `${API.GetDetail}/${id}`,
        }
    );
}