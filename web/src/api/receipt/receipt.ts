import {defHttp} from '/@/utils/http/axios';
import {BaseDataResp} from "@/api/model/baseModel";
import {ReceiptDetailResp} from "@/api/receipt/model/receiptModel"

enum API {
    GetDetail = '/receipt/detail',
}

export function getReceiptDetail(id: string | number) {
    return defHttp.get<BaseDataResp<ReceiptDetailResp>>(
        {
            url: `${API.GetDetail}/${id}`,
        }
    );
}