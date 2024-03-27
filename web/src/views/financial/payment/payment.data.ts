import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {getAccountList} from "@/api/financial/account";
import {getSupplierList} from "@/api/basic/supplier";
import {getOperatorList} from "@/api/basic/operator";
import {useI18n} from "@/hooks/web/useI18n";

export const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: t('financial.payment.table.supplier'),
        dataIndex: 'supplierName',
        width: 120,
    },
    {
        title: t('financial.payment.table.receiptNumber'),
        dataIndex: 'receiptNumber',
        width: 130,
    },
    {
        title: t('financial.payment.table.receiptDate'),
        dataIndex: 'receiptDate',
        width: 130,
    },
    {
        title: t('financial.payment.table.financialPerson'),
        dataIndex: 'financialPerson',
        width: 70,
    },
    {
        title: t('financial.payment.table.paymentAccount'),
        dataIndex: 'paymentAccountName',
        width: 100,
    },
    {
        title: t('financial.payment.table.totalPayment'),
        dataIndex: 'totalPaymentAmount',
        width: 70,
    },
    {
        title: t('financial.payment.table.discountAmount'),
        dataIndex: 'discountAmount',
        width: 70,
    },
    {
        title: t('financial.payment.table.actualPayment'),
        dataIndex: 'actualPaymentAmount',
        width: 70,
    },
    {
        title: t('financial.payment.table.remark'),
        dataIndex: 'remark',
        width: 150,
    },
    {
        title: t('financial.payment.table.status'),
        dataIndex: 'status',
        width: 70,
    },
]

export const searchFormSchema: FormSchema[] = [
    {
        label: t('financial.payment.header.receiptNumber'),
        field: 'receiptNumber',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        field: '[startDate, endDate]',
        label: t('financial.payment.header.receiptDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('financial.payment.header.starDate'), t('financial.payment.header.endDate')],
        },
        colProps: { span: 7 },
    },
    {
        label: t('financial.payment.header.paymentAccount'),
        field: 'accountId',
        component: 'ApiSelect',
        componentProps: {
            api: getAccountList,
            resultField: 'data',
            labelField: 'accountName',
            valueField: 'id',
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('financial.payment.header.supplier'),
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
        label: t('financial.payment.header.financialPerson'),
        field: 'financialPersonId',
        component: 'ApiSelect',
        componentProps: {
            api: getOperatorList,
            params: '财务员',
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
        label: t('financial.payment.header.status'),
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
        label: t('financial.payment.header.remark'),
        field: 'remark',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    }
]

export const paymentReceiptTableColumns: BasicColumn[] = [
    {
        title:  t('financial.payment.view.purchaseReceiptNumber'),
        dataIndex: 'purchaseReceiptNumber',
        width: 180,
    },
    {
        title: t('financial.payment.view.payableArrears'),
        dataIndex: 'paymentArrears',
        width: 80,
    },
    {
        title: t('financial.payment.view.paidArrears'),
        dataIndex: 'prepaidArrears',
        width: 80,
    },
    {
        title: t('financial.payment.view.thisTimePayment'),
        dataIndex: 'thisPaymentAmount',
        width: 80,
    },
    {
        title: t('financial.payment.view.remark'),
        dataIndex: 'remark',
        width: 200,
    },
]

export const searchPurchaseArrearsFormSchema: FormSchema[] = [
    {
        label: t('financial.payment.header.receiptNumber'),
        field: 'receiptNumber',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('financial.payment.header.productInfo'),
        field: 'productInfo',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        field: '[startDate, endDate]',
        label: t('financial.payment.header.receiptDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('financial.payment.header.starDate'), t('financial.payment.header.endDate')],
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
]

export const purchaseArrearsReceiptTableColumns: BasicColumn[] = [
    {
        title: '销售单据id',
        dataIndex: 'id',
        ifShow: false,
        width: 0,
    },
    {
        title: t('financial.payment.table.supplier'),
        dataIndex: 'supplierName',
        width: 130,
    },
    {
        title: t('financial.payment.table.receiptNumber'),
        dataIndex: 'receiptNumber',
        width: 200,
    },
    {
        title: t('financial.payment.table.receiptDate'),
        dataIndex: 'receiptDate',
        width: 140,
    },
    {
        title: t('financial.payment.header.productInfo'),
        dataIndex: 'productInfo',
        width: 150,
    },
    {
        title: t('financial.payment.view.thisReceiptArrears'),
        dataIndex: 'thisReceiptArrears',
        width: 75,
    },
    {
        title:  t('financial.payment.view.paidArrears'),
        dataIndex: 'prepaidArrears',
        width: 75,
    },
    {
        title: t('financial.payment.view.payableArrears'),
        dataIndex: 'paymentArrears',
        width: 75,
    },
    {
        title: t('financial.payment.view.operator'),
        dataIndex: 'operatorName',
        width: 75,
    },
    {
        title: t('financial.payment.view.remark'),
        dataIndex: 'remark',
        width: 140,
    },
]