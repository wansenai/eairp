import {defHttp} from '/@/utils/http/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    QuerySaleShipmentsReq,
    AddOrUpdateReceiptSaleShipmentsReq,
    SaleShipmentDetailData,
    LinkReceiptSaleShipmentsDetailResp
} from "@/api/sale/model/shipmentsModel"
import {ErrorMessageMode, SuccessMessageMode} from "#/axios";

enum API {
    PageList = '/sale/shipments/pageList',
    AddOrUpdate = '/sale/shipments/addOrUpdate',
    GetDetail = '/sale/shipments/detail',
    UpdateStatus = '/sale/shipments/updateStatus',
    Delete = '/sale/shipments/delete',
    GetLinkShipmentsDetail = '/sale/shipments/getLinkShipmentDetail',
}

export function getSaleShipmentsPageList(params: QuerySaleShipmentsReq) {
    return defHttp.get<BaseDataResp<SaleShipmentDetailData>>(
        {
            url: API.PageList,
            params,
        }
    );
}

export function addOrUpdateSaleShipments(params: AddOrUpdateReceiptSaleShipmentsReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdate,
            params,
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function getSaleShipmentsDetail(id: string) {
    return defHttp.get<BaseDataResp<AddOrUpdateReceiptSaleShipmentsReq>>(
        {
            url: `${API.GetDetail}/${id}`,
        }
    );
}

export function updateSaleShipmentsStatus(ids: number[], status: number, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}/${ids}/${status}`,
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function deleteSaleShipments(ids: number[], successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.Delete}/${ids}`,
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function getLinkShipmentsDetail(receiptNumber: string) {
    return defHttp.get<BaseDataResp<LinkReceiptSaleShipmentsDetailResp>>(
        {
            url: `${API.GetLinkShipmentsDetail}/${receiptNumber}`,
        }
    );
}