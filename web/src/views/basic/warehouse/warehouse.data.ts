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
import {updateWarehouseStatus} from "@/api/basic/warehouse";
import {getTenantUserList} from "@/api/sys/user";

const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: '仓库名称',
        dataIndex: 'warehouseName',
        width: 180,
    },
    {
        title: '仓库地址',
        dataIndex: 'address',
        width: 80,
    },
    {
        title: '仓储费',
        dataIndex: 'price',
        width: 120,
    },
    {
        title: '搬运费',
        dataIndex: 'truckage',
        width: 120,
    },
    {
        title: '负责人',
        dataIndex: 'warehouseManagerName',
        width: 90,
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
                    updateWarehouseStatus([record.id], newStatus )
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
        title: '默认仓库',
        dataIndex: 'isDefault',
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
        label: '仓库名称',
        field: 'warehouseName',
        component: 'Input',
        colProps: { span: 5 },
    },
    {
        label: '备注',
        field: 'remark',
        component: 'Input',
        colProps: { span: 5 },
    }
]

export const formSchema: FormSchema[] = [
    {
        label: '仓库名称',
        field: 'warehouseName',
        component: 'Input',
        required: true,
    },
    {
        label: '仓库地址',
        field: 'address',
        component: 'Input',
    },
    {
        label: '仓储费',
        field: 'price',
        component: 'InputNumber',
    },
    {
        label: '搬运费',
        field: 'truckage',
        component: 'InputNumber',
    },
    {
        label: '默认仓库',
        field: 'isDefault',
        helpMessage: '只允许有一个默认仓库，如果选择是，之前的默认仓库将会变成非默认仓库',
        component: 'RadioGroup',
        defaultValue: 0,
        componentProps: {
            options: [
                {
                    label: '不是',
                    value: 0,
                },
                {
                    label: '是',
                    value: 1,
                },
            ],
        },
    },
    {
        field: 'warehouseManager',
        label: '负责人',
        component: 'ApiSelect',
        helpMessage: ['用户列表中的用户'],
        componentProps: {
            api: getTenantUserList,
            resultField: 'data',
            labelField: 'name',
            valueField: 'id',
        },
    },
    {
        label: '备注',
        field: 'remark',
        component: 'InputTextArea',
    },
    {
        label: '排序',
        field: 'sort',
        component: 'InputNumber',
    }
]