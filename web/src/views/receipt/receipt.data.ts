import {BasicColumn, FormSchema} from "@/components/Table";

export const ReceiptDetailColumn: BasicColumn[] = [
    {
        title: 'id',
        dataIndex: 'id',
        width: 60,
        ifShow: false,
    },
    {
        title: '条码',
        dataIndex: 'productBarcode',
        width: 130,
    },
    {
        title: '商品名称',
        dataIndex: 'productName',
        width: 130,
    },
    {
        title: '商品规格',
        dataIndex: 'productStandard',
        width: 100,
    },
    {
        title: '商品型号',
        dataIndex: 'productModel',
        width: 100,
    },
    {
        title: '单位',
        dataIndex: 'unit',
        width: 80,
    },
    {
        title: '数量',
        dataIndex: 'productNumber',
        width: 60,
    },
    {
        title: '单价',
        dataIndex: 'unitPrice',
        width: 60,
    },
    {
        title: '金额',
        dataIndex: 'amount',
        width: 60,
    },
    {
        title: '税率(%)',
        dataIndex: 'taxRate',
        width: 80,
    },
    {
        title: '税额',
        dataIndex: 'taxAmount',
        width: 80,
    },
    {
        title: '优惠合计',
        dataIndex: 'taxIncludedAmount',
        width: 80,
    },
    {
        title: '备注',
        dataIndex: 'remark',
        width: 100,
    },
]

export const ReceiptColumn: BasicColumn[] = [
    {
        title: 'id',
        dataIndex: 'id',
        width: 60,
        ifShow: false,
    },
    {
        title: '名称',
        dataIndex: 'name',
        width: 70,
    },
    {
        title: '单据编号',
        dataIndex: 'receiptNumber',
        width: 190,
    },
    {
        title: '单据日期',
        dataIndex: 'receiptDate',
        width: 150,
    },
    {
        title: '商品信息',
        dataIndex: 'productInfo',
        width: 200,
    },
    {
        title: '数量',
        dataIndex: 'productNumber',
        width: 70,
    },
    {
        title: '金额合计',
        dataIndex: 'totalAmount',
        width: 70,
    },
    {
        title: '含税合计',
        dataIndex: 'taxRateTotalAmount',
        width: 70,
    },
    {
        title: '操作员',
        dataIndex: 'operator',
        width: 80,
    },
    {
        title: '状态',
        dataIndex: 'status',
        width: 90,
    },
]

export const searchSchema: FormSchema[] = [
    {
        label: '类型',
        field: 'type',
        component: 'Input',
        show: false,
    },
    {
        label: '子类型',
        field: 'subType',
        component: 'Input',
        show: false,
    },
    {
        label: '单据编号',
        field: 'receiptNumber',
        component: 'Input',
        colProps: { span: 8 },
    },
    {
        label: '商品信息',
        field: 'productInfo',
        component: 'Input',
        helpMessage: '支持商品名称、商品编号、商品规格、商品型号',
        colProps: { span: 7 },
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
]