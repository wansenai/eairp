import {defHttp} from '/@/utils/http/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    QuerySaleRefundReq,
    AddOrUpdateReceiptSaleRefundReq,
    SaleRefundDetailData
} from "@/api/sale/model/refundModel"

enum API {
    PageList = '/sale/refund/pageList',
    AddOrUpdate = '/sale/refund/addOrUpdate',
    GetDetail = '/sale/refund/detail',
    UpdateStatus = '/sale/refund/updateStatus',
    Delete = '/sale/refund/delete',
}

export function getSaleRefundPageList(params: QuerySaleRefundReq) {
    return defHttp.get<BaseDataResp<SaleRefundDetailData>>(
        {
            url: API.PageList,
            params,
        }
    );
}

export function addOrUpdateSaleRefund(params: AddOrUpdateReceiptSaleRefundReq) {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdate,
            params,
        }
    );
}

export function getSaleRefundDetail(id: string) {
    return defHttp.get<BaseDataResp<AddOrUpdateReceiptSaleRefundReq>>(
        {
            url: `${API.GetDetail}/${id}`,
        }
    );
}

export function updateSaleRefundStatus(ids: number[], status: number ) {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}/${ids}/${status}`,
        }
    );
}

export function deleteSaleRefund(ids: number[]) {
    return defHttp.put<BaseResp>(
        {
            url: `${API.Delete}/${ids}`,
        }
    );
}