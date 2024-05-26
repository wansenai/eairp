import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {useI18n} from "@/hooks/web/useI18n";
import {getCustomerList} from "@/api/basic/customer";

export const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: t('sales.order.table.customer'),
        dataIndex: 'customerName',
        width: 60,
    },
    {
        title: t('sales.order.table.receiptNumber'),
        dataIndex: 'receiptNumber',
        width: 130,
    },
    {
        title: t('sales.order.table.productInformation'),
        dataIndex: 'productInfo',
        width: 80,
    },
    {
        title: t('sales.order.table.productQuantity'),
        dataIndex: 'productNumber',
        width: 80,
    },
    {
        title: t('sales.order.table.totalAmount'),
        dataIndex: 'totalPrice',
        width: 60,
    },
    {
        title: t('sales.order.table.totalIncludingTax'),
        dataIndex: 'taxRateTotalPrice',
        width: 80,
    },
    {
        title: t('sales.order.table.collectDeposit'),
        dataIndex: 'deposit',
        width: 80,
    },
    {
        title: t('sales.order.table.receiptDate'),
        dataIndex: 'receiptDate',
        width: 130,
    },
    {
        title: t('sales.order.table.operator'),
        dataIndex: 'operator',
        width: 60,
    },
    {
        title: t('sales.order.table.status'),
        dataIndex: 'status',
        width: 80,
    },
]

export const searchFormSchema: FormSchema[] = [
    {
        label: t('sales.order.table.receiptNumber'),
        field: 'receiptNumber',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        field: '[startDate, endDate]',
        label: t('sales.order.table.receiptDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('sales.order.header.startDate'), t('sales.order.header.endDate')],
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('sales.order.table.customer'),
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
        label: t('sales.order.table.productInformation'),
        field: 'productInfo',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('sales.order.table.status'),
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
                { label: t('sales.partialSales'), value: 2, key: 2 },
                { label: t('sales.completeSales'), value: 3, key: 3 },
            ],
        },
    },
    {
        label: t('sales.order.header.receiptRemark'),
        field: 'remark',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },

    }
]

export const TableColumns: BasicColumn[] = [
    {
        title: t('sales.order.form.table.warehouse'),
        dataIndex: 'warehouseName',
        width: 100,
    },
    {
        title: t('sales.order.form.table.barCode'),
        dataIndex: 'barCode',
        width: 150,
    },
    {
        title: t('sales.order.form.table.name'),
        dataIndex: 'productName',
        width: 150,
    },
    {
        title: t('sales.order.form.table.standard'),
        dataIndex: 'productStandard',
        width: 120,
    },
    {
        title: t('sales.order.form.table.model'),
        dataIndex: 'productModel',
        width: 120,
    },
    {
        title: t('sales.order.form.table.color'),
        dataIndex: 'productColor',
        width: 70,
    },
    {
        title: t('sales.order.form.table.stock'),
        dataIndex: 'stock',
        width: 80,
    },
    {
        title: t('sales.order.form.table.unit'),
        dataIndex: 'productUnit',
        width: 60,
    },
    {
        title: t('sales.order.form.table.quantity'),
        dataIndex: 'productNumber',
        width: 60,
    },
    {
        title: t('sales.order.form.table.unitPrice'),
        dataIndex: 'unitPrice',
        width: 60,
    },
    {
        title: t('sales.order.form.table.amount'),
        dataIndex: 'amount',
        width: 60,
    },
    {
        title: t('sales.order.form.table.taxRate'),
        dataIndex: 'taxRate',
        width: 60,
    },
    {
        title: t('sales.order.form.table.taxAmount'),
        dataIndex: 'taxAmount',
        width: 60,
    },
    {
        title: t('sales.order.form.table.totalPriceAndTax'),
        dataIndex: 'taxTotalPrice',
        width: 60,
    },
    {
        title: t('sales.order.form.table.remark'),
        dataIndex: 'remark',
        width: 100,
    },
]