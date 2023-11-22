import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {getAccountList} from "@/api/financial/account";
import {getCustomerList} from "@/api/basic/customer";
import {getOperatorList} from "@/api/basic/operator";

export const columns: BasicColumn[] = [
    {
        title: '客户',
        dataIndex: 'customerName',
        width: 120,
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
        title: '收款账户',
        dataIndex: 'collectionAccountName',
        width: 100,
    },
    {
        title: '合计收款',
        dataIndex: 'totalCollectionAmount',
        width: 70,
    },
    {
        title: '优惠金额',
        dataIndex: 'discountAmount',
        width: 70,
    },
    {
        title: '实际收款',
        dataIndex: 'actualCollectionAmount',
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
        label: '收款账户',
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
        label: '客户',
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

export const collectionReceiptTableColumns: BasicColumn[] = [
    {
        title: '销售单据编号',
        dataIndex: 'saleReceiptNumber',
        width: 180,
    },
    {
        title: '应收欠款',
        dataIndex: 'receivableArrears',
        width: 80,
    },
    {
        title: '已收欠款',
        dataIndex: 'receivedArrears',
        width: 80,
    },
    {
        title: '本次收款',
        dataIndex: 'thisCollectionAmount',
        width: 80,
    },
    {
        title: '备注',
        dataIndex: 'remark',
        width: 200,
    },
]