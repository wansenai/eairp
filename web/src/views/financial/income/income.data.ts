import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {getAccountList} from "@/api/financial/account";
import {getRelatedPerson} from "@/api/report/report";
import {getOperatorList} from "@/api/basic/operator";

export const columns: BasicColumn[] = [
    {
        title: '名称',
        dataIndex: 'name',
        width: 60,
    },
    {
        title: '单据编号',
        dataIndex: 'receiptNumber',
        width: 130,
    },
    {
        title: '单据日期',
        dataIndex: 'receiptDate',
        width: 130,
    },
    {
        title: '财务人员',
        dataIndex: 'financialPerson',
        width: 70,
    },
    {
        title: '收入账户',
        dataIndex: 'incomeAccountName',
        width: 70,
    },
    {
        title: '收入金额',
        dataIndex: 'incomeAmount',
        width: 70,
    },
    {
        title: '备注',
        dataIndex: 'remark',
        width: 150,
    },
    {
        title: '状态',
        dataIndex: 'status',
        width: 70,
    },
]

export const searchFormSchema: FormSchema[] = [
    {
        label: '单据编号',
        field: 'receiptNumber',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        field: '[startDate, endDate]',
        label: '单据日期',
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: ['开始日期', '结束日期'],
        },
        colProps: { span: 7 },
    },
    {
        label: '收入账户',
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
        label: '往来单位',
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
        label: '财务人员',
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
        label: '单据状态',
        field: 'status',
        component: 'Select',
        colProps: {
            xl: 8,
            xxl: 8,
        },
        componentProps: {
            options: [
                { label: '未审核', value: 0, key: 0 },
                { label: '已审核', value: 1, key: 1 },
            ],
        },
    },
    {
        label: '单据备注',
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
        title: '收入项目',
        dataIndex: 'incomeExpenseName',
        width: 200,
    },
    {
        title: '金额',
        dataIndex: 'incomeExpenseAmount',
        width: 180,
    },
    {
        title: '备注',
        dataIndex: 'remark',
        width: 200,
    },
]