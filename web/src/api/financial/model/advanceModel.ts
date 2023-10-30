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

interface FileData {
    id: number | string;
    fileName: string;
    fileUrl: string;
    fileType: string;
    fileSize: number;
}

interface AdvanceChargeData {
    accountId: number | string;
    accountName: string;
    amount: number;
    remark: string;
}

interface AddOrUpdateAdvanceReq {
    id: number | string | undefined;
    memberId: number | string;
    receiptDate: string;
    receiptNumber: string;
    financialPersonnelId: number | string;
    totalAmount: number;
    collectedAmount: number;
    remark: string;
    tableData: AdvanceChargeData[];
    fileDataList: FileData[];
    review: number;
}

interface QueryAdvanceReq {
    receiptNumber: string;
    memberId: number | string;
    operatorId: number | string;
    financialPersonnelId: number | string;
    status: number;
    remark: string;
}

interface AdvanceChargeResp {
    id: number | string;
    memberName: string;
    receiptNumber: string;
    receiptDate: string;
    operator: string;
    financialPersonnel: string;
    totalAmount: number;
    collectedAmount: number;
    status: number;
    remark: string;
}

interface AdvanceChargeDetailResp {
    memberId: string;
    memberName: string;
    receiptNumber: string;
    receiptDate: string;
    financialPersonnel: string;
    financialPersonnelId: string;
    remark: string;
    totalAmount: number;
    collectedAmount: number;
    tableData: AdvanceChargeData[];
    files: FileData[];
}

export {
    AdvanceChargeData,
    AddOrUpdateAdvanceReq,
    QueryAdvanceReq,
    AdvanceChargeResp,
    AdvanceChargeDetailResp
}