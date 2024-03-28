import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import { h } from 'vue';
import {Switch} from "ant-design-vue";
import {useMessage} from "@/hooks/web/useMessage";
import {useI18n} from "@/hooks/web/useI18n";
import {updateWarehouseStatus} from "@/api/basic/warehouse";
import {getTenantUserList} from "@/api/sys/user";

const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: t('basic.warehouse.table.warehouseName'),
        dataIndex: 'warehouseName',
        width: 180,
    },
    {
        title: t('basic.warehouse.table.warehouseAddress'),
        dataIndex: 'address',
        width: 80,
    },
    {
        title: t('basic.warehouse.table.storageFees'),
        dataIndex: 'price',
        width: 120,
    },
    {
        title: t('basic.warehouse.table.handlingFees'),
        dataIndex: 'truckage',
        width: 120,
    },
    {
        title: t('basic.warehouse.table.manager'),
        dataIndex: 'warehouseManagerName',
        width: 90,
    },
    {
        title: t('basic.warehouse.table.status'),
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
                    updateWarehouseStatus([record.id], newStatus )
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
        title: t('basic.warehouse.table.default'),
        dataIndex: 'isDefault',
        width: 80,
    },
    {
        title: t('basic.warehouse.table.sort'),
        dataIndex: 'sort',
        width: 80,
    },
    {
        title: t('basic.warehouse.table.createTime'),
        dataIndex: 'createTime',
        width: 150,
    }
]

export const searchFormSchema: FormSchema[] = [
    {
        label: t('basic.warehouse.header.warehouse'),
        field: 'warehouseName',
        component: 'Input',
        colProps: { span: 5 },
    },
    {
        label: t('basic.warehouse.header.remark'),
        field: 'remark',
        component: 'Input',
        colProps: { span: 5 },
    }
]

export const formSchema: FormSchema[] = [
    {
        label: t('basic.warehouse.form.warehouseName'),
        field: 'warehouseName',
        component: 'Input',
        required: true,
    },
    {
        label: t('basic.warehouse.form.warehouseAddress'),
        field: 'address',
        component: 'Input',
    },
    {
        label: t('basic.warehouse.form.storageFees'),
        field: 'price',
        component: 'InputNumber',
    },
    {
        label: t('basic.warehouse.form.handlingFees'),
        field: 'truckage',
        component: 'InputNumber',
    },
    {
        label: t('basic.warehouse.form.default'),
        field: 'isDefault',
        helpMessage: t('basic.warehouse.form.defaultTip'),
        component: 'RadioGroup',
        defaultValue: 0,
        componentProps: {
            options: [
                {
                    label: t('basic.warehouse.no'),
                    value: 0,
                },
                {
                    label: t('basic.warehouse.yes'),
                    value: 1,
                },
            ],
        },
    },
    {
        field: 'warehouseManager',
        label: t('basic.warehouse.form.manager'),
        component: 'ApiSelect',
        helpMessage: [t('basic.warehouse.form.managerTip')],
        componentProps: {
            api: getTenantUserList,
            resultField: 'data',
            labelField: 'name',
            valueField: 'id',
        },
    },
    {
        label: t('basic.warehouse.form.remark'),
        field: 'remark',
        component: 'InputTextArea',
    },
    {
        label: t('basic.warehouse.form.sort'),
        field: 'sort',
        component: 'InputNumber',
    }
]