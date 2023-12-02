import {defHttp} from '/@/utils/http/axios';
import {ErrorMessageMode, SuccessMessageMode} from '/#/axios';
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
    Export = '/warehouse/allotShipments/export',
}

export function getAllotShipmentsPageList(params: QueryAllotShipmentsReq) {
    return defHttp.post<BaseDataResp<AllotShipmentsResp>>(
        {
            url: API.PageList,
            params,
        }
    );
}

export function addOrUpdateAllotShipments(params: AddOrUpdateAllotShipmentsReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdateAccount,
            params,
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function updateAllotShipmentsStatus(ids: number[], status: number, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function deleteBatchAllotShipments(ids: number[], successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function getAllotShipmentsDetailById(id: number) {
    return defHttp.get<BaseDataResp<AllotShipmentsDetailResp>>(
        {
            url: `${API.GetDetail}/${id}`
        },
    );
}

export function exportAllotShipments(params: QueryAllotShipmentsReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.Export}`,
            params,
            responseType: "blob"
        }
    );
}