import {defHttp} from '/@/utils/http/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {reportModel} from "@/api/report/model/reportModel";

enum API {
    getStatisticalData = '/report/homePage/statistics'
}


export function getStatistical() {
    return defHttp.get<BaseDataResp<reportModel>>(
        {
            url: API.getStatisticalData,
        },
    );
}