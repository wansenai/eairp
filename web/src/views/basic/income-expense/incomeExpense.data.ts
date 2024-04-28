import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {useI18n} from "@/hooks/web/useI18n";

const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: t('basic.incomeExpense.table.name'),
        dataIndex: 'name',
        width: 90,
    },
    {
        title: t('basic.incomeExpense.table.type'),
        dataIndex: 'type',
        width: 120,
    },
    {
        title: t('basic.incomeExpense.table.remark'),
        dataIndex: 'remark',
        width: 200,
    },
    {
        title: t('basic.incomeExpense.table.sort'),
        dataIndex: 'sort',
        width: 80,
    },
    {
        title: t('basic.incomeExpense.table.status'),
        dataIndex: 'status',
        width: 80,
    },
    {
        title: t('basic.incomeExpense.table.createTime'),
        dataIndex: 'createTime',
        width: 150,
    }
]

export const searchFormSchema: FormSchema[] = [
    {
        label: t('basic.incomeExpense.header.name'),
        field: 'name',
        component: 'Input',
        colProps: { span: 7 },
    },
    {
        label: t('basic.incomeExpense.header.type'),
        field: 'type',
        component: 'Select',
        colProps: { span: 7 },
        componentProps: {
            options: [
                { label: t('basic.incomeExpense.income'), value: '收入', key: 0 },
                { label: t('basic.incomeExpense.expense'), value: '支出', key: 1 },
            ],
        },
    },
    {
        label: t('basic.incomeExpense.header.remark'),
        field: 'remark',
        component: 'Input',
        colProps: { span: 7 },
    },
]

export const formSchema: FormSchema[] = [
    {
        label: t('basic.incomeExpense.form.name'),
        field: 'name',
        component: 'Input',
        required: true,
    },
    {
        label: t('basic.incomeExpense.form.type'),
        field: 'type',
        component: 'Select',
        componentProps: {
            options: [
                { label:  t('basic.incomeExpense.income'), value: '收入', key: 0 },
                { label: t('basic.incomeExpense.expense'), value: '支出', key: 1 },
            ],
        },
        required: true,
    },
    {
        label: t('basic.incomeExpense.form.sort'),
        field: 'sort',
        component: 'InputNumber',
    },
    {
        label: t('basic.incomeExpense.form.remark'),
        field: 'remark',
        component: 'InputTextArea',
    }
]