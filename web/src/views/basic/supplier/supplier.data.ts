import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import { h } from 'vue';
import {Switch} from "ant-design-vue";
import {useMessage} from "@/hooks/web/useMessage";
import {useI18n} from "@/hooks/web/useI18n";
import {updateSupplierStatus} from "@/api/basic/supplier";

const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: t('basic.supplier.table.name'),
        dataIndex: 'supplierName',
        width: 180,
    },
    {
        title: t('basic.supplier.table.contact'),
        dataIndex: 'contact',
        width: 80,
    },
    {
        title: t('basic.supplier.table.phoneNumber'),
        dataIndex: 'phoneNumber',
        width: 120,
    },
    {
        title: t('basic.supplier.table.email'),
        dataIndex: 'email',
        width: 120,
    },
    {
        title: t('basic.supplier.table.status'),
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
                    updateSupplierStatus({ids: [record.id], status: newStatus} )
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
        title: t('basic.supplier.table.accumulatedAccountsPayable'),
        dataIndex: 'totalAccountPayment',
        width: 90,
    },
    {
        title: t('basic.supplier.table.rate'),
        dataIndex: 'taxRate',
        width: 80,
    },
    {
        title: t('basic.supplier.table.sort'),
        dataIndex: 'sort',
        width: 80,
    },
    {
        title: t('basic.supplier.table.createTime'),
        dataIndex: 'createTime',
        width: 150,
    }
]

export const searchFormSchema: FormSchema[] = [
    {
        label: t('basic.supplier.header.name'),
        field: 'supplierName',
        component: 'Input',
        colProps: { span: 5 },
    },
    {
        label: t('basic.supplier.header.contactPhone'),
        field: 'contactNumber',
        component: 'Input',
        colProps: { span: 5 },
    },
    {
        field: '[startDate, endDate]',
        label: t('basic.supplier.header.createTime'),
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY/MM/DD',
          placeholder: [t('basic.supplier.header.startDate'), t('basic.supplier.header.endDate')],
        },
        colProps: { span: 7 },
    },
]

export const formSchema: FormSchema[] = [
    {
        label: t('basic.supplier.form.name'),
        field: 'supplierName',
        helpMessage: t('basic.supplier.form.notice'),
        component: 'Input',
        colProps: {
            span: 11,
        },
        required: true,
    },
    {
        label: t('basic.supplier.form.contact'),
        field: 'contact',
        component: 'Input',
        colProps: {
            span: 11,
        },
        required: true,
    },
    {
        label: t('basic.supplier.form.phoneNumber'),
        field: 'phoneNumber',
        component: 'Input',
        colProps: {
            span: 11,
        },
        required: true,
    },
    {
        label: t('basic.supplier.form.contactPhone'),
        helpMessage: t('basic.supplier.form.noticeTwo'),
        field: 'contactNumber',
        component: 'Input',
        colProps: {
            span: 11,
        },
    },
    {
        label: t('basic.supplier.form.email'),
        field: 'email',
        component: 'Input',
        colProps: {
            span: 11,
        },
    },
    {
        label: t('basic.supplier.form.fax'),
        field: 'fax',
        component: 'Input',
        colProps: {
            span: 11,
        },
    },
    {
        label: t('basic.supplier.form.address'),
        field: 'address',
        component: 'InputTextArea',
        colProps: {
            span: 11,
        },
    },
    {
        label: t('basic.supplier.form.remark'),
        field: 'remark',
        component: 'InputTextArea',
        colProps: {
            span: 11,
        },
    },
    {
        field: '',
        component: 'Divider',
        label: t('basic.supplier.form.accountsPayableInfo'),
        colProps: {
          span: 22,
        },
    },
    {
        label: t('basic.supplier.form.firstQuarterPayment'),
        field: 'firstQuarterAccountPayment',
        component: 'InputNumber',
        colProps: {
            span: 11,
        },
    },
    {
        label: t('basic.supplier.form.secondQuarterPayment'),
        field: 'secondQuarterAccountPayment',
        component: 'InputNumber',
        colProps: {
            span: 11,
        },
    },
    {
        label: t('basic.supplier.form.thirdQuarterPayment'),
        field: 'thirdQuarterAccountPayment',
        component: 'InputNumber',
        colProps: {
            span: 11,
        },
    },
    {
        label: t('basic.supplier.form.fourthQuarterPayment'),
        field: 'fourthQuarterAccountPayment',
        component: 'InputNumber',
        colProps: {
            span: 11,
        },
    },
    {
        field: '',
        component: 'Divider',
        label: t('basic.supplier.form.accountInfo'),
        colProps: {
          span: 22,
        },
    },
    {
        label: t('basic.supplier.form.taxNumber'),
        field: 'taxNumber',
        component: 'Input',
        colProps: {
            span: 11,
        },
    },
    {
        label: t('basic.supplier.form.rate'),
        field: 'taxRate',
        component: 'InputNumber',
        colProps: {
            span: 11,
        },
    },
    {
        label: t('basic.supplier.form.bankName'),
        field: 'bankName',
        component: 'Input',
        colProps: {
            span: 11,
        },
    },
    {
        label: t('basic.supplier.form.bankAccount'),
        field: 'accountNumber',
        component: 'InputNumber',
        colProps: {
            span: 11,
        },
    }
]