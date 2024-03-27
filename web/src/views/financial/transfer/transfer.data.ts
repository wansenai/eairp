import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {getAccountList} from "@/api/financial/account";
import {getOperatorList} from "@/api/basic/operator";
import {useI18n} from "@/hooks/web/useI18n";

export const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: t('financial.transfer.table.receiptNumber'),
        dataIndex: 'receiptNumber',
        width: 130,
    },
    {
        title: t('financial.transfer.table.receiptDate'),
        dataIndex: 'receiptDate',
        width: 130,
    },
    {
        title: t('financial.transfer.table.financialPerson'),
        dataIndex: 'financialPerson',
        width: 60,
    },
    {
        title: t('financial.transfer.table.paymentAccount'),
        dataIndex: 'paymentAccountName',
        width: 130,
    },
    {
        title: t('financial.transfer.table.paymentAmount'),
        dataIndex: 'paymentAmount',
        width: 60,
    },
    {
        title: t('financial.transfer.table.remark'),
        dataIndex: 'remark',
        width: 200,
    },
    {
        title: t('financial.transfer.table.status'),
        dataIndex: 'status',
        width: 60,
    },
]

export const searchFormSchema: FormSchema[] = [
    {
        label: t('financial.transfer.header.receiptNumber'),
        field: 'receiptNumber',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        field: '[startDate, endDate]',
        label: t('financial.transfer.header.receiptDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('financial.transfer.header.starDate'), t('financial.transfer.header.endDate')],
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('financial.transfer.header.paymentAccount'),
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
        label: t('financial.transfer.header.financialPerson'),
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
        label: t('financial.transfer.header.status'),
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
        label: t('financial.transfer.header.remark'),
        field: 'remark',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },

    }
]

export const transferReceiptTableColumns: BasicColumn[] = [
    {
        title: t('financial.transfer.view.accountName'),
        dataIndex: 'accountName',
        width: 150,
    },
    {
        title: t('financial.transfer.view.amount'),
        dataIndex: 'transferAmount',
        width: 100,
    },
    {
        title: t('financial.transfer.view.remark'),
        dataIndex: 'remark',
        width: 200,
    },
]