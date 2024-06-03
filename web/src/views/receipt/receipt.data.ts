import {BasicColumn, FormSchema} from "@/components/Table";
import {useI18n} from "@/hooks/web/useI18n";

export const { t } = useI18n();

export const ReceiptDetailColumn: BasicColumn[] = [
    {
        title: 'id',
        dataIndex: 'id',
        width: 60,
        ifShow: false,
    },
    {
        title: t('purchase.order.form.table.barCode'),
        dataIndex: 'productBarcode',
        width: 130,
    },
    {
        title: t('purchase.order.form.table.name'),
        dataIndex: 'productName',
        width: 130,
    },
    {
        title: t('purchase.order.form.table.standard'),
        dataIndex: 'productStandard',
        width: 100,
    },
    {
        title: t('purchase.order.form.table.model'),
        dataIndex: 'productModel',
        width: 100,
    },
    {
        title: t('purchase.order.form.table.unit'),
        dataIndex: 'unit',
        width: 80,
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
        width: 80,
    },
    {
        title: t('purchase.order.form.table.taxAmount'),
        dataIndex: 'taxAmount',
        width: 80,
    },
    {
        title: t('purchase.order.form.table.totalIncludingTax'),
        dataIndex: 'taxIncludedAmount',
        width: 80,
    },
    {
        title: t('purchase.order.form.table.remark'),
        dataIndex: 'remark',
        width: 100,
    },
]

export const ReceiptColumn: BasicColumn[] = [
    {
        title: 'id',
        dataIndex: 'id',
        width: 0,
        ifShow: false,
    },
    {
        title: 'uid',
        dataIndex: 'uid',
        width: 0,
        ifShow: false,
    },
    {
        title: t('purchase.order.form.table.name'),
        dataIndex: 'name',
        width: 70,
    },
    {
        title: t('purchase.order.table.receiptNumber'),
        dataIndex: 'receiptNumber',
        width: 190,
    },
    {
        title: t('purchase.order.table.receiptDate'),
        dataIndex: 'receiptDate',
        width: 150,
    },
    {
        title: t('purchase.order.table.productInformation'),
        dataIndex: 'productInfo',
        width: 200,
    },
    {
        title: t('purchase.order.form.table.quantity'),
        dataIndex: 'productNumber',
        width: 70,
    },
    {
        title: t('purchase.order.table.totalAmount'),
        dataIndex: 'totalAmount',
        width: 70,
    },
    {
        title: t('purchase.order.form.table.totalIncludingTax'),
        dataIndex: 'taxRateTotalAmount',
        width: 70,
    },
    {
        title: t('purchase.order.table.operator'),
        dataIndex: 'operator',
        width: 80,
    },
    {
        title: t('purchase.order.table.status'),
        dataIndex: 'status',
        width: 90,
    },
]

export const searchSchema: FormSchema[] = [
    {
        label: t('sys.table.type'),
        field: 'type',
        component: 'Input',
        show: false,
    },
    {
        label: t('sys.table.subType'),
        field: 'subType',
        component: 'Input',
        show: false,
    },
    {
        label: t('purchase.order.table.receiptNumber'),
        field: 'receiptNumber',
        component: 'Input',
        colProps: { span: 8 },
    },
    {
        label: t('purchase.order.table.productInformation'),
        field: 'productInfo',
        component: 'Input',
        helpMessage: t('purchase.order.form.noticeSeven'),
        colProps: { span: 7 },
    },
    {
        field: '[startDate, endDate]',
        label: t('purchase.order.table.receiptDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('purchase.order.header.startDate'), t('purchase.order.header.endDate')],
        },
        colProps: { span: 7 },
    },
]