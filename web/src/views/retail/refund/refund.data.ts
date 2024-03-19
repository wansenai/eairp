import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {useI18n} from "@/hooks/web/useI18n";
import {getMemberList} from "@/api/basic/member";
import {getAccountList} from "@/api/financial/account";

export const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: t('retail.refund.table.customer'),
        dataIndex: 'memberName',
        width: 60,
    },
    {
        title: t('retail.refund.table.receiptNumber'),
        dataIndex: 'receiptNumber',
        width: 130,
    },
    {
        title: t('retail.refund.table.productInformation'),
        dataIndex: 'productInfo',
        width: 80,
    },
    {
        title: t('retail.refund.table.totalAmount'),
        dataIndex: 'totalPrice',
        width: 60,
    },
    {
        title: t('retail.refund.table.paymentAmount'),
        dataIndex: 'paymentAmount',
        width: 80,
    },
    {
        title: t('retail.refund.table.changeAmount'),
        dataIndex: 'backAmount',
        width: 80,
    },
    {
        title: t('retail.refund.table.receiptDate'),
        dataIndex: 'receiptDate',
        width: 130,
    },
    {
        title: t('retail.refund.table.operator'),
        dataIndex: 'operator',
        width: 60,
    },
    {
        title: t('retail.refund.table.status'),
        dataIndex: 'status',
        width: 80,
    },
]

export const searchFormSchema: FormSchema[] = [
    {
        label: t('retail.refund.table.receiptNumber'),
        field: 'receiptNumber',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        field: '[startDate, endDate]',
        label: t('retail.refund.table.receiptDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('retail.refund.header.startDate'), t('retail.refund.header.endDate')],
        },
        colProps: { span: 7 },
    },
    {
        label: t('retail.refund.header.settlementAccount'),
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
        label: t('retail.refund.table.productInformation'),
        field: 'productInfo',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('retail.refund.table.customer'),
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
        label: t('retail.refund.table.status'),
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
        label: t('retail.refund.header.receiptRemark'),
        field: 'remark',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },

    }
]