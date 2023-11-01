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
    GetDetail = '/retail/shipments/getDetailById',
}

export function getShipmentsPageList(params: QueryShipmentsReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<ShipmentsResp>>(
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

export function addOrUpdateShipments(params: AddOrUpdateShipmentsReq) {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdate,
            params,
        }
    );
}