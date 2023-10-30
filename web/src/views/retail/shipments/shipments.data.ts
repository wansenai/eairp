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

import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {useI18n} from "@/hooks/web/useI18n";

export const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: '会员',
        dataIndex: 'productBarcode',
        width: 80,
    },
    {
        title: '单据编号',
        dataIndex: 'productName',
        width: 100,
    },
    {
        title: '商品信息',
        dataIndex: 'productStandard',
        width: 80,
    },
    {
        title: '数量',
        dataIndex: 'productModel',
        width: 100,
    },
    {
        title: '金额合计',
        dataIndex: 'productColor',
        width: 60,
    },
    {
        title: '收款金额',
        dataIndex: 'productCategoryName',
        width: 80,
    },
    {
        title: '找零金额',
        dataIndex: 'productUnit',
        width: 80,
    },
    {
        title: '单据日期',
        dataIndex: 'productStock',
        width: 60,
    },
    {
        title: '操作员',
        dataIndex: 'purchasePrice',
        width: 60,
    },
    {
        title: '状态',
        dataIndex: 'status',
        width: 80,
        customRender: ({ record }) => {
        }
    },
]

export const searchFormSchema: FormSchema[] = [
    {
        label: '单据编号',
        field: 'productCategoryId',
        component: 'ApiTreeSelect',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: '商品信息',
        field: 'keywords',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        field: '[startDate, endDate]',
        label: '单据日期',
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: ['开始日期', '结束日期'],
        },
        colProps: { span: 7 },
    },
    {
        label: '会员卡号',
        field: 'warehouseManager',
        component: 'ApiSelect',
        componentProps: {
         //   api: test,
            resultField: 'data',
            labelField: 'name',
            valueField: 'id',
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: '仓库',
        field: 'warehouseManager',
        component: 'ApiSelect',
        componentProps: {
      //      api: test,
            resultField: 'data',
            labelField: 'name',
            valueField: 'id',
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: '操作员',
        field: 'warehouseManager',
        component: 'ApiSelect',
        componentProps: {
      //      api: test,
            resultField: 'data',
            labelField: 'name',
            valueField: 'id',
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: '结算账户',
        field: 'warehouseManager',
        component: 'ApiSelect',
        componentProps: {
    //        api: test,
            resultField: 'data',
            labelField: 'name',
            valueField: 'id',
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: '单据状态',
        field: 'status',
        component: 'Select',
        colProps: {
            xl: 8,
            xxl: 8,
        },
        componentProps: {
            options: [
                { label: '未审核', value: 0, key: 0 },
                { label: '已审核', value: 1, key: 1 },
            ],
        },
    },
    {
        label: '单据备注',
        field: 'warehouseShelves',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },

    }
]