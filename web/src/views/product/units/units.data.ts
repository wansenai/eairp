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
import {updateUnitStatus} from "@/api/product/productUnit";

const { t } = useI18n();
export const columns: BasicColumn[] = [
    {
        title: '计量单位',
        dataIndex: 'computeUnit',
        width: 230,
    },
    {
        title: '基本单位',
        dataIndex: 'basicUnit',
        width: 70,
    },
    {
        title: '副单位',
        dataIndex: 'otherComputeUnit',
        width: 70,
    },
    {
        title: '副单位二',
        dataIndex: 'otherComputeUnitTwo',
        width: 150,
    },
    {
        title: '副单位三',
        dataIndex: 'otherComputeUnitThree',
        width: 150,
    },
    {
        title: '状态',
        dataIndex: 'status',
        width: 150,
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
                    updateUnitStatus({id: record.id, status: newStatus} )
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
        title: '创建时间',
        dataIndex: 'createTime',
        width: 150,
    }
]

export const searchFormSchema: FormSchema[] = [
    {
        label: '计量单位',
        field: 'computeUnit',
        component: 'Input',
        colProps: { span: 8 },
    },
]

export const formSchema: FormSchema[] = [
    {
        label: '单位id',
        field: 'id',
        show: false,
        component: 'Input',
    },
    {
        label: '基本单位',
        field: 'basicUnit',
        component: 'Input',
        required: true,
        componentProps: {
            placeholder: '请输入基本单位(小单位)',
        },
    },
    {
        label: '副单位',
        field: 'otherUnit',
        component: 'Input',
        required: true,
        componentProps: {
            placeholder: '请输入副单位(大单位)',
        },
        colProps: {
            span: 13,
        },
    },
    {
        label: '=',
        field: 'ratio',
        component: 'InputNumber',
        labelWidth: 10,
        componentProps: {
            addonAfter: '基本单位',
            placeholder: '请输入比例',
        },
        colProps: {
            span: 11,
        },
    },
    {
        label: '副单位二',
        field: 'otherUnitTwo',
        component: 'Input',
        componentProps: {
            placeholder: '请输入副单位2(大单位)',
        },
        colProps: {
            span: 13,
        },
    },
    {
        label: '=',
        field: 'ratioTwo',
        labelWidth: 10,
        componentProps: {
            addonAfter: '基本单位',
            placeholder: '请输入比例2',
        },
        component: 'Input',
        colProps: {
            span: 11,
        }
    },
    {
        label: '副单位三',
        field: 'otherUnitThree',
        component: 'Input',
        componentProps: {
            placeholder: '请输入副单位2(大单位)',
        },
        colProps: {
            span: 13,
        },
    },
    {
        label: '=',
        labelWidth: 10,
        field: 'ratioThree',
        componentProps: {
            addonAfter: '基本单位',
            placeholder: '请输入比例3',
        },
        component: 'Input',
        colProps: {
            span: 11,
        }
    }
]