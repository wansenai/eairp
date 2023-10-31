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

export interface SupplierResp {
    id: number | string;
    supplierName: string;
    contact: string;
    contactNumber: string;
    phoneNumber: string;
    address: string;
    email: string;
    status: number;
    firstQuarterAccountReceivable: number;
    secondQuarterAccountReceivable: number;
    thirdQuarterAccountReceivable: number;
    fourthQuarterAccountReceivable: number;
    totalAccountReceivable: number;
    firstQuarterAccountPayment: number;
    secondQuarterAccountPayment: number;
    thirdQuarterAccountPayment: number;
    fourthQuarterAccountPayment: number;
    totalAccountPayment: number;
    fax: string;
    taxNumber: string;
    bankName: string;
    accountNumber: string;
    taxRate: string;
    sort: number;
    remark: string;
    createTime: string;
}

export interface AddSupplierReq {
    supplierName: string;
    contact: string;
    contactNumber: string;
    phoneNumber: string;
    address: string;
    email: string;
    status: number;
    firstQuarterAccountReceivable: number;
    secondQuarterAccountReceivable: number;
    thirdQuarterAccountReceivable: number;
    fourthQuarterAccountReceivable: number;
    firstQuarterAccountPayment: number;
    secondQuarterAccountPayment: number;
    thirdQuarterAccountPayment: number;
    fourthQuarterAccountPayment: number;
    fax: string;
    taxNumber: string;
    bankName: string;
    accountNumber: string;
    taxRate: string;
    sort: number;
    remark: string;
}

export interface UpdateSupplierReq {
    id: number | string;
    supplierName: string;
    contact: string;
    contactNumber: string;
    phoneNumber: string;
    address: string;
    email: string;
    status: number;
    firstQuarterAccountReceivable: number;
    secondQuarterAccountReceivable: number;
    thirdQuarterAccountReceivable: number;
    fourthQuarterAccountReceivable: number;
    totalAccountReceivable: number;
    firstQuarterAccountPayment: number;
    secondQuarterAccountPayment: number;
    thirdQuarterAccountPayment: number;
    fourthQuarterAccountPayment: number;
    totalAccountPayment: number;
    fax: string;
    taxNumber: string;
    bankName: string;
    accountNumber: string;
    taxRate: string;
    sort: number;
    remark: string;
}

export interface QuerySupplierReq {
    supplierName: string;
    contact: string;
    contactNumber: string;
    phoneNumber: string;
}