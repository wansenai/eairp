import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import { h } from 'vue';
import {Switch} from "ant-design-vue";
import {useMessage} from "@/hooks/web/useMessage";
import {useI18n} from "@/hooks/web/useI18n";
import {updateOperatorStatus} from "@/api/basic/operator";

const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: t('basic.operator.table.name'),
        dataIndex: 'name',
        width: 90,
    },
    {
        title: t('basic.operator.table.type'),
        dataIndex: 'type',
        width: 120,
    },
    {
        title: t('basic.operator.table.status'),
        dataIndex: 'status',
        width: 100,
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
                    updateOperatorStatus([record.id], newStatus )
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
        title: t('basic.operator.table.sort'),
        dataIndex: 'sort',
        width: 80,
    },
    {
        title: t('basic.operator.table.createTime'),
        dataIndex: 'createTime',
        width: 150,
    }
]

export const searchFormSchema: FormSchema[] = [
    {
        label: t('basic.operator.header.name'),
        field: 'name',
        component: 'Input',
        colProps: { span: 5 },
    },
    {
        label: t('basic.operator.header.type'),
        field: 'type',
        component: 'Select',
        colProps: { span: 5 },
        componentProps: {
            options: [
                { label: t('basic.operator.businessPerson'), value: '业务员', key: 0 },
                { label: t('basic.operator.financialPerson'), value: '财务员', key: 1 },
                { label: t('basic.operator.salesPerson'), value: '销售员', key: 2 },
            ],
        },
    }
]

export const formSchema: FormSchema[] = [
    {
        label: t('basic.operator.form.name'),
        field: 'name',
        component: 'Input',
        required: true,
    },
    {
        label: t('basic.operator.form.type'),
        field: 'type',
        component: 'Select',
        componentProps: {
            options: [
                { label: t('basic.operator.businessPerson'), value: '业务员', key: 0 },
                { label: t('basic.operator.financialPerson'), value: '财务员', key: 1 },
                { label: t('basic.operator.salesPerson'), value: '销售员', key: 2 },
            ],
        },
        required: true,
    },
    {
        label: t('basic.operator.form.sort'),
        field: 'sort',
        component: 'InputNumber',
    },
    {
        label: t('basic.operator.form.remark'),
        field: 'remark',
        component: 'InputTextArea',
    }
]