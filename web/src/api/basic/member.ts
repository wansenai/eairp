import {defHttp} from '/@/utils/http/axios';
import {ErrorMessageMode, SuccessMessageMode} from '/#/axios';
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

export function getMemberPageList(params: QueryMemberReq) {
    return defHttp.post<BaseDataResp<MemberResp>>(
        {
            url: API.PageList,
            params,
        }
    );
}

export function addOrUpdateMember(params: AddOrUpdateMemberReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdateMember,
            params,
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function updateMemberStatus(ids: number[], status: number, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function deleteBatchMember(ids: number[], successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.delete<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function getMemberList() {
    return defHttp.get<BaseDataResp<MemberResp>>(
        {
            url: API.List
        }
    );
}