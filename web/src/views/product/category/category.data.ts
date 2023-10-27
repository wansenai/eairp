import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {getCategoryList} from "@/api/product/productCategory";
export const columns: BasicColumn[] = [
    {
        title: '分类名称',
        dataIndex: 'categoryName',
        width: 150,
        align: 'left',
    },
    {
        title: '分类编号',
        dataIndex: 'categoryNumber',
    },
    {
        title: '上级分类',
        dataIndex: 'parentName',
    },
    {
        title: '排序',
        dataIndex: 'sort',
    },
    {
        title: '备注',
        dataIndex: 'remark',
    },
    {
        title: '创建时间',
        dataIndex: 'createTime',
    }
]
export const CategorySchema: FormSchema[] = [
    {
        field: 'id',
        label: '分类id',
        component: 'Input',
        show: false,
    },
    {
        field: 'categoryName',
        label: '分类名称',
        component: 'Input',
        required: true,
    },
    {
        label: '分类编号',
        field: 'categoryNumber',
        component: 'Input',
        required: true,
    },
    {
        field: 'parentId',
        label: '上级分类',
        component: 'ApiTreeSelect',
        componentProps: {
            api: getCategoryList,
            resultField: 'data',
            labelField: 'categoryName',
            valueField: 'id',
        },
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
    },
]