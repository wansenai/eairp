import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {getCategoryList} from "@/api/product/productCategory";
import {useI18n} from "@/hooks/web/useI18n";
export const { t } = useI18n();
export const columns: BasicColumn[] = [
    {
        title: t('product.category.table.categoryName'),
        dataIndex: 'categoryName',
        width: 150,
        align: 'left',
    },
    {
        title: t('product.category.table.categoryNumber'),
        dataIndex: 'categoryNumber',
    },
    {
        title: t('product.category.table.categoryParent'),
        dataIndex: 'parentName',
    },
    {
        title: t('product.category.table.sort'),
        dataIndex: 'sort',
    },
    {
        title: t('product.category.table.remark'),
        dataIndex: 'remark',
    },
    {
        title: t('product.category.table.createTime'),
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
        label: t('product.category.table.categoryName'),
        component: 'Input',
        required: true,
    },
    {
        label: t('product.category.table.categoryNumber'),
        field: 'categoryNumber',
        component: 'Input',
        required: true,
    },
    {
        field: 'parentId',
        label: t('product.category.table.categoryParent'),
        component: 'ApiTreeSelect',
        componentProps: {
            api: getCategoryList,
            resultField: 'data',
            labelField: 'categoryName',
            valueField: 'id',
        },
    },
    {
        label: t('product.category.table.sort'),
        field: 'sort',
        component: 'InputNumber',
    },
    {
        label: t('product.category.table.remark'),
        field: 'remark',
        component: 'InputTextArea',
    },
]