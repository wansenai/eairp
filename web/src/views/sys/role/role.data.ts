import { BasicColumn, FormSchema } from '/@/components/Table';
import { useI18n } from '/@/hooks/web/useI18n';
import { h } from 'vue';
import { Switch } from 'ant-design-vue';
import { setRoleStatus } from '/@/api/sys/role';

const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: t('system.role.table.roleName'),
        dataIndex: 'roleName',
        width: 150,
    },
    {
        title: t('system.role.table.type'),
        dataIndex: 'type',
        width: 120,
    },
    {
        title: t('system.role.table.priceBlocking'),
        dataIndex: 'priceLimit',
        width: 120,
        customRender: ({record}) => {
            if(record.priceLimit === 1) {
                return t('system.role.header.blockPurchasePrice')
            } else if(record.priceLimit === 2) {
                return t('system.role.header.blockRetailPrice')
            } else if(record.priceLimit === 3) {
                return t('system.role.header.blockSalesPrice')
            }
        }
    },
    {
        title: t('system.role.table.status'),
        dataIndex: 'status',
        width: 120,
        customRender: ({ record }) => {
            if (!Reflect.has(record, 'pendingStatus')) {
                record.pendingStatus = false;
            }
            return h(Switch, {
                checked: record.status === 0,
                checkedChildren: t('common.on'),
                unCheckedChildren: t('common.off'),
                loading: record.pendingStatus,
                onChange(checked: boolean) {
                    record.pendingStatus = true;
                    const newStatus = checked ? 0 : 1;
                    setRoleStatus(record.id, newStatus)
                        .then(() => {
                            record.status = newStatus;
                        })
                        .finally(() => {
                            record.pendingStatus = false;
                        });
                },
            });
        },
    },
    {
        title: t('system.role.table.createTime'),
        dataIndex: 'createTime',
        width: 180,
    },
    {
        title: t('system.role.table.remark'),
        dataIndex: 'description',
        width: 180,
    },
];

export const searchFormSchema: FormSchema[] = [
    {
        field: 'roleName',
        label: t('system.role.header.roleName'),
        component: 'Input',
        colProps: { span: 8 },
    },
    {
        field: 'status',
        label: t('system.role.header.status'),
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

export const formSchema: FormSchema[] = [
    {
        field: 'id',
        label: '角色id',
        show: false,
        component: 'Input',
    },
    {
        field: 'roleName',
        label: t('system.role.form.roleName'),
        required: true,
        component: 'Input',
    },
    {
        field: 'type',
        label: t('system.role.form.type'),
        required: true,
        component: 'Select',
        componentProps: {
            options: [
                { label: t('system.role.header.viewAllData'), value: '全部数据' },
                { label: t('system.role.header.viewPersonalData'), value: '个人数据' },
            ],
        },
    },
    {
        field: 'priceLimit',
        label: t('system.role.form.priceBlocking'),
        component: 'Select',
        componentProps: {
            options: [
                { label: t('system.role.header.blockPurchasePrice'), value: 1, key: 1},
                { label: t('system.role.header.blockRetailPrice'), value: 2, key: 2 },
                { label: t('system.role.header.blockSalesPrice'), value: 3, key: 3 },
            ],
        },
    },
    {
        field: 'status',
        label: t('system.role.form.status'),
        component: 'RadioButtonGroup',
        defaultValue: 0,
        componentProps: {
            options: [
                { label: t('system.role.header.enable'), value: 0 },
                { label: t('system.role.header.disable'), value: 1 },
            ],
        },
    },
    {
        label: t('system.role.form.remark'),
        field: 'description',
        component: 'InputTextArea',
    },
];

export const roleSchema: FormSchema[] = [
    {
        field: 'id',
        label: '角色id',
        show: false,
        component: 'Input',
    },
    {
        label: ' ',
        field: 'menuIds',
        slot: 'menu',
        component: 'Input',
    },
]