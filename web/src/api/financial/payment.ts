import {defHttp} from '/@/utils/http/axios';
import { ErrorMessageMode } from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    AddOrUpdatePaymentReq,
    QueryPaymentReq,
    PaymentResp,
    PaymentDetailResp,
    QueryPaymentArrearsReq,
    PaymentArrearsResp,
} from "@/api/financial/model/paymentModel";

enum API {
    PageList = '/financial/payment/pageList',
    AddOrUpdateAccount = '/financial/payment/addOrUpdate',
    DeleteBatch = '/financial/payment/deleteByIds',
    UpdateStatus = '/financial/payment/updateStatusByIds',
    GetDetail = '/financial/payment/getDetailById',
    GetArrearsPage = '/purchase/arrears/pageList',
}

export function getPaymentPageList(params: QueryPaymentReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<PaymentResp>>(
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

export function addOrUpdatePayment(params: AddOrUpdatePaymentReq) {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdateAccount,
            params,
        },
    );
}

export function updatePaymentStatus(ids: number[], status: number) {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },
    );
}

export function deleteBatchPayment(ids: number[]) {
    return defHttp.put<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },
    );
}

export function getPaymentDetailById(id: number) {
    return defHttp.get<BaseDataResp<PaymentDetailResp>>(
        {
            url: `${API.GetDetail}/${id}`
        },
    );
}

export function getArrearsPageList(params: QueryPaymentArrearsReq) {
    return defHttp.post<BaseDataResp<PaymentArrearsResp>>(
        {
            url: API.GetArrearsPage,
            params,
        }
    );
}