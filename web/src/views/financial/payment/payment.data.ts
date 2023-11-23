import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {getAccountList} from "@/api/financial/account";
import {getSupplierList} from "@/api/basic/supplier";
import {getOperatorList} from "@/api/basic/operator";

export const columns: BasicColumn[] = [
    {
        title: '客户',
        dataIndex: 'supplierName',
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
        title: '付款账户',
        dataIndex: 'paymentAccountName',
        width: 100,
    },
    {
        title: '合计付款',
        dataIndex: 'totalPaymentAmount',
        width: 70,
    },
    {
        title: '优惠金额',
        dataIndex: 'discountAmount',
        width: 70,
    },
    {
        title: '实际付款',
        dataIndex: 'actualPaymentAmount',
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
        label: '付款账户',
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
        label: '供应商',
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

export const paymentReceiptTableColumns: BasicColumn[] = [
    {
        title: '采购单据编号',
        dataIndex: 'purchaseReceiptNumber',
        width: 180,
    },
    {
        title: '应付欠款',
        dataIndex: 'paymentArrears',
        width: 80,
    },
    {
        title: '已付欠款',
        dataIndex: 'prepaidArrears',
        width: 80,
    },
    {
        title: '本次付款',
        dataIndex: 'thisPaymentAmount',
        width: 80,
    },
    {
        title: '备注',
        dataIndex: 'remark',
        width: 200,
    },
]

export const searchPurchaseArrearsFormSchema: FormSchema[] = [
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
        label: '商品信息',
        field: 'productInfo',
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
        title: '供应商',
        dataIndex: 'supplierName',
        width: 130,
    },
    {
        title: '单据编号',
        dataIndex: 'receiptNumber',
        width: 200,
    },
    {
        title: '单据日期',
        dataIndex: 'receiptDate',
        width: 140,
    },
    {
        title: '商品信息',
        dataIndex: 'productInfo',
        width: 150,
    },
    {
        title: '本单欠款',
        dataIndex: 'thisReceiptArrears',
        width: 75,
    },
    {
        title: '已付欠款',
        dataIndex: 'prepaidArrears',
        width: 75,
    },
    {
        title: '待付欠款',
        dataIndex: 'paymentArrears',
        width: 75,
    },
    {
        title: '操作员',
        dataIndex: 'operatorName',
        width: 75,
    },
    {
        title: '备注',
        dataIndex: 'remark',
        width: 140,
    },
]