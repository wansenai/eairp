import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";

export const columns: BasicColumn[] = [
    {
        title: '名称',
        dataIndex: 'name',
        width: 90,
    },
    {
        title: '类型',
        dataIndex: 'type',
        width: 120,
    },
    {
        title: '备注',
        dataIndex: 'remark',
        width: 200,
    },
    {
        title: '排序',
        dataIndex: 'sort',
        width: 80,
    },
    {
        title: '状态',
        dataIndex: 'status',
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
        label: '名称',
        field: 'name',
        component: 'Input',
        colProps: { span: 7 },
    },
    {
        label: '类型',
        field: 'type',
        component: 'Select',
        colProps: { span: 7 },
        componentProps: {
            options: [
                { label: '收入', value: '收入', key: 0 },
                { label: '支出', value: '支出', key: 1 },
            ],
        },
    },
    {
        label: '备注',
        field: 'remark',
        component: 'Input',
        colProps: { span: 7 },
    },
]

export const formSchema: FormSchema[] = [
    {
        label: '名称',
        field: 'name',
        component: 'Input',
        required: true,
    },
    {
        label: '类型',
        field: 'type',
        component: 'Select',
        componentProps: {
            options: [
                { label: '收入', value: '收入', key: 0 },
                { label: '支出', value: '支出', key: 1 },
            ],
        },
        required: true,
    },
    {
        label: '排序',
        field: 'sort',
        component: 'InputNumber',
    },
    {
        label: '备注',
        field: 'remark',
        component: 'InputTextArea',
    }
]