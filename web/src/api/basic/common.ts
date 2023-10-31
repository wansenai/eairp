/*
 * Copyright (C) 2023-2033 WanSen AI Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import {defHttp} from '/@/utils/http/axios';
import { ErrorMessageMode } from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {ContentTypeEnum} from "@/enums/httpEnum";

enum Api {
    UploadXlsx = '/v2/common/upload/excel',
    ExportXlsx = '/v2/common/export/excel',
    UploadOss = '/v2/common/uploadOss',
    GenerateId = '/v2/common/nextId',
}

export interface UploadFileParams {
    // file name
    file: File;
}

export function uploadXlsx(params: UploadFileParams, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: Api.UploadXlsx,
            params,
            headers: {
                'Content-type': ContentTypeEnum.FORM_DATA,
                // @ts-ignore
                ignoreCancelToken: true,
            },
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function exportXlsx(type: string, mode: ErrorMessageMode = 'notice') {
    return defHttp.get<BaseDataResp<Blob>>(
        {
            url: `${Api.ExportXlsx}?type=${type}`,
            responseType: "blob"
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function uploadOss(params: UploadFileParams, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: Api.UploadOss,
            params,
            headers: {
                'Content-type': ContentTypeEnum.FORM_DATA,
                // @ts-ignore
                ignoreCancelToken: true,
            },
        },
        {
            errorMessageMode: mode,
        },
    );
}

export function generateId(type: string) {
    return defHttp.get<BaseDataResp<number>>(
        {
            url: `${Api.GenerateId}/${type}`,
        }
    );
}