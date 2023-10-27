import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import { h } from 'vue';
import {Switch} from "ant-design-vue";
import {useMessage} from "@/hooks/web/useMessage";
import {useI18n} from "@/hooks/web/useI18n";
import {updateAccountStatus} from "@/api/financial/account";

const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: '账户编号',
        dataIndex: 'accountNumber',
        width: 90,
    },
    {
        title: '账户名称',
        dataIndex: 'accountName',
        width: 120,
    },
    {
        title: '期初金额',
        dataIndex: 'initialAmount',
        width: 120,
    },
    {
        title: '当前余额',
        dataIndex: 'currentAmount',
        width: 120,
    },
    {
        title: '状态',
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
        title: '排序',
        dataIndex: 'sort',
        width: 80,
    },
    {
        title: '是否默认账户',
        dataIndex: 'isDefault',
        width: 80,
    },
    {
        title: '创建时间',
        dataIndex: 'createTime',
        width: 150,
    }
]

export const searchFormSchema: FormSchema[] = [
    {
        label: '账户编号',
        field: 'accountNumber',
        component: 'Input',
        colProps: { span: 5 },
    },
    {
        label: '账户名称',
        field: 'accountName',
        component: 'Input',
        colProps: { span: 5 },
    }
]

export const formSchema: FormSchema[] = [
    {
        label: '账户名称',
        field: 'accountName',
        component: 'Input',
        required: true,
    },
    {
        label: '账户编号',
        field: 'accountNumber',
        component: 'Input',
    },
    {
        label: '期初金额',
        field: 'initialAmount',
        component: 'InputNumber',
    },
    {
        label: '当前余额',
        field: 'currentAmount',
        component: 'InputNumber',
    },
    {
        label: '默认账户',
        field: 'isDefault',
        helpMessage: '只允许有一个默认账户，如果选择是，之前的默认账户将会变成非默认账户',
        component: 'RadioGroup',
        defaultValue: 0,
        componentProps: {
            options: [
                {
                    label: '不是',
                    value: 0,
                },
                {
                    label: '是',
                    value: 1,
                },
            ],
        },
    },
    {
        label: '备注',
        field: 'remark',
        component: 'InputTextArea',
    },
    {
        label: '排序',
        field: 'sort',
        component: 'InputNumber',
    }
]