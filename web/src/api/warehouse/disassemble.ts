import {defHttp} from '/@/utils/http/axios';
import { ErrorMessageMode } from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    AddOrUpdateDisAssembleReq,
    QueryDisAssembleReq,
    DisAssembleResp,
    DisAssembleDetailResp,
} from "@/api/warehouse/model/disassembleModel";

enum API {
    PageList = '/warehouse/disassemble/pageList',
    AddOrUpdateAccount = '/warehouse/disassemble/addOrUpdate',
    DeleteBatch = '/warehouse/disassemble/deleteByIds',
    UpdateStatus = '/warehouse/disassemble/updateStatusByIds',
    GetDetail = '/warehouse/disassemble/getDetailById',
}

export function getDisAssemblePageList(params: QueryDisAssembleReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<DisAssembleResp>>(
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

export function addOrUpdateDisAssemble(params: AddOrUpdateDisAssembleReq) {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdateAccount,
            params,
        },
    );
}

export function updateDisAssembleStatus(ids: number[], status: number) {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },
    );
}

export function deleteBatchDisAssemble(ids: number[]) {
    return defHttp.put<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },
    );
}

export function getDisAssembleDetailById(id: number) {
    return defHttp.get<BaseDataResp<DisAssembleDetailResp>>(
        {
            url: `${API.GetDetail}/${id}`
        },
    );
}