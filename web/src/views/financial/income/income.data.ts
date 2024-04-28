import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {getAccountList} from "@/api/financial/account";
import {getRelatedPerson} from "@/api/report/report";
import {getOperatorList} from "@/api/basic/operator";
import {useI18n} from "@/hooks/web/useI18n";

export const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: t('financial.income.table.name'),
        dataIndex: 'name',
        width: 120,
    },
    {
        title: t('financial.income.table.receiptNumber'),
        dataIndex: 'receiptNumber',
        width: 130,
    },
    {
        title: t('financial.income.table.receiptDate'),
        dataIndex: 'receiptDate',
        width: 130,
    },
    {
        title: t('financial.income.table.financialPerson'),
        dataIndex: 'financialPerson',
        width: 70,
    },
    {
        title: t('financial.income.table.incomeAccount'),
        dataIndex: 'incomeAccountName',
        width: 70,
    },
    {
        title: t('financial.income.table.incomeAmount'),
        dataIndex: 'incomeAmount',
        width: 70,
    },
    {
        title: t('financial.income.table.remark'),
        dataIndex: 'remark',
        width: 150,
    },
    {
        title: t('financial.income.table.status'),
        dataIndex: 'status',
        width: 70,
    },
]

export const searchFormSchema: FormSchema[] = [
    {
        label: t('financial.income.header.receiptNumber'),
        field: 'receiptNumber',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        field: '[startDate, endDate]',
        label: t('financial.income.header.receiptDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('financial.income.header.starDate'), t('financial.income.header.endDate')],
        },
        colProps: { span: 7 },
    },
    {
        label: t('financial.income.header.incomeAccount'),
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
        label: t('financial.income.header.correspondenceUnit'),
        field: 'relatedPersonId',
        component: 'ApiSelect',
        componentProps: {
            api: getRelatedPerson,
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
        label: t('financial.income.header.financialPerson'),
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
        label: t('financial.income.header.status'),
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
        label: t('financial.income.header.remark'),
        field: 'remark',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },

    }
]

export const incomeReceiptTableColumns: BasicColumn[] = [
    {
        title: t('financial.income.view.incomeExpenseName'),
        dataIndex: 'incomeExpenseName',
        width: 200,
    },
    {
        title: t('financial.income.view.amount'),
        dataIndex: 'incomeExpenseAmount',
        width: 180,
    },
    {
        title: t('financial.income.view.remark'),
        dataIndex: 'remark',
        width: 200,
    },
]