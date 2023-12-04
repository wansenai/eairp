import { defHttp } from '/@/utils/http/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    AddOrUpdateSystemConfigModel,
    GetSystemConfigModel,
} from '@/api/sys/model/configModel';

enum Api {
    GetConfigInfo = '/sys/config/getCompanyInfo',
    AddOrUpdateConfigInfo = '/sys/config/addOrUpdate',
}

export function getConfigInfo() {
    return defHttp.get<BaseDataResp<GetSystemConfigModel>>({url: Api.GetConfigInfo})
}

export function addOrUpdateConfigInfo(params: AddOrUpdateSystemConfigModel) {
    return defHttp.post<BaseResp>({url: Api.AddOrUpdateConfigInfo, params})
}

