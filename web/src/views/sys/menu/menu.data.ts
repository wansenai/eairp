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

import { BasicColumn, FormSchema } from '/@/components/Table';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';
import Icon from '@/components/Icon/Icon.vue';
import {getMenuList} from "@/api/sys/menu";
import {ParentIdEnum} from "@/enums/appEnum";

export const columns: BasicColumn[] = [
    {
        title: '菜单名称',
        dataIndex: 'title',
        width: 150,
        align: 'left',
    },
    {
        title: '图标',
        dataIndex: 'icon',
        width: 80,
        customRender: ({ record }) => {
            return h(Icon, { icon: record.icon });
        },
    },
    {
        title: '路径',
        dataIndex: 'path',
        width: 180,
    },
    {
        title: '组件',
        dataIndex: 'component',
        width: 200,
    },
    {
        title: '排序',
        dataIndex: 'sort',
        width: 50,
    },
    {
        title: '状态',
        dataIndex: 'status',
        width: 80,
        customRender: ({ record }) => {
            const status = record.status;
            const enable = ~~status === 0;
            const color = enable ? 'green' : 'red';
            const text = enable ? '启用' : '停用';
            return h(Tag, { color: color }, () => text);
        },
    },
    {
        title: '创建时间',
        dataIndex: 'createTime',
        width: 180,
    },
];
const isMenu = (type: number) => type === 1;

export const searchFormSchema: FormSchema[] = [
    {
        field: 'title',
        label: '菜单名称',
        component: 'Input',
        colProps: { span: 8 },
    },
    {
        field: 'status',
        label: '状态',
        component: 'Select',
        componentProps: {
            options: [
                { label: '启用', value: 0 },
                { label: '停用', value: 1 },
            ],
        },
        colProps: { span: 8 },
    },
];

export const formSchema: FormSchema[] = [
    {
        field: 'id',
        label: '菜单ID',
        component: 'Input',
        show: false,
    },
    {
        field: 'menuType',
        label: '菜单类型',
        component: 'RadioButtonGroup',
        defaultValue: 1,
        componentProps: {
            options: [
                { label: '目录', value: 0 },
                { label: '菜单', value: 1 },
            ],
        },
        colProps: { lg: 24, md: 24 },
    },
    {
        field: 'name',
        label: '菜单名称',
        component: 'Input',
        required: true,
    },
    {
        field: 'title',
        label: '菜单标题',
        component: 'Input',
        required: true,
    },
    {
        field: 'parentId',
        label: '上级菜单',
        component: 'ApiTreeSelect',
        helpMessage: ['如果不填写，则默认为目录'],
        componentProps: {
            api: getMenuList,
            resultField: 'data.data',
            labelField: 'title',
            valueField: 'id',
            defaultValue: {
                id: ParentIdEnum.DEFAULT,
                parentId: -1,
                label: '根菜单',
                value: ParentIdEnum.DEFAULT,
            },
        },
    },
    {
        field: 'sort',
        label: '排序',
        component: 'InputNumber',
        required: true,
    },
    {
        field: 'icon',
        label: '图标',
        component: 'IconPicker',
        required: true,
    },

    {
        field: 'path',
        label: '路由地址',
        component: 'Input',
        required: true,
        ifShow: ({ values }) => isMenu(values.menuType),
    },
    {
        field: 'component',
        label: '组件路径',
        component: 'Input',
        ifShow: ({ values }) => isMenu(values.menuType),
    },
    {
        field: 'status',
        label: '状态',
        component: 'RadioButtonGroup',
        defaultValue: 0,
        componentProps: {
            options: [
                { label: '启用', value: 0 },
                { label: '禁用', value: 1 },
            ],
        },
    },
    {
        field: 'blank',
        label: '是否外链',
        component: 'RadioButtonGroup',
        defaultValue: 0,
        componentProps: {
            options: [
                { label: '否', value: 0 },
                { label: '是', value: 1 },
            ],
        },
        ifShow: ({ values }) => isMenu(values.menuType),
    },

    {
        field: 'ignoreKeepAlive',
        label: '是否缓存',
        component: 'RadioButtonGroup',
        defaultValue: 0,
        componentProps: {
            options: [
                { label: '否', value: 0 },
                { label: '是', value: 1 },
            ],
        },
        ifShow: ({ values }) => isMenu(values.menuType),
    },

    {
        field: 'hideMenu',
        label: '是否显示',
        component: 'RadioButtonGroup',
        defaultValue: 0,
        componentProps: {
            options: [
                { label: '是', value: 0 },
                { label: '否', value: 1 },
            ],
        },
        ifShow: ({ values }) => isMenu(values.menuType),
    },
];
