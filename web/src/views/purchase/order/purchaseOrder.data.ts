import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {useI18n} from "@/hooks/web/useI18n";
import {getSupplierList} from "@/api/basic/supplier";

export const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: t('purchase.order.table.supplier'),
        dataIndex: 'supplierName',
        width: 130,
    },
    {
        title: t('purchase.order.table.receiptNumber'),
        dataIndex: 'receiptNumber',
        width: 130,
    },
    {
        title: t('purchase.order.table.productInformation'),
        dataIndex: 'productInfo',
        width: 80,
    },
    {
        title: t('purchase.order.table.productQuantity'),
        dataIndex: 'productNumber',
        width: 50,
    },
    {
        title: t('purchase.order.table.totalAmount'),
        dataIndex: 'totalAmount',
        width: 60,
    },
    {
        title: t('purchase.order.table.totalIncludingTax'),
        dataIndex: 'taxRateTotalAmount',
        width: 80,
    },
    {
        title: t('purchase.order.table.collectDeposit'),
        dataIndex: 'deposit',
        width: 80,
    },
    {
        title: t('purchase.order.table.receiptDate'),
        dataIndex: 'receiptDate',
        width: 130,
    },
    {
        title: t('purchase.order.table.operator'),
        dataIndex: 'operator',
        width: 60,
    },
    {
        title: t('purchase.order.table.status'),
        dataIndex: 'status',
        width: 80,
    },
]

export const searchFormSchema: FormSchema[] = [
    {
        label: t('purchase.order.table.receiptNumber'),
        field: 'receiptNumber',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        field: '[startDate, endDate]',
        label: t('purchase.order.table.receiptDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('purchase.order.header.startDate'), t('purchase.order.header.endDate'),],
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('purchase.order.table.supplier'),
        field: 'supplierId',
        component: 'ApiSelect',
        componentProps: {
            api: getSupplierList,
            resultField: 'data',
            labelField: 'supplierName',
            valueField: 'id',
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('purchase.order.table.productInformation'),
        field: 'productInfo',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('purchase.order.table.status'),
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
                { label: t('purchase.partialPurchase'), value: 2, key: 2 },
                { label: t('purchase.completePurchase'), value: 3, key: 3 },
            ],
        },
    },
    {
        label: t('purchase.order.header.receiptRemark'),
        field: 'remark',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },

    }
]

export const purchaseOrderTableColumns: BasicColumn[] = [
    {
        title: t('purchase.order.form.table.warehouse'),
        dataIndex: 'warehouseName',
        width: 100,
    },
    {
        title: t('purchase.order.form.table.barCode'),
        dataIndex: 'barCode',
        width: 100,
    },
    {
        title: t('purchase.order.form.table.name'),
        dataIndex: 'productName',
        width: 120,
    },
    {
        title: t('purchase.order.form.table.standard'),
        dataIndex: 'productStandard',
        width: 120,
    },
    {
        title: t('purchase.order.form.table.model'),
        dataIndex: 'productModel',
        width: 120,
    },
    {
        title: t('purchase.order.form.table.color'),
        dataIndex: 'productColor',
        width: 70,
    },
    {
        title: t('purchase.order.form.table.stock'),
        dataIndex: 'stock',
        width: 80,
    },
    {
        title: t('purchase.order.form.table.unit'),
        dataIndex: 'productUnit',
        width: 60,
    },
    {
        title: t('purchase.order.form.table.quantity'),
        dataIndex: 'productNumber',
        width: 60,
    },
    {
        title: t('purchase.order.form.table.unitPrice'),
        dataIndex: 'unitPrice',
        width: 60,
    },
    {
        title: t('purchase.order.form.table.amount'),
        dataIndex: 'amount',
        width: 60,
    },
    {
        title: t('purchase.order.form.table.taxRate'),
        dataIndex: 'taxRate',
        width: 60,
    },
    {
        title: t('purchase.order.form.table.taxAmount'),
        dataIndex: 'taxAmount',
        width: 60,
    },
    {
        title: t('purchase.order.form.table.totalIncludingTax'),
        dataIndex: 'taxTotalPrice',
        width: 80,
    },
    {
        title: t('purchase.order.form.table.remark'),
        dataIndex: 'remark',
        width: 100,
    },
]