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
import { h } from 'vue';
import {Switch} from "ant-design-vue";
import {useMessage} from "@/hooks/web/useMessage";
import {useI18n} from "@/hooks/web/useI18n";
import {updateCustomerStatus} from "@/api/basic/customer";

const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: '客户名称',
        dataIndex: 'customerName',
        width: 180,
    },
    {
        title: '联系人',
        dataIndex: 'contact',
        width: 80,
    },
    {
        title: '手机号',
        dataIndex: 'phoneNumber',
        width: 120,
    },
    {
        title: '电子邮箱',
        dataIndex: 'email',
        width: 120,
    },
    {
        title: '状态',
        dataIndex: 'status',
        width: 100,
        customRender: ({ record }) => {
            if (!Reflect.has(record, 'pendingStatus')) {
                record.pendingStatus = false;
            }
            return h(Switch, {
                checked: record.status === 0,
                checkedChildren: t('common.on'),
                unCheckedChildren: t('common.off'),
                loading: record.pendingStatus,
                onChange(checked, _) {
                    const {createMessage} = useMessage();
                    if (record.id == 1) {
                        createMessage.warn(t('common.notice'));
                        return;
                    }
                    record.pendingStatus = true;
                    const newStatus = checked ? 0 : 1;
                    updateCustomerStatus([record.id], newStatus )
                        .then(() => {
                            record.status = newStatus;
                        })
                        .finally(() => {
                            record.pendingStatus = false;
                        });
                },
            });
        }
    },
    {
        title: '累计应收账款',
        dataIndex: 'totalAccountReceivable',
        width: 90,
    },
    {
        title: '税率(%)',
        dataIndex: 'taxRate',
        width: 80,
    },
    {
        title: '排序',
        dataIndex: 'sort',
        width: 80,
    },
    {
        title: '创建时间',
        dataIndex: 'createTime',
        width: 150,
    }
]

export const searchFormSchema: FormSchema[] = [
    {
        label: '客户名称',
        field: 'customerName',
        component: 'Input',
        colProps: { span: 5 },
    },
    {
        label: '手机号码',
        field: 'phoneNumber',
        component: 'Input',
        colProps: { span: 5 },
    },
    {
        field: '[startDate, endDate]',
        label: '时间',
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: ['开始日期', '结束日期'],
        },
        colProps: { span: 7 },
    },
]

export const formSchema: FormSchema[] = [
    {
        label: '客户名称',
        field: 'customerName',
        helpMessage: '可以是个人，如果是个人可以不用填写联系人',
        component: 'Input',
        colProps: {
            span: 11,
        },
        required: true,
    },
    {
        label: '联系人',
        field: 'contact',
        component: 'Input',
        colProps: {
            span: 11,
        },
    },
    {
        label: '手机号码',
        field: 'phoneNumber',
        component: 'Input',
        colProps: {
            span: 11,
        },
        required: true,
    },
    {
        label: '电子邮箱',
        field: 'email',
        component: 'Input',
        colProps: {
            span: 11,
        },
    },
    {
        label: '传真',
        field: 'fax',
        component: 'Input',
        colProps: {
            span: 11,
        },
    },
    {
        label: '地址',
        field: 'address',
        component: 'InputTextArea',
        colProps: {
            span: 11,
        },
    },
    {
        label: '备注',
        field: 'remark',
        component: 'InputTextArea',
        colProps: {
            span: 11,
        },
    },
    {
        field: '',
        component: 'Divider',
        label: '应收账款信息',
        colProps: {
            span: 22,
        },
    },
    {
        label: '一季度收款',
        field: 'firstQuarterAccountReceivable',
        component: 'InputNumber',
        colProps: {
            span: 11,
        },
    },
    {
        label: '二季度收款',
        field: 'secondQuarterAccountReceivable',
        component: 'InputNumber',
        colProps: {
            span: 11,
        },
    },
    {
        label: '三季度收款',
        field: 'thirdQuarterAccountReceivable',
        component: 'InputNumber',
        colProps: {
            span: 11,
        },
    },
    {
        label: '四季度收款',
        field: 'fourthQuarterAccountReceivable',
        component: 'InputNumber',
        colProps: {
            span: 11,
        },
    },
    {
        field: '',
        component: 'Divider',
        label: '账户信息',
        colProps: {
            span: 22,
        },
    },
    {
        label: '纳税人识别号',
        field: 'taxNumber',
        component: 'Input',
        colProps: {
            span: 11,
        },
    },
    {
        label: '税率(%)',
        field: 'taxRate',
        component: 'InputNumber',
        colProps: {
            span: 11,
        },
    },
    {
        label: '开户行',
        field: 'bankName',
        component: 'Input',
        colProps: {
            span: 11,
        },
    },
    {
        label: '银行账号',
        field: 'accountNumber',
        component: 'InputNumber',
        colProps: {
            span: 11,
        },
    }
]