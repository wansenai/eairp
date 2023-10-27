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
        title: '会员卡号',
        dataIndex: 'memberNumber',
        width: 180,
    },
    {
        title: '会员名称',
        dataIndex: 'memberName',
        width: 80,
    },
    {
        title: '手机号',
        dataIndex: 'phoneNumber',
        width: 120,
    },
    {
        title: '电子邮箱',
        dataIndex: 'email',
        width: 120,
    },
    {
        title: '预付款',
        dataIndex: 'advancePayment',
        width: 90,
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
        title: '备注',
        dataIndex: 'remark',
        width: 80,
    },
    {
        title: '排序',
        dataIndex: 'sort',
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
        label: '会员卡号',
        field: 'memberNumber',
        component: 'Input',
        colProps: { span: 5 },
    },
    {
        label: '手机号码',
        field: 'phoneNumber',
        component: 'Input',
        colProps: { span: 5 },
    },
    {
        field: '[startDate, endDate]',
        label: '时间',
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: ['开始日期', '结束日期'],
        },
        colProps: { span: 7 },
    },
]

export const formSchema: FormSchema[] = [
    {
        label: '会员卡号',
        field: 'memberNumber',
        component: 'Input',
        colProps: {
            span: 11,
        },
        required: true,
    },
    {
        label: '会员名称',
        field: 'memberName',
        component: 'Input',
        colProps: {
            span: 11,
        },
        required: true,
    },
    {
        label: '手机号码',
        field: 'phoneNumber',
        component: 'Input',
        colProps: {
            span: 11,
        },
    },
    {
        label: '电子邮箱',
        field: 'email',
        component: 'Input',
        colProps: {
            span: 11,
        },
    },
    {
        label: '预付款',
        field: 'advancePayment',
        component: 'InputNumber',
        colProps: {
            span: 11,
        }
    },
    {
        label: '备注',
        field: 'remark',
        component: 'InputTextArea',
        colProps: {
            span: 11,
        },
    },
    {
        label: '排序',
        field: 'sort',
        component: 'InputNumber',
        colProps: {
            span: 11,
        }
    }
]