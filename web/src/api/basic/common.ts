import {defHttp} from '/@/utils/http/axios';
import {ErrorMessageMode, SuccessMessageMode} from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {ContentTypeEnum} from "@/enums/httpEnum";

enum Api {
    UploadXlsx = '/v2/common/upload/excel',
    ExportXlsx = '/v2/common/export/excel',
    UploadOss = '/v2/common/uploadOss',
    GenerateId = '/v2/common/nextId',
    productCoverUpload = '/v2/common/upload/productCoverUpload',
}

export interface UploadFileParams {
    // file name
    file: File | any;
}

export interface UploadCoverProductParams {
    // file name
    file: File | any;
    type: number;
}

export function uploadXlsx(params: UploadFileParams, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseResp>(
        {
            url: Api.UploadXlsx,
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

export function productCoverUpload(params: UploadCoverProductParams, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseResp>(
        {
            url: Api.productCoverUpload,
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

export function exportXlsx(type: string, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${Api.ExportXlsx}?type=${type}`,
            responseType: "blob"
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function uploadOss(params: UploadFileParams, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
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

export function generateId(type: string) {
    return defHttp.get<BaseDataResp<string>>(
        {
            url: `${Api.GenerateId}/${type}`,
        }
    );
}