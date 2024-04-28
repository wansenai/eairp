import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {getUserOperatorList} from "@/api/sys/user";
import {useI18n} from "@/hooks/web/useI18n";

export const { t } = useI18n();
export const columns: BasicColumn[] = [
    {
        title: t('warehouse.allotShipments.table.receiptNumber'),
        dataIndex: 'receiptNumber',
        width: 130,
    },
    {
        title: t('warehouse.allotShipments.table.productInfo'),
        dataIndex: 'productInfo',
        width: 150,
    },
    {
        title: t('warehouse.allotShipments.table.receiptDate'),
        dataIndex: 'receiptDate',
        width: 130,
    },
    {
        title: t('warehouse.allotShipments.table.productNumber'),
        dataIndex: 'productNumber',
        width: 100,
    },
    {
        title: t('warehouse.allotShipments.table.operator'),
        dataIndex: 'operator',
        width: 70,
    },
    {
        title: t('warehouse.allotShipments.table.status'),
        dataIndex: 'status',
        width: 70,
    },
]

export const searchFormSchema: FormSchema[] = [
    {
        label: t('warehouse.allotShipments.header.receiptNumber'),
        field: 'receiptNumber',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        field: '[startDate, endDate]',
        label: t('warehouse.allotShipments.header.receiptDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('warehouse.allotShipments.header.starDate'), t('warehouse.allotShipments.header.endDate')],
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('warehouse.allotShipments.header.operator'),
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
        label: t('warehouse.allotShipments.header.status'),
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
        label: t('warehouse.allotShipments.header.remark'),
        field: 'remark',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    }
]

export const allotShipmentTableColumns: BasicColumn[] = [
    {
        title: t('warehouse.allotShipments.form.table.outWarehouse'),
        dataIndex: 'warehouseName',
        width: 100,
    },
    {
        title:  t('warehouse.allotShipments.form.table.inWarehouse'),
        dataIndex: 'otherWarehouseName',
        width: 100,
    },
    {
        title: t('warehouse.allotShipments.form.table.barCode'),
        dataIndex: 'barCode',
        width: 120,
    },
    {
        title: t('warehouse.allotShipments.form.table.name'),
        dataIndex: 'productName',
        width: 150,
    },
    {
        title: t('warehouse.allotShipments.form.table.standard'),
        dataIndex: 'productStandard',
        width: 100,
    },
    {
        title: t('warehouse.allotShipments.form.table.model'),
        dataIndex: 'productModel',
        width: 100,
    },
    {
        title: t('warehouse.allotShipments.form.table.extendInfo'),
        dataIndex: 'productExtendInfo',
        width: 150,
    },
    {
        title: t('warehouse.allotShipments.form.table.stock'),
        dataIndex: 'stock',
        width: 70,
    },
    {
        title: t('warehouse.allotShipments.form.table.unit'),
        dataIndex: 'productUnit',
        width: 70,
    },
    {
        title: t('warehouse.allotShipments.form.table.quantity'),
        dataIndex: 'productNumber',
        width: 70,
    },
    {
        title: t('warehouse.allotShipments.form.table.remark'),
        dataIndex: 'remark',
        width: 130,
    },
]