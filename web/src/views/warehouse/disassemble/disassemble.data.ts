import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {getUserOperatorList} from "@/api/sys/user";
import {useI18n} from "@/hooks/web/useI18n";

export const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: t('warehouse.disassemble.table.receiptNumber'),
        dataIndex: 'receiptNumber',
        width: 130,
    },
    {
        title: t('warehouse.disassemble.table.productInfo'),
        dataIndex: 'productInfo',
        width: 150,
    },
    {
        title:  t('warehouse.disassemble.table.receiptDate'),
        dataIndex: 'receiptDate',
        width: 130,
    },
    {
        title: t('warehouse.disassemble.table.productNumber'),
        dataIndex: 'productNumber',
        width: 100,
    },
    {
        title: t('warehouse.disassemble.table.totalAmount'),
        dataIndex: 'totalAmount',
        width: 70,
    },
    {
        title: t('warehouse.disassemble.table.operator'),
        dataIndex: 'operator',
        width: 70,
    },
    {
        title: t('warehouse.disassemble.table.status'),
        dataIndex: 'status',
        width: 70,
    },
]

export const searchFormSchema: FormSchema[] = [
    {
        label: t('warehouse.disassemble.header.receiptNumber'),
        field: 'receiptNumber',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        field: '[startDate, endDate]',
        label: t('warehouse.disassemble.header.receiptDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('warehouse.disassemble.header.starDate'), t('warehouse.disassemble.header.endDate')],
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('warehouse.disassemble.header.operator'),
        field: 'operatorId',
        component: 'ApiSelect',
        componentProps: {
            api: getUserOperatorList,
            resultField: 'data',
            labelField: 'name',
            valueField: 'id',
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('warehouse.disassemble.header.status'),
        field: 'status',
        component: 'Select',
        colProps: {
            xl: 8,
            xxl: 8,
        },
        componentProps: {
            options: [
                { label: t('sys.table.unaudited'), value: 0, key: 0 },
                { label: t('sys.table.audited'), value: 1, key: 1 },
            ],
        },
    },
    {
        label: t('warehouse.disassemble.header.remark'),
        field: 'remark',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    }
]

export const disAssembleTableColumns: BasicColumn[] = [
    {
        title: t('warehouse.assemble.view.productType'),
        dataIndex: 'type',
        width: 80,
    },
    {
        title: t('warehouse.assemble.view.warehouseName'),
        dataIndex: 'warehouseName',
        width: 100,
    },
    {
        title: t('warehouse.assemble.view.barCode'),
        dataIndex: 'barCode',
        width: 120,
    },
    {
        title: t('warehouse.assemble.view.productName'),
        dataIndex: 'productName',
        width: 150,
    },
    {
        title: t('warehouse.assemble.view.productStandard'),
        dataIndex: 'productStandard',
        width: 100,
    },
    {
        title: t('warehouse.assemble.view.productModel'),
        dataIndex: 'productModel',
        width: 100,
    },
    {
        title: t('warehouse.assemble.view.productExtendInfo'),
        dataIndex: 'productExtendInfo',
        width: 150,
    },
    {
        title: t('warehouse.assemble.view.stock'),
        dataIndex: 'stock',
        width: 70,
    },
    {
        title: t('warehouse.assemble.view.productUnit'),
        dataIndex: 'productUnit',
        width: 70,
    },
    {
        title: t('warehouse.assemble.view.productNumber'),
        dataIndex: 'productNumber',
        width: 70,
    },
    {
        title: t('warehouse.assemble.view.purchasePrice'),
        dataIndex: 'unitPrice',
        width: 70,
    },
    {
        title: t('warehouse.assemble.view.amount'),
        dataIndex: 'amount',
        width: 70,
    },
    {
        title: t('warehouse.assemble.view.remark'),
        dataIndex: 'remark',
        width: 130,
    },
]