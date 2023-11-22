import {defHttp} from '/@/utils/http/axios';
import { ErrorMessageMode } from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    TransferDetailResp,
    AddOrUpdateTransferReq,
    QueryTransferReq,
    TransferResp,
} from "@/api/financial/model/transferModel";

enum API {
    PageList = '/financial/transfer/pageList',
    AddOrUpdateAccount = '/financial/transfer/addOrUpdate',
    DeleteBatch = '/financial/transfer/deleteByIds',
    UpdateStatus = '/financial/transfer/updateStatusByIds',
    GetDetail = '/financial/transfer/getDetailById',
}

export function getTransferPageList(params: QueryTransferReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<TransferResp>>(
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

export function addOrUpdateTransfer(params: AddOrUpdateTransferReq) {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdateAccount,
            params,
        },
    );
}

export function updateTransferStatus(ids: number[], status: number) {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },
    );
}

export function deleteBatchTransfer(ids: number[]) {
    return defHttp.put<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },
    );
}

export function getTransferDetailById(id: number) {
    return defHttp.get<BaseDataResp<TransferDetailResp>>(
        {
            url: `${API.GetDetail}/${id}`
        },
    );
}