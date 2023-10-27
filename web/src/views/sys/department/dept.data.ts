import {BasicColumn, FormSchema} from "@/components/Table";
import { h } from 'vue';
import { Tag } from 'ant-design-vue';
import {getDeptList} from "@/api/sys/dept";

export const columns: BasicColumn[] = [
    {
        title: '部门名称',
        dataIndex: 'deptName',
        width: 160,
        align: "left",
    },
    {
        title: '部门编号',
        dataIndex: 'deptNumber',
        width: 160,
    },
    {
        title: '部门负责人',
        dataIndex: 'leader',
        width: 160,
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
    {
        title: '备注',
        dataIndex: 'remark',
        width: 180
    },
]

export const searchFormSchema: FormSchema[] = [
    {
        field: 'deptName',
        label: '部门名称',
        component: 'Input',
        colProps: { span: 8 },
    }
]

export const formSchema: FormSchema[] = [
    {
        field: 'id',
        label: '部门ID',
        component: 'Input',
        show: false,
    },
    {
        field: 'deptName',
        label: '部门名称',
        component: 'Input',
        required: true,
    },
    {
        field: 'deptNumber',
        label: '部门编号',
        component: 'Input',
    },
    {
        field: 'parentId',
        label: '上级部门',
        component: 'ApiTreeSelect',
        helpMessage: ['如果不填写，则默认为父级部门'],
        componentProps: {
            api: getDeptList,
            resultField: 'data',
            labelField: 'deptName',
            valueField: 'id',
            childrenKeyField: 'children',
        },
    },
    {
        field: 'leader',
        label: '部门负责人',
        component: 'Input',
    },
    {
        field: 'status',
        label: '状态',
        component: 'RadioButtonGroup',
        defaultValue: 0,
        componentProps: {
            options: [
                { label: '启用', value: 0 },
                { label: '停用', value: 1 },
            ],
        },
        required: true,
    },

    {
        field: 'sort',
        label: '排序',
        component: 'InputNumber',
    },
    {
        label: '备注',
        field: 'remark',
        component: 'InputTextArea',
    },
];