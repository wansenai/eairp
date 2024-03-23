import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {useI18n} from "@/hooks/web/useI18n";
import {getSupplierList} from "@/api/basic/supplier";

export const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: t('purchase.storage.table.supplier'),
        dataIndex: 'supplierName',
        width: 130,
    },
    {
        title: t('purchase.storage.table.receiptNumber'),
        dataIndex: 'receiptNumber',
        width: 130,
    },
    {
        title: t('purchase.storage.table.productInformation'),
        dataIndex: 'productInfo',
        width: 80,
    },
    {
        title: t('purchase.storage.table.productQuantity'),
        dataIndex: 'productNumber',
        width: 50,
    },
    {
        title: t('purchase.storage.table.totalAmount'),
        dataIndex: 'totalAmount',
        width: 60,
    },
    {
        title: t('purchase.storage.table.totalIncludingTax'),
        dataIndex: 'taxIncludedAmount',
        width: 80,
    },
    {
        title: t('purchase.storage.table.paymentAmount'),
        dataIndex: 'totalPaymentAmount',
        width: 80,
    },
    {
        title:t('purchase.storage.table.thisTimePaymentAmount'),
        dataIndex: 'thisPaymentAmount',
        width: 80,
    },
    {
        title: t('purchase.storage.table.thisTimeArrearsAmount'),
        dataIndex: 'thisArrearsAmount',
        width: 80,
    },
    {
        title: t('purchase.storage.table.receiptDate'),
        dataIndex: 'receiptDate',
        width: 130,
    },
    {
        title: t('purchase.storage.table.operator'),
        dataIndex: 'operator',
        width: 60,
    },
    {
        title: t('purchase.storage.table.status'),
        dataIndex: 'status',
        width: 80,
    },
]

export const searchFormSchema: FormSchema[] = [
    {
        label: t('purchase.storage.table.receiptNumber'),
        field: 'receiptNumber',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        field: '[startDate, endDate]',
        label: t('purchase.storage.table.receiptDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('purchase.storage.header.startDate'), t('purchase.storage.header.endDate')],
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('purchase.storage.table.supplier'),
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
        label: t('purchase.storage.table.productInformation'),
        field: 'productInfo',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('purchase.storage.table.status'),
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
        label: t('purchase.storage.header.receiptRemark'),
        field: 'remark',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },

    }
]