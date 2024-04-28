import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {h} from 'vue';
import {Switch} from "ant-design-vue";
import {useMessage} from "@/hooks/web/useMessage";
import {useI18n} from "@/hooks/web/useI18n";
import {updateAccountStatus} from "@/api/financial/account";

const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: t('basic.settlement.table.accountNumber'),
        dataIndex: 'accountNumber',
        width: 90,
    },
    {
        title: t('basic.settlement.table.accountName'),
        dataIndex: 'accountName',
        width: 120,
    },
    {
        title: t('basic.settlement.table.openingAmount'),
        dataIndex: 'initialAmount',
        width: 120,
    },
    {
        title: t('basic.settlement.table.currentBalance'),
        dataIndex: 'currentAmount',
        width: 120,
    },
    {
        title: t('basic.settlement.table.status'),
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
                    updateAccountStatus([record.id], newStatus )
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
        title: t('basic.settlement.table.sort'),
        dataIndex: 'sort',
        width: 80,
    },
    {
        title: t('basic.settlement.table.default'),
        dataIndex: 'isDefault',
        width: 80,
    },
    {
        title: t('basic.settlement.table.createTime'),
        dataIndex: 'createTime',
        width: 150,
    }
]

export const searchFormSchema: FormSchema[] = [
    {
        label: t('basic.settlement.header.accountNumber'),
        field: 'accountNumber',
        component: 'Input',
        colProps: { span: 5 },
    },
    {
        label: t('basic.settlement.header.accountName'),
        field: 'accountName',
        component: 'Input',
        colProps: { span: 5 },
    }
]

export const formSchema: FormSchema[] = [
    {
        label: t('basic.settlement.form.accountName'),
        field: 'accountName',
        component: 'Input',
        required: true,
    },
    {
        label: t('basic.settlement.form.accountNumber'),
        field: 'accountNumber',
        component: 'Input',
    },
    {
        label: t('basic.settlement.form.openingAmount'),
        field: 'initialAmount',
        component: 'InputNumber',
    },
    {
        label: t('basic.settlement.form.currentBalance'),
        field: 'currentAmount',
        component: 'InputNumber',
    },
    {
        label: t('basic.settlement.form.default'),
        field: 'isDefault',
        helpMessage: t('basic.settlement.form.notice'),
        component: 'RadioGroup',
        defaultValue: 0,
        componentProps: {
            options: [
                {
                    label: t('basic.settlement.no'),
                    value: 0,
                },
                {
                    label: t('basic.settlement.yes'),
                    value: 1,
                },
            ],
        },
    },
    {
        label: t('basic.settlement.form.remark'),
        field: 'remark',
        component: 'InputTextArea',
    },
    {
        label: t('basic.settlement.form.sort'),
        field: 'sort',
        component: 'InputNumber',
    }
]