import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {useI18n} from "@/hooks/web/useI18n";

export const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: t('product.attribute.table.attributeName'),
        dataIndex: 'attributeName',
        width: 100,
    },
    {
        title: t('product.attribute.table.attributeValue'),
        dataIndex: 'attributeValue',
        width: 150,
    },
    {
        title: t('product.attribute.table.sort'),
        dataIndex: 'sort',
        width: 80,
    },
    {
        title: t('product.attribute.table.remark'),
        dataIndex: 'remark',
        width: 150,
    },
    {
        title: t('product.attribute.table.createTime'),
        dataIndex: 'createTime',
        width: 150,
    }
]

export const searchFormSchema: FormSchema[] = [
    {
        label: t('product.attribute.table.attributeName'),
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
        label: t('product.attribute.table.attributeName'),
        field: 'attributeName',
        component: 'Input',
        required: true,
    },
    {
        label: t('product.attribute.table.attributeValue'),
        helpMessage: '多个属性值用|隔开',
        field: 'attributeValue',
        component: 'InputTextArea',
        required: true,
    },
    {
        label: t('product.attribute.table.sort'),
        field: 'sort',
        component: 'InputNumber',
    },
    {
        label: t('product.attribute.table.remark'),
        field: 'remark',
        component: 'InputTextArea',
    }
]