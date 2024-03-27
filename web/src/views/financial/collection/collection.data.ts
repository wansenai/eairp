import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {getAccountList} from "@/api/financial/account";
import {getCustomerList} from "@/api/basic/customer";
import {getOperatorList} from "@/api/basic/operator";
import {useI18n} from "@/hooks/web/useI18n";

export const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: t('financial.collection.table.customer'),
        dataIndex: 'customerName',
        width: 120,
    },
    {
        title: t('financial.collection.table.receiptNumber'),
        dataIndex: 'receiptNumber',
        width: 130,
    },
    {
        title: t('financial.collection.table.receiptDate'),
        dataIndex: 'receiptDate',
        width: 130,
    },
    {
        title: t('financial.collection.table.financialPerson'),
        dataIndex: 'financialPerson',
        width: 70,
    },
    {
        title: t('financial.collection.table.collectionAccount'),
        dataIndex: 'collectionAccountName',
        width: 100,
    },
    {
        title: t('financial.collection.table.totalCollection'),
        dataIndex: 'totalCollectionAmount',
        width: 70,
    },
    {
        title: t('financial.collection.table.discountAmount'),
        dataIndex: 'discountAmount',
        width: 70,
    },
    {
        title: t('financial.collection.table.actualCollection'),
        dataIndex: 'actualCollectionAmount',
        width: 70,
    },
    {
        title: t('financial.collection.table.remark'),
        dataIndex: 'remark',
        width: 150,
    },
    {
        title: t('financial.collection.table.status'),
        dataIndex: 'status',
        width: 70,
    },
]

export const searchFormSchema: FormSchema[] = [
    {
        label: t('financial.collection.header.receiptNumber'),
        field: 'receiptNumber',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        field: '[startDate, endDate]',
        label: t('financial.collection.header.receiptDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('financial.collection.header.starDate'), t('financial.collection.header.endDate')],
        },
        colProps: { span: 7 },
    },
    {
        label: t('financial.collection.header.collectionAccount'),
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
        label: t('financial.collection.header.customer'),
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
        label: t('financial.collection.header.financialPerson'),
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
        label: t('financial.collection.header.status'),
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
        label: t('financial.collection.header.remark'),
        field: 'remark',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    }
]

export const collectionReceiptTableColumns: BasicColumn[] = [
    {
        title:  t('financial.collection.view.saleReceiptNumber'),
        dataIndex: 'saleReceiptNumber',
        width: 180,
    },
    {
        title: t('financial.collection.view.receivableArrears'),
        dataIndex: 'receivableArrears',
        width: 80,
    },
    {
        title: t('financial.collection.view.receivedArrears'),
        dataIndex: 'receivedArrears',
        width: 80,
    },
    {
        title: t('financial.collection.view.thisTimeCollection'),
        dataIndex: 'thisCollectionAmount',
        width: 80,
    },
    {
        title: t('financial.collection.view.remark'),
        dataIndex: 'remark',
        width: 200,
    },
]

export const searchSaleArrearsFormSchema: FormSchema[] = [
    {
        label: t('financial.collection.header.receiptNumber'),
        field: 'receiptNumber',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('financial.collection.header.productInfo'),
        field: 'productInfo',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        field: '[startDate, endDate]',
        label: t('financial.collection.header.receiptDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('financial.collection.header.starDate'), t('financial.collection.header.endDate')],
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
]

export const saleArrearsReceiptTableColumns: BasicColumn[] = [
    {
        title: '销售单据id',
        dataIndex: 'id',
        ifShow: false,
        width: 0,
    },
    {
        title: t('financial.collection.table.customer'),
        dataIndex: 'customerName',
        width: 90,
    },
    {
        title: t('financial.collection.table.receiptNumber'),
        dataIndex: 'receiptNumber',
        width: 200,
    },
    {
        title: t('financial.collection.table.receiptDate'),
        dataIndex: 'receiptDate',
        width: 150,
    },
    {
        title: t('financial.collection.header.productInfo'),
        dataIndex: 'productInfo',
        width: 150,
    },
    {
        title: t('financial.collection.view.thisReceiptArrears'),
        dataIndex: 'thisReceiptArrears',
        width: 80,
    },
    {
        title: t('financial.collection.view.receivedArrears'),
        dataIndex: 'receivedArrears',
        width: 80,
    },
    {
        title: t('financial.collection.view.receivableArrears'),
        dataIndex: 'receivableArrears',
        width: 80,
    },
    {
        title: t('financial.collection.view.operator'),
        dataIndex: 'operatorName',
        width: 80,
    },
    {
        title: t('financial.collection.view.remark'),
        dataIndex: 'remark',
        width: 140,
    },
]