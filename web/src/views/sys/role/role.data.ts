import { BasicColumn, FormSchema } from '/@/components/Table';
import { useI18n } from '/@/hooks/web/useI18n';
import { h } from 'vue';
import { Switch } from 'ant-design-vue';
import { setRoleStatus } from '/@/api/sys/role';

const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: '角色名称',
        dataIndex: 'roleName',
        width: 150,
    },
    {
        title: '类型',
        dataIndex: 'type',
        width: 120,
    },
    {
        title: '价格屏蔽',
        dataIndex: 'priceLimit',
        width: 120,
        customRender: ({record}) => {
            if(record.priceLimit === 1) {
                return '屏蔽采购价'
            } else if(record.priceLimit === 2) {
                return '屏蔽零售价'
            } else if(record.priceLimit === 3) {
                return '屏蔽销售价'
            }
        }
    },
    {
        title: '状态',
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
        title: '创建时间',
        dataIndex: 'createTime',
        width: 180,
    },
    {
        title: '备注',
        dataIndex: 'description',
        width: 180,
    },
];

export const searchFormSchema: FormSchema[] = [
    {
        field: 'roleName',
        label: '角色名称',
        component: 'Input',
        colProps: { span: 8 },
    },
    {
        field: 'status',
        label: '状态',
        component: 'Select',
        componentProps: {
            options: [
                { label: '启用', value: 0 },
                { label: '停用', value: 1 },
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
        label: '角色名称',
        required: true,
        component: 'Input',
    },
    {
        field: 'type',
        label: '类型',
        required: true,
        component: 'Select',
        componentProps: {
            options: [
                { label: '查看全部数据', value: '全部数据' },
                { label: '查看个人数据', value: '个人数据' },
            ],
        },
    },
    {
        field: 'priceLimit',
        label: '价格屏蔽',
        component: 'Select',
        componentProps: {
            options: [
                { label: '屏蔽采购价', value: 1, key: 1},
                { label: '屏蔽零售价', value: 2, key: 2 },
                { label: '屏蔽销售价', value: 3, key: 3 },
            ],
        },
    },
    {
        field: 'status',
        label: '状态',
        component: 'RadioButtonGroup',
        defaultValue: 0,
        componentProps: {
            options: [
                { label: '启用', value: 0 },
                { label: '停用', value: 1 },
            ],
        },
    },
    {
        label: '备注',
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