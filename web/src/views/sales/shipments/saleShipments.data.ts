import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {useI18n} from "@/hooks/web/useI18n";
import {getCustomerList} from "@/api/basic/customer";

export const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: t('sales.shipments.table.customer'),
        dataIndex: 'customerName',
        width: 60,
    },
    {
        title: t('sales.shipments.table.receiptNumber'),
        dataIndex: 'receiptNumber',
        width: 140,
    },
    {
        title: t('sales.shipments.table.productInformation'),
        dataIndex: 'productInfo',
        width: 80,
    },
    {
        title: t('sales.shipments.table.productQuantity'),
        dataIndex: 'productNumber',
        width: 80,
    },
    {
        title: t('sales.shipments.table.totalAmount'),
        dataIndex: 'totalAmount',
        width: 60,
    },
    {
        title: t('sales.shipments.table.totalIncludingTax'),
        dataIndex: 'taxIncludedAmount',
        width: 80,
    },
    {
        title: t('sales.shipments.table.collectAmount'),
        dataIndex: 'totalCollectAmount',
        width: 80,
    },
    {
        title: t('sales.shipments.table.thisTimeCollectAmount'),
        dataIndex: 'thisCollectAmount',
        width: 80,
    },
    {
        title: t('sales.shipments.table.thisTimeArrearsAmount'),
        dataIndex: 'thisArrearsAmount',
        width: 80,
    },
    {
        title: t('sales.shipments.table.receiptDate'),
        dataIndex: 'receiptDate',
        width: 130,
    },
    {
        title: t('sales.shipments.table.operator'),
        dataIndex: 'operator',
        width: 60,
    },
    {
        title: t('sales.shipments.table.status'),
        dataIndex: 'status',
        width: 80,
    },
]

export const searchFormSchema: FormSchema[] = [
    {
        label: t('sales.shipments.table.receiptNumber'),
        field: 'receiptNumber',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        field: '[startDate, endDate]',
        label: t('sales.shipments.table.receiptDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('sales.shipments.header.startDate'), t('sales.shipments.header.endDate')],
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('sales.shipments.table.customer'),
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
        label: t('sales.shipments.table.productInformation'),
        field: 'productInfo',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('sales.shipments.table.status'),
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
        label: t('sales.shipments.header.receiptRemark'),
        field: 'remark',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },

    }
]