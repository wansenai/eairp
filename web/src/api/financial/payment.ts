import {defHttp} from '@/utils/http/axios';
import {ErrorMessageMode, SuccessMessageMode} from '/#/axios';
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
    Export = '/financial/payment/export',
    ExportDetail = '/financial/payment/exportDetail',
}

export function getPaymentPageList(params: QueryPaymentReq) {
    return defHttp.post<BaseDataResp<PaymentResp>>(
        {
            url: API.PageList,
            params,
        }
    );
}

export function addOrUpdatePayment(params: AddOrUpdatePaymentReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
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

export function updatePaymentStatus(ids: number[], status: number, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function deleteBatchPayment(ids: number[], successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
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

export function exportPayment(params: QueryPaymentReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.Export}`,
            params,
            responseType: "blob"
        }
    );
}

export function exportPaymentDetail(receiptNumber: string) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.ExportDetail}/${receiptNumber}`,
            responseType: "blob"
        }
    );
}