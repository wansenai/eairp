import { BasicColumn, FormSchema } from '@/components/Table';
import { useI18n } from '@/hooks/web/useI18n';
import { h } from 'vue';
import { Switch } from 'ant-design-vue';
import { useMessage } from '@/hooks/web/useMessage';
import { updateStatus } from "@/api/sys/tenant";

const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: t('sys.tenant.form.name'),
        dataIndex: 'tenantName',
        width: 120,
    },
    {
        title: t('sys.tenant.form.userNumLimit'),
        dataIndex: 'userNumLimit',
        width: 120,
    },
    {
        title: t('sys.tenant.form.type'),
        dataIndex: 'type',
        width: 120,
        customRender: ({record}) => {
            if(record.type === 0) {
                return t('sys.tenant.form.free')
            } else if(record.type === 1) {
                return t('sys.tenant.form.pay')
            }
        }
    },
    {
        title: t('sys.tenant.form.expireTime'),
        dataIndex: 'expireTime',
        width: 180,
    },
    {
        title: t('sys.tenant.form.status'),
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
                    updateStatus({id: record.id, status: newStatus} )
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
        title: t('sys.tenant.form.remark'),
        dataIndex: 'remark',
        width: 120,
    },
    {
        title: t('common.createTime'),
        dataIndex: 'createTime',
        width: 180,
    }
];

export const searchFormSchema: FormSchema[] = [
    {
        field: 'tenantName',
        label: t('sys.tenant.form.name'),
        component: 'Input',
        colProps: { span: 8 },
    },
    {
        field: 'type',
        label: t('sys.tenant.form.type'),
        component: 'Select',
        componentProps: {
            options: [
                { label: t('sys.tenant.form.free'), value: 0 },
                { label: t('sys.tenant.form.pay'), value: 1 },
            ],
        },
        colProps: { span: 8 },
    },
    {
        field: 'status',
        label: t('sys.tenant.form.status'),
        component: 'Select',
        componentProps: {
            options: [
                { label: t('system.role.header.enable'), value: 0 },
                { label: t('system.role.header.disable'), value: 1 },
            ],
        },
        colProps: { span: 8 },
    },
];

function isNotExist({ values }) {
    return !Boolean(values.id)
}

export const tenantFormSchema: FormSchema[] = [
    {
        field: 'id',
        label: '租户id',
        show: false,
        component: 'Input',
    },
    {
        field: 'username',
        label: t('sys.login.userName'),
        component: 'Input',
        // 注意最好使用异步验证
        helpMessage: [t('sys.tenant.form.noticeThree')],
        rules: [
            {
                required: true,
                message: '请输入用户名',
            },
        ],
        ifShow: isNotExist
    },
    {
        field: 'password',
        label: t('sys.login.password'),
        component: 'InputPassword',
        required: false,
        helpMessage: [t('sys.tenant.form.noticeTwo')],
        ifShow: isNotExist
    },
    {
        field: 'tenantName',
        label: t('sys.tenant.form.name'),
        component: 'Input',
        required: true,
    },
    {
        field: 'phoneNumber',
        label: t('sys.login.mobile'),
        component: 'Input',
        required: true,
    },
    {
        field: 'userNumLimit',
        label: t('sys.tenant.form.userNumLimit'),
        component: 'InputNumber',
        required: true,
    },
    {
        field: 'type',
        label: t('sys.tenant.form.type'),
        component: 'Select',
        required: true,
        componentProps: {
            options: [
                { label: t('sys.tenant.form.free'), value: 0 },
                { label: t('sys.tenant.form.pay'), value: 1 },
            ],
        },
    },
    {
        field: 'expireTime',
        component: 'DatePicker',
        required: true,
        helpMessage: [t('sys.tenant.form.noticeFour')],
        label: t('sys.tenant.form.expireTime'),
    },
    {
        field: 'email',
        label: t('sys.login.email'),
        component: 'Input',
    },
    {
        field: 'remark',
        label: t('sys.user.remake'),
        component: 'InputTextArea',
    },
];
