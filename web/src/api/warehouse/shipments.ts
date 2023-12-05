import {defHttp} from '/@/utils/http/axios';
import {ErrorMessageMode, SuccessMessageMode} from '/#/axios';
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
    Export = '/warehouse/otherShipments/export',
    ExportDetail = '/warehouse/otherShipments/exportDetail',
}

export function getOtherShipmentsPageList(params: QueryOtherShipmentsReq) {
    return defHttp.post<BaseDataResp<OtherShipmentsResp>>(
        {
            url: API.PageList,
            params,
        }
    );
}

export function addOrUpdateOtherShipments(params: AddOrUpdateOtherShipmentsReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
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

export function updateOtherShipmentsStatus(ids: number[], status: number, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function deleteBatchOtherShipments(ids: number[], successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function getOtherShipmentsDetailById(id: number) {
    return defHttp.get<BaseDataResp<OtherShipmentsDetailResp>>(
        {
            url: `${API.GetDetail}/${id}`
        },
    );
}

export function exportOtherShipments(params: QueryOtherShipmentsReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.Export}`,
            params,
            responseType: "blob"
        }
    );
}

export function exportOtherShipmentsDetail(receiptNumber: string) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.ExportDetail}/${receiptNumber}`,
            responseType: "blob"
        }
    );
}