import {defHttp} from '/@/utils/http/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    AddOrUpdateShipmentsReq, AddOrUpdateShipmentsResp,
    QueryShipmentsReq,
    ShipmentsResp
} from "@/api/retail/model/shipmentsModel"
import {ErrorMessageMode, SuccessMessageMode} from "#/axios";

enum API {
    PageList = '/retail/shipments/pageList',
    List = '/retail/shipments/list',
    AddOrUpdate = '/retail/shipments/addOrUpdate',
    DeleteBatch = '/retail/shipments/deleteByIds',
    UpdateStatus = '/retail/shipments/updateStatus',
    GetDetail = '/retail/shipments/detail',
    GetLinkShipmentDetail = '/retail/shipments/getLinkShipmentDetail',
    Export = '/retail/shipments/export',
}

export function getShipmentsPageList(params: QueryShipmentsReq) {
    return defHttp.post<BaseDataResp<ShipmentsResp>>(
        {
            url: API.PageList,
            params,
        }
    );
}

export function getShipmentsList(params: QueryShipmentsReq) {
    return defHttp.post<BaseDataResp<ShipmentsResp>>(
        {
            url: API.List,
            params,
        }
    );
}

export function addOrUpdateShipments(params: AddOrUpdateShipmentsReq,
                                     successMode: SuccessMessageMode = 'message',
                                     errorMode: ErrorMessageMode = 'message',) {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdate,
            params,
        },
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function deleteShipments(ids: string[], successMode: SuccessMessageMode = 'message', errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`,
        },
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function updateShipmentsStatus(ids: string[], status: number, successMode: SuccessMessageMode = 'message', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`,
        },
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function getShipmentsDetail(id: string | number, errorMode: ErrorMessageMode = 'message') {
    return defHttp.get<BaseDataResp<AddOrUpdateShipmentsReq>>(
        {
            url: `${API.GetDetail}/${id}`,
        },
        {
            errorMessageMode: errorMode,
        }
    );
}

export function getLinkShipmentsDetail(otherReceipt: string, errorMode: ErrorMessageMode = 'message') {
    return defHttp.get<BaseDataResp<AddOrUpdateShipmentsResp>>(
        {
            url: `${API.GetLinkShipmentDetail}/${otherReceipt}`,
        },
        {
            errorMessageMode: errorMode,
        }
    );
}

export function exportShipments(params: QueryShipmentsReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.Export}`,
            params,
            responseType: "blob"
        }
    );
}