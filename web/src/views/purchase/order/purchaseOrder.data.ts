import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {useI18n} from "@/hooks/web/useI18n";
import {getSupplierList} from "@/api/basic/supplier";

export const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: '供应商',
        dataIndex: 'supplierName',
        width: 130,
    },
    {
        title: '单据编号',
        dataIndex: 'receiptNumber',
        width: 130,
    },
    {
        title: '商品信息',
        dataIndex: 'productInfo',
        width: 80,
    },
    {
        title: '数量',
        dataIndex: 'productNumber',
        width: 50,
    },
    {
        title: '金额合计',
        dataIndex: 'totalAmount',
        width: 60,
    },
    {
        title: '含税合计',
        dataIndex: 'taxRateTotalAmount',
        width: 80,
    },
    {
        title: '收取定金',
        dataIndex: 'deposit',
        width: 80,
    },
    {
        title: '单据日期',
        dataIndex: 'receiptDate',
        width: 130,
    },
    {
        title: '操作员',
        dataIndex: 'operator',
        width: 60,
    },
    {
        title: '状态',
        dataIndex: 'status',
        width: 80,
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
        label: '商品信息',
        field: 'productInfo',
        component: 'Input',
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
                { label: '部分采购', value: 2, key: 2 },
                { label: '完成采购', value: 3, key: 3 },
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

export const purchaseOrderTableColumns: BasicColumn[] = [
    {
        title: '仓库名称',
        dataIndex: 'warehouseName',
        width: 100,
    },
    {
        title: '条码',
        dataIndex: 'barCode',
        width: 100,
    },
    {
        title: '商品名称',
        dataIndex: 'productName',
        width: 120,
    },
    {
        title: '规格',
        dataIndex: 'productStandard',
        width: 120,
    },
    {
        title: '型号',
        dataIndex: 'productModel',
        width: 120,
    },
    {
        title: '颜色',
        dataIndex: 'productColor',
        width: 70,
    },
    {
        title: '库存',
        dataIndex: 'stock',
        width: 80,
    },
    {
        title: '单位',
        dataIndex: 'productUnit',
        width: 60,
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
        width: 60,
    },
    {
        title: '税额',
        dataIndex: 'taxAmount',
        width: 60,
    },
    {
        title: '税额',
        dataIndex: 'taxTotalPrice',
        width: 60,
    },
    {
        title: '备注',
        dataIndex: 'remark',
        width: 100,
    },
]