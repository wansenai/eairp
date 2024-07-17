import { defHttp } from '@/utils/http/axios';
import {MessageInfo, ReadMessageReq} from "@/api/sys/model/MessageModel";
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";

enum Api {
    List = '/sys/message/list',
    Read = '/sys/message/read',
}

export function getMessageList() {
    return defHttp.get<BaseDataResp<MessageInfo>>({
        url: Api.List,
    });
}

export function readMessage(params: ReadMessageReq) {
    return defHttp.post<BaseResp>({
        url: Api.Read,
        params,
    });
}