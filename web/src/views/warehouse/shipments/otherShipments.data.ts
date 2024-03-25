import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {getCustomerList} from "@/api/basic/customer";
import {getUserOperatorList} from "@/api/sys/user";
import {useI18n} from "@/hooks/web/useI18n";

export const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: t('warehouse.otherShipments.table.customer'),
        dataIndex: 'customerName',
        width: 170,
    },
    {
        title: t('warehouse.otherShipments.table.receiptNumber'),
        dataIndex: 'receiptNumber',
        width: 130,
    },
    {
        title: t('warehouse.otherShipments.table.productInfo'),
        dataIndex: 'productInfo',
        width: 150,
    },
    {
        title: t('warehouse.otherShipments.table.receiptDate'),
        dataIndex: 'receiptDate',
        width: 130,
    },
    {
        title: t('warehouse.otherShipments.table.productNumber'),
        dataIndex: 'productNumber',
        width: 100,
    },
    {
        title: t('warehouse.otherShipments.table.totalAmount'),
        dataIndex: 'totalAmount',
        width: 70,
    },
    {
        title: t('warehouse.otherShipments.table.operator'),
        dataIndex: 'operator',
        width: 70,
    },
    {
        title: t('warehouse.otherShipments.table.status'),
        dataIndex: 'status',
        width: 70,
    },
]

export const searchFormSchema: FormSchema[] = [
    {
        label: t('warehouse.otherShipments.header.receiptNumber'),
        field: 'receiptNumber',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        field: '[startDate, endDate]',
        label: t('warehouse.otherShipments.header.receiptDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('warehouse.otherShipments.header.starDate'), t('warehouse.otherShipments.header.endDate')],
        },
        colProps: { span: 7 },
    },
    {
        label: t('warehouse.otherShipments.header.customer'),
        field: 'customerId',
        component: 'ApiSelect',
        componentProps: {
            api: getCustomerList,
            resultField: 'data',
            labelField: 'customerName',
            valueField: 'id',
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('warehouse.otherShipments.header.operator'),
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
        label: t('warehouse.otherShipments.header.status'),
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
        label: t('warehouse.otherShipments.header.remark'),
        field: 'remark',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    }
]

export const otherShipmentTableColumns: BasicColumn[] = [
    {
        title: t('warehouse.otherStorage.view.warehouseName'),
        dataIndex: 'warehouseName',
        width: 100,
    },
    {
        title: t('warehouse.otherStorage.view.barCode'),
        dataIndex: 'barCode',
        width: 120,
    },
    {
        title: t('warehouse.otherStorage.view.productName'),
        dataIndex: 'productName',
        width: 150,
    },
    {
        title: t('warehouse.otherStorage.view.productStandard'),
        dataIndex: 'productStandard',
        width: 100,
    },
    {
        title: t('warehouse.otherStorage.view.productModel'),
        dataIndex: 'productModel',
        width: 100,
    },
    {
        title: t('warehouse.otherStorage.view.productExtendInfo'),
        dataIndex: 'productExtendInfo',
        width: 150,
    },
    {
        title: t('warehouse.otherStorage.view.stock'),
        dataIndex: 'stock',
        width: 70,
    },
    {
        title: t('warehouse.otherStorage.view.productUnit'),
        dataIndex: 'productUnit',
        width: 70,
    },
    {
        title: t('warehouse.otherStorage.view.productNumber'),
        dataIndex: 'productNumber',
        width: 70,
    },
    {
        title:  t('warehouse.otherStorage.view.unitPrice'),
        dataIndex: 'unitPrice',
        width: 70,
    },
    {
        title: t('warehouse.otherStorage.view.amount'),
        dataIndex: 'amount',
        width: 70,
    },
    {
        title: t('warehouse.otherStorage.view.remark'),
        dataIndex: 'remark',
        width: 130,
    },
]