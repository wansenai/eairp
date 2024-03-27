import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {getAccountList} from "@/api/financial/account";
import {getRelatedPerson} from "@/api/report/report";
import {getOperatorList} from "@/api/basic/operator";
import {useI18n} from "@/hooks/web/useI18n";

export const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: t('financial.expense.table.name'),
        dataIndex: 'name',
        width: 120,
    },
    {
        title: t('financial.expense.table.receiptNumber'),
        dataIndex: 'receiptNumber',
        width: 130,
    },
    {
        title: t('financial.expense.table.receiptDate'),
        dataIndex: 'receiptDate',
        width: 130,
    },
    {
        title: t('financial.expense.table.financialPerson'),
        dataIndex: 'financialPerson',
        width: 70,
    },
    {
        title: t('financial.expense.table.expenseAccount'),
        dataIndex: 'expenseAccountName',
        width: 70,
    },
    {
        title: t('financial.expense.table.expenseAmount'),
        dataIndex: 'expenseAmount',
        width: 70,
    },
    {
        title: t('financial.expense.table.remark'),
        dataIndex: 'remark',
        width: 150,
    },
    {
        title: t('financial.expense.table.status'),
        dataIndex: 'status',
        width: 70,
    },
]

export const searchFormSchema: FormSchema[] = [
    {
        label: t('financial.expense.header.receiptNumber'),
        field: 'receiptNumber',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        field: '[startDate, endDate]',
        label: t('financial.expense.header.receiptDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('financial.expense.header.starDate'), t('financial.expense.header.endDate')],
        },
        colProps: { span: 7 },
    },
    {
        label: t('financial.expense.header.expenseAccount'),
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
        label: t('financial.expense.header.correspondenceUnit'),
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
        label: t('financial.expense.header.financialPerson'),
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
        label: t('financial.expense.header.status'),
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
        label: t('financial.expense.header.remark'),
        field: 'remark',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },

    }
]

export const expenseReceiptTableColumns: BasicColumn[] = [
    {
        title: t('financial.expense.view.expenseName'),
        dataIndex: 'incomeExpenseName',
        width: 200,
    },
    {
        title: t('financial.expense.view.amount'),
        dataIndex: 'incomeExpenseAmount',
        width: 180,
    },
    {
        title: t('financial.expense.view.remark'),
        dataIndex: 'remark',
        width: 200,
    },
]