import {BasicColumn, FormSchema} from "@/components/Table";
import { h } from 'vue';
import { Tag } from 'ant-design-vue';
import {getDeptList} from "@/api/sys/dept";
import {useI18n} from "@/hooks/web/useI18n";

export const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: t('system.department.table.name'),
        dataIndex: 'deptName',
        width: 160,
        align: "left",
    },
    {
        title: t('system.department.table.number'),
        dataIndex: 'deptNumber',
        width: 160,
    },
    {
        title: t('system.department.table.manager'),
        dataIndex: 'leader',
        width: 160,
    },
    {
        title: t('system.department.table.status'),
        dataIndex: 'status',
        width: 80,
        customRender: ({ record }) => {
            const status = record.status;
            const enable = ~~status === 0;
            const color = enable ? 'green' : 'red';
            const text = enable ? t('system.department.form.enable') : t('system.department.form.disable');
            return h(Tag, { color: color }, () => text);
        },
    },
    {
        title: t('system.department.table.createTime'),
        dataIndex: 'createTime',
        width: 180,
    },
    {
        title: t('system.department.table.remark'),
        dataIndex: 'remark',
        width: 180
    },
]

export const searchFormSchema: FormSchema[] = [
    {
        field: 'deptName',
        label: t('system.department.header.name'),
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
        label: t('system.department.form.name'),
        component: 'Input',
        required: true,
    },
    {
        field: 'deptNumber',
        label: t('system.department.form.number'),
        component: 'Input',
    },
    {
        field: 'parentId',
        label: t('system.department.form.parent'),
        component: 'ApiTreeSelect',
        helpMessage: [t('system.department.form.notice')],
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
        label:  t('system.department.form.manager'),
        component: 'Input',
    },
    {
        field: 'status',
        label: t('system.department.form.status'),
        component: 'RadioButtonGroup',
        defaultValue: 0,
        componentProps: {
            options: [
                { label: t('system.department.form.enable'), value: 0 },
                { label: t('system.department.form.disable'), value: 1 },
            ],
        },
        required: true,
    },

    {
        field: 'sort',
        label: t('system.department.form.sort'),
        component: 'InputNumber',
    },
    {
        label: t('system.department.form.remark'),
        field: 'remark',
        component: 'InputTextArea',
    },
];