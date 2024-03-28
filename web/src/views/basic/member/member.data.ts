import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import { h } from 'vue';
import {Switch} from "ant-design-vue";
import {useMessage} from "@/hooks/web/useMessage";
import {useI18n} from "@/hooks/web/useI18n";
import {updateMemberStatus} from "@/api/basic/member";

const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: t('basic.member.table.memberNumber'),
        dataIndex: 'memberNumber',
        width: 180,
    },
    {
        title: t('basic.member.table.memberName'),
        dataIndex: 'memberName',
        width: 80,
    },
    {
        title: t('basic.member.table.phoneNumber'),
        dataIndex: 'phoneNumber',
        width: 120,
    },
    {
        title: t('basic.member.table.email'),
        dataIndex: 'email',
        width: 120,
    },
    {
        title: t('basic.member.table.advancePayment'),
        dataIndex: 'advancePayment',
        width: 90,
    },
    {
        title: t('basic.member.table.status'),
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
                    updateMemberStatus([record.id], newStatus )
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
        title: t('basic.member.table.remark'),
        dataIndex: 'remark',
        width: 80,
    },
    {
        title: t('basic.member.table.sort'),
        dataIndex: 'sort',
        width: 80,
    },
    {
        title: t('basic.member.table.createTime'),
        dataIndex: 'createTime',
        width: 150,
    }
]

export const searchFormSchema: FormSchema[] = [
    {
        label: t('basic.member.header.memberNumber'),
        field: 'memberNumber',
        component: 'Input',
        colProps: { span: 5 },
    },
    {
        label: t('basic.member.header.phoneNumber'),
        field: 'phoneNumber',
        component: 'Input',
        colProps: { span: 5 },
    },
    {
        field: '[startDate, endDate]',
        label: t('basic.member.header.createTime'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('basic.member.header.startDate'), t('basic.member.header.endDate')],
        },
        colProps: { span: 7 },
    },
]

export const formSchema: FormSchema[] = [
    {
        label: t('basic.member.form.memberNumber'),
        field: 'memberNumber',
        component: 'Input',
        colProps: {
            span: 11,
        },
        required: true,
    },
    {
        label: t('basic.member.form.memberName'),
        field: 'memberName',
        component: 'Input',
        colProps: {
            span: 11,
        },
        required: true,
    },
    {
        label: t('basic.member.form.phoneNumber'),
        field: 'phoneNumber',
        component: 'Input',
        colProps: {
            span: 11,
        },
    },
    {
        label: t('basic.member.form.email'),
        field: 'email',
        component: 'Input',
        colProps: {
            span: 11,
        },
    },
    {
        label: t('basic.member.form.advancePayment'),
        field: 'advancePayment',
        component: 'InputNumber',
        colProps: {
            span: 11,
        }
    },
    {
        label: t('basic.member.table.remark'),
        field: 'remark',
        component: 'InputTextArea',
        colProps: {
            span: 11,
        },
    },
    {
        label: t('basic.member.table.sort'),
        field: 'sort',
        component: 'InputNumber',
        colProps: {
            span: 11,
        }
    }
]