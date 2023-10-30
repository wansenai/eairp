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

import {BasicFetchResult} from "@/api/model/baseModel";

export interface DeptListItem {
    deptName: string;
}

export interface GetDeptInfoModel {
    // 机构id
    id: string | number;
    // 机构编号
    deptNumber: string;
    // 机构名称
    deptName: string;
    // 备注
    remark: string;
    // 父级部门id
    parentId: string;
    // 排序
    sort: number;
}

export interface addOrUpdateDeptReq {
    id: number | string;
    deptName: string;
    parentId: number;
    deptNumber: string;
    leader: string;
    status: number;
    remark: string;
    sort: string;
}
