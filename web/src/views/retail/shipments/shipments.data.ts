import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {useI18n} from "@/hooks/web/useI18n";
import {getMemberList} from "@/api/basic/member";
import {getAccountList} from "@/api/financial/account";

export const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: t('retail.shipments.table.member'),
        dataIndex: 'memberName',
        width: 60,
    },
    {
        title: t('retail.shipments.table.receiptNumber'),
        dataIndex: 'receiptNumber',
        width: 130,
    },
    {
        title: t('retail.shipments.table.productInformation'),
        dataIndex: 'productInfo',
        width: 80,
    },
    {
        title: t('retail.shipments.table.productQuantity'),
        dataIndex: 'productNumber',
        width: 60,
    },
    {
        title: t('retail.shipments.table.totalAmount'),
        dataIndex: 'totalPrice',
        width: 60,
    },
    {
        title: t('retail.shipments.table.amountCollection'),
        dataIndex: 'collectionAmount',
        width: 80,
    },
    {
        title: t('retail.shipments.table.changeAmount'),
        dataIndex: 'backAmount',
        width: 80,
    },
    {
        title: t('retail.shipments.table.receiptDate'),
        dataIndex: 'receiptDate',
        width: 130,
    },
    {
        title: t('retail.shipments.table.operator'),
        dataIndex: 'operator',
        width: 60,
    },
    {
        title: t('retail.shipments.table.status'),
        dataIndex: 'status',
        width: 80,
    },
]

export const searchFormSchema: FormSchema[] = [
    {
        label: t('retail.shipments.table.receiptNumber'),
        field: 'receiptNumber',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        field: '[startDate, endDate]',
        label: t('retail.shipments.table.receiptDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('retail.shipments.header.startDate'), t('retail.shipments.header.endDate')],
        },
        colProps: { span: 7 },
    },
    {
        label: t('retail.shipments.header.settlementAccount'),
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
        label: t('retail.shipments.table.productInformation'),
        field: 'productInfo',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('retail.shipments.table.member'),
        field: 'memberId',
        component: 'ApiSelect',
        componentProps: {
            api: getMemberList,
            resultField: 'data',
            labelField: 'memberName',
            valueField: 'id',
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('retail.shipments.table.status'),
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
        label: t('retail.shipments.header.receiptRemark'),
        field: 'remark',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },

    }
]

export const retailShipmentsTableColumns: BasicColumn[] = [
    {
        title: t('retail.shipments.view.member'),
        dataIndex: 'warehouseName',
        width: 100,
    },
    {
        title: t('retail.shipments.form.table.barCode'),
        dataIndex: 'barCode',
        width: 100,
    },
    {
        title: t('retail.shipments.form.table.name'),
        dataIndex: 'productName',
        width: 120,
    },
    {
        title: t('retail.shipments.form.table.standard'),
        dataIndex: 'productStandard',
        width: 120,
    },
    {
        title: t('retail.shipments.form.table.model'),
        dataIndex: 'productModel',
        width: 120,
    },
    {
        title: t('retail.shipments.form.table.color'),
        dataIndex: 'productColor',
        width: 70,
    },
    {
        title: t('retail.shipments.form.table.stock'),
        dataIndex: 'stock',
        width: 80,
    },
    {
        title: t('retail.shipments.form.table.unit'),
        dataIndex: 'productUnit',
        width: 60,
    },
    {
        title: t('retail.shipments.form.table.quantity'),
        dataIndex: 'productNumber',
        width: 60,
    },
    {
        title: t('retail.shipments.form.table.unitPrice'),
        dataIndex: 'unitPrice',
        width: 60,
    },
    {
        title: t('retail.shipments.form.table.amount'),
        dataIndex: 'amount',
        width: 60,
    },
    {
        title: t('retail.shipments.view.remark'),
        dataIndex: 'remark',
        width: 100,
    },
]