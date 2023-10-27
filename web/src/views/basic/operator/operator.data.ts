import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import { h } from 'vue';
import {Switch} from "ant-design-vue";
import {useMessage} from "@/hooks/web/useMessage";
import {useI18n} from "@/hooks/web/useI18n";
import {updateOperatorStatus} from "@/api/basic/operator";

const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: '姓名',
        dataIndex: 'name',
        width: 90,
    },
    {
        title: '类型',
        dataIndex: 'type',
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
                    updateOperatorStatus([record.id], newStatus )
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
        title: '创建时间',
        dataIndex: 'createTime',
        width: 150,
    }
]

export const searchFormSchema: FormSchema[] = [
    {
        label: '姓名',
        field: 'name',
        component: 'Input',
        colProps: { span: 5 },
    },
    {
        label: '类型',
        field: 'type',
        component: 'Select',
        colProps: { span: 5 },
        componentProps: {
            options: [
                { label: '业务员', value: '业务员', key: 0 },
                { label: '财务员', value: '财务员', key: 1 },
            ],
        },
    }
]

export const formSchema: FormSchema[] = [
    {
        label: '姓名',
        field: 'name',
        component: 'Input',
        required: true,
    },
    {
        label: '类型',
        field: 'type',
        component: 'Select',
        componentProps: {
            options: [
                { label: '业务员', value: '业务员', key: 0 },
                { label: '财务员', value: '财务员', key: 1 },
            ],
        },
        required: true,
    },
    {
        label: '排序',
        field: 'sort',
        component: 'InputNumber',
    },
    {
        label: '备注',
        field: 'remark',
        component: 'InputTextArea',
    }
]