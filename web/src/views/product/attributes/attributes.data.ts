import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";

export const columns: BasicColumn[] = [
    {
        title: '属性名称',
        dataIndex: 'attributeName',
        width: 100,
    },
    {
        title: '属性值 (用|隔开)',
        dataIndex: 'attributeValue',
        width: 150,
    },
    {
        title: '排序',
        dataIndex: 'sort',
        width: 80,
    },
    {
        title: '备注',
        dataIndex: 'remark',
        width: 150,
    },
    {
        title: '创建时间',
        dataIndex: 'createTime',
        width: 150,
    }
]

export const searchFormSchema: FormSchema[] = [
    {
        label: '属性名称',
        field: 'attributeName',
        component: 'Input',
        colProps: { span: 8 },
    },
]

export const formSchema: FormSchema[] = [
    {
        label: '属性id',
        field: 'id',
        show: false,
        component: 'Input',
    },
    {
        label: '属性名称',
        field: 'attributeName',
        component: 'Input',
        required: true,
    },
    {
        label: '属性值',
        helpMessage: '多个属性值用|隔开',
        field: 'attributeValue',
        component: 'InputTextArea',
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