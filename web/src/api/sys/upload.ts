import { defHttp } from '/@/utils/http/axios';
import {ErrorMessageMode, SuccessMessageMode, UploadFileParams} from '/#/axios';
import {BaseResp} from "@/api/model/baseModel";
import {ContentTypeEnum} from "@/enums/httpEnum";

enum Api {
    UploadOss = '/v2/common/uploadOss',
}

export function uploadApi(params: UploadFileParams, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseResp>(
        {
            url: Api.UploadOss,
            params,
            headers: {
                'Content-type': ContentTypeEnum.FORM_DATA,
                // @ts-ignore
                ignoreCancelToken: true,
            },
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}