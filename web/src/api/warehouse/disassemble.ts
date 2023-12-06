import {defHttp} from '/@/utils/http/axios';
import {ErrorMessageMode, SuccessMessageMode} from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    AddOrUpdateDisAssembleReq,
    QueryDisAssembleReq,
    DisAssembleResp,
    DisAssembleDetailResp, ExportDisAssembleReq,
} from "@/api/warehouse/model/disassembleModel";

enum API {
    PageList = '/warehouse/disassemble/pageList',
    AddOrUpdateAccount = '/warehouse/disassemble/addOrUpdate',
    DeleteBatch = '/warehouse/disassemble/deleteByIds',
    UpdateStatus = '/warehouse/disassemble/updateStatusByIds',
    GetDetail = '/warehouse/disassemble/getDetailById',
    Export = '/warehouse/disassemble/export',
    ExportDetail = '/warehouse/disassemble/exportDetail',
}

export function getDisAssemblePageList(params: QueryDisAssembleReq) {
    return defHttp.post<BaseDataResp<DisAssembleResp>>(
        {
            url: API.PageList,
            params,
        }
    );
}

export function addOrUpdateDisAssemble(params: AddOrUpdateDisAssembleReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
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

export function updateDisAssembleStatus(ids: number[], status: number, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function deleteBatchDisAssemble(ids: number[], successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function getDisAssembleDetailById(id: number) {
    return defHttp.get<BaseDataResp<DisAssembleDetailResp>>(
        {
            url: `${API.GetDetail}/${id}`
        },
    );
}

export function exportDisAssemble(params: ExportDisAssembleReq) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.Export}`,
            params,
            responseType: "blob"
        }
    );
}

export function exportDisAssembleDetail(receiptNumber: string) {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${API.ExportDetail}/${receiptNumber}`,
            responseType: "blob"
        }
    );
}