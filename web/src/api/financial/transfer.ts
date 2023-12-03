import {defHttp} from '/@/utils/http/axios';
import {ErrorMessageMode, SuccessMessageMode} from '/#/axios';
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
    Export = '/financial/transfer/export',
}

export function getTransferPageList(params: QueryTransferReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<TransferResp>>(
        {
            url: API.PageList,
            params,
        }
    );
}

export function addOrUpdateTransfer(params: AddOrUpdateTransferReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
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

export function updateTransferStatus(ids: number[], status: number, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function deleteBatchTransfer(ids: number[], successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function getTransferDetailById(id: number) {
    return defHttp.get<BaseDataResp<TransferDetailResp>>(
        {
            url: `${API.GetDetail}/${id}`
        },
    );
}

export function exportTransfer(params: QueryTransferReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.Export}`,
            params,
            responseType: "blob"
        }
    );
}