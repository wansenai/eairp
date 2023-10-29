import {defHttp} from '/@/utils/http/axios';
import { ErrorMessageMode } from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    MemberResp,
    AddOrUpdateMemberReq,
    QueryMemberReq
} from "@/api/basic/model/memberModel";


enum API {
    List = '/basic/member/list',
    PageList = '/basic/member/pageList',
    AddOrUpdateMember = '/basic/member/addOrUpdate',
    DeleteBatch = '/basic/member/deleteBatch',
    UpdateStatus = '/basic/member/updateStatus',
}

export function getMemberPageList(params: QueryMemberReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<MemberResp>>(
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

export function addOrUpdateMember(params: AddOrUpdateMemberReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdateMember,
            params,
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function updateMemberStatus(ids: number[], status: number, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function deleteBatchMember(ids: number[], mode: ErrorMessageMode = 'notice') {
    return defHttp.delete<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function getMemberList() {
    return defHttp.get<BaseDataResp<MemberResp>>(
        {
            url: API.List
        }
    );
}