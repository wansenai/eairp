import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import { h } from 'vue';
import {Switch} from "ant-design-vue";
import {useMessage} from "@/hooks/web/useMessage";
import {useI18n} from "@/hooks/web/useI18n";
import {updateUnitStatus} from "@/api/product/productUnit";

const { t } = useI18n();
export const columns: BasicColumn[] = [
    {
        title: t('product.unit.table.unitName'),
        dataIndex: 'computeUnit',
        width: 230,
    },
    {
        title: t('product.unit.table.basicUnit'),
        dataIndex: 'basicUnit',
        width: 70,
    },
    {
        title: t('product.unit.table.deputyUnitOne'),
        dataIndex: 'otherComputeUnit',
        width: 70,
    },
    {
        title: t('product.unit.table.deputyUnitTwo'),
        dataIndex: 'otherComputeUnitTwo',
        width: 150,
    },
    {
        title: t('product.unit.table.deputyUnitThree'),
        dataIndex: 'otherComputeUnitThree',
        width: 150,
    },
    {
        title: t('product.unit.table.status'),
        dataIndex: 'status',
        width: 150,
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
                    updateUnitStatus({id: record.id, status: newStatus} )
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
        title: t('product.unit.table.createTime'),
        dataIndex: 'createTime',
        width: 150,
    }
]

export const searchFormSchema: FormSchema[] = [
    {
        label: t('product.unit.table.unitName'),
        field: 'computeUnit',
        component: 'Input',
        colProps: { span: 8 },
    },
]

export const formSchema: FormSchema[] = [
    {
        label: '单位id',
        field: 'id',
        show: false,
        component: 'Input',
    },
    {
        label: t('product.unit.table.basicUnit'),
        field: 'basicUnit',
        component: 'Input',
        required: true,
        componentProps: {
            placeholder: t('product.unit.table.inputBasicUnit'),
        },
    },
    {
        label: t('product.unit.table.deputyUnitOne'),
        field: 'otherUnit',
        component: 'Input',
        required: true,
        componentProps: {
            placeholder: t('product.unit.table.inputDeputyUnitOne'),
        },
        colProps: {
            span: 13,
        },
    },
    {
        label: '=',
        field: 'ratio',
        component: 'InputNumber',
        labelWidth: 10,
        componentProps: {
            addonAfter: t('product.unit.table.basicUnit'),
            placeholder: t('product.unit.table.inputProportionOne'),
        },
        colProps: {
            span: 11,
        },
    },
    {
        label: t('product.unit.table.deputyUnitTwo'),
        field: 'otherUnitTwo',
        component: 'Input',
        componentProps: {
            placeholder: t('product.unit.table.inputDeputyUnitTwo'),
        },
        colProps: {
            span: 13,
        },
    },
    {
        label: '=',
        field: 'ratioTwo',
        labelWidth: 10,
        componentProps: {
            addonAfter: t('product.unit.table.basicUnit'),
            placeholder: t('product.unit.table.inputProportionTwo'),
        },
        component: 'Input',
        colProps: {
            span: 11,
        }
    },
    {
        label: t('product.unit.table.deputyUnitThree'),
        field: 'otherUnitThree',
        component: 'Input',
        componentProps: {
            placeholder: t('product.unit.table.inputDeputyUnitThree'),
        },
        colProps: {
            span: 13,
        },
    },
    {
        label: '=',
        labelWidth: 10,
        field: 'ratioThree',
        componentProps: {
            addonAfter: t('product.unit.table.basicUnit'),
            placeholder: t('product.unit.table.inputProportionThree'),
        },
        component: 'Input',
        colProps: {
            span: 11,
        }
    }
]