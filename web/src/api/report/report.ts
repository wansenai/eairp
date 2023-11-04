import {defHttp} from '/@/utils/http/axios';
import {BaseDataResp} from "@/api/model/baseModel";
import {reportModel} from "@/api/report/reportModel";

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