import {defHttp} from '/@/utils/http/axios';
import {BaseDataResp} from "@/api/model/baseModel";
import {queryReceipt, ReceiptDetailResp, ReceiptResp} from "@/api/receipt/model/receiptModel"

enum API {
    GetOtherReceipt = '/receipt/otherReceipt',
    GetOtherReceiptDetail = '/receipt/otherReceiptDetail',
}

export function getReceipt(params: queryReceipt, type: string, subType: string) {
    return defHttp.get<BaseDataResp<ReceiptResp>>(
        {
            url: API.GetOtherReceipt,
            params: params
        }
    );
}

export function getReceiptDetail(params: queryReceipt) {
    return defHttp.get<BaseDataResp<ReceiptDetailResp>>(
        {
            url: API.GetOtherReceiptDetail,
            params: params
        }
    );
}