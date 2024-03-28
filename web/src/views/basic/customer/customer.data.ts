import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import { h } from 'vue';
import {Switch} from "ant-design-vue";
import {useMessage} from "@/hooks/web/useMessage";
import {useI18n} from "@/hooks/web/useI18n";
import {updateCustomerStatus} from "@/api/basic/customer";

const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: t('basic.customer.table.name'),
        dataIndex: 'customerName',
        width: 180,
    },
    {
        title: t('basic.customer.table.contact'),
        dataIndex: 'contact',
        width: 80,
    },
    {
        title: t('basic.customer.table.phoneNumber'),
        dataIndex: 'phoneNumber',
        width: 120,
    },
    {
        title: t('basic.customer.table.email'),
        dataIndex: 'email',
        width: 120,
    },
    {
        title: t('basic.customer.table.status'),
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
                    updateCustomerStatus([record.id], newStatus )
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
        title: t('basic.customer.table.accumulatedAccountsReceivable'),
        dataIndex: 'totalAccountReceivable',
        width: 90,
    },
    {
        title: t('basic.customer.table.rate'),
        dataIndex: 'taxRate',
        width: 80,
    },
    {
        title: t('basic.customer.table.sort'),
        dataIndex: 'sort',
        width: 80,
    },
    {
        title: t('basic.customer.table.createTime'),
        dataIndex: 'createTime',
        width: 150,
    }
]

export const searchFormSchema: FormSchema[] = [
    {
        label: t('basic.customer.header.name'),
        field: 'customerName',
        component: 'Input',
        colProps: { span: 5 },
    },
    {
        label: t('basic.customer.header.phoneNumber'),
        field: 'phoneNumber',
        component: 'Input',
        colProps: { span: 5 },
    },
    {
        field: '[startDate, endDate]',
        label: t('basic.customer.header.createTime'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('basic.customer.header.startDate'), t('basic.customer.header.endDate')],
        },
        colProps: { span: 7 },
    },
]

export const formSchema: FormSchema[] = [
    {
        label: t('basic.customer.form.name'),
        field: 'customerName',
        helpMessage: t('basic.customer.form.nameTip'),
        component: 'Input',
        colProps: {
            span: 11,
        },
        required: true,
    },
    {
        label: t('basic.customer.form.contact'),
        field: 'contact',
        component: 'Input',
        colProps: {
            span: 11,
        },
    },
    {
        label: t('basic.customer.form.phoneNumber'),
        field: 'phoneNumber',
        component: 'Input',
        colProps: {
            span: 11,
        },
        required: true,
    },
    {
        label: t('basic.customer.form.email'),
        field: 'email',
        component: 'Input',
        colProps: {
            span: 11,
        },
    },
    {
        label: t('basic.customer.form.fax'),
        field: 'fax',
        component: 'Input',
        colProps: {
            span: 11,
        },
    },
    {
        label: t('basic.customer.form.address'),
        field: 'address',
        component: 'InputTextArea',
        colProps: {
            span: 11,
        },
    },
    {
        label: t('basic.customer.form.remark'),
        field: 'remark',
        component: 'InputTextArea',
        colProps: {
            span: 11,
        },
    },
    {
        field: '',
        component: 'Divider',
        label: t('basic.customer.form.accountsReceivableInfo'),
        colProps: {
            span: 22,
        },
    },
    {
        label: t('basic.customer.form.firstQuarterCollection'),
        field: 'firstQuarterAccountReceivable',
        component: 'InputNumber',
        colProps: {
            span: 11,
        },
    },
    {
        label: t('basic.customer.form.secondQuarterCollection'),
        field: 'secondQuarterAccountReceivable',
        component: 'InputNumber',
        colProps: {
            span: 11,
        },
    },
    {
        label: t('basic.customer.form.thirdQuarterCollection'),
        field: 'thirdQuarterAccountReceivable',
        component: 'InputNumber',
        colProps: {
            span: 11,
        },
    },
    {
        label: t('basic.customer.form.fourthQuarterCollection'),
        field: 'fourthQuarterAccountReceivable',
        component: 'InputNumber',
        colProps: {
            span: 11,
        },
    },
    {
        field: '',
        component: 'Divider',
        label: t('basic.customer.form.accountInfo'),
        colProps: {
            span: 22,
        },
    },
    {
        label: t('basic.customer.form.taxNumber'),
        field: 'taxNumber',
        component: 'Input',
        colProps: {
            span: 11,
        },
    },
    {
        label: t('basic.customer.form.rate'),
        field: 'taxRate',
        component: 'InputNumber',
        colProps: {
            span: 11,
        },
    },
    {
        label: t('basic.customer.form.bankName'),
        field: 'bankName',
        component: 'Input',
        colProps: {
            span: 11,
        },
    },
    {
        label: t('basic.customer.form.bankAccount'),
        field: 'accountNumber',
        component: 'InputNumber',
        colProps: {
            span: 11,
        },
    }
]