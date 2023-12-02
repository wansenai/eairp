import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {getUserOperatorList} from "@/api/sys/user";

export const columns: BasicColumn[] = [
    {
        title: '单据编号',
        dataIndex: 'receiptNumber',
        width: 130,
    },
    {
        title: '商品信息',
        dataIndex: 'productInfo',
        width: 150,
    },
    {
        title: '单据日期',
        dataIndex: 'receiptDate',
        width: 130,
    },
    {
        title: '数量',
        dataIndex: 'productNumber',
        width: 100,
    },
    {
        title: '金额合计',
        dataIndex: 'totalAmount',
        width: 70,
    },
    {
        title: '操作员',
        dataIndex: 'operator',
        width: 70,
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
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: '操作员',
        field: 'operatorId',
        component: 'ApiSelect',
        componentProps: {
            api: getUserOperatorList,
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

export const allotShipmentTableColumns: BasicColumn[] = [
    {
        title: '调出方仓库',
        dataIndex: 'warehouseName',
        width: 100,
    },
    {
        title: '调入方仓库',
        dataIndex: 'otherWarehouseName',
        width: 100,
    },
    {
        title: '条码',
        dataIndex: 'barCode',
        width: 120,
    },
    {
        title: '商品名称',
        dataIndex: 'productName',
        width: 150,
    },
    {
        title: '规格',
        dataIndex: 'productStandard',
        width: 100,
    },
    {
        title: '型号',
        dataIndex: 'productModel',
        width: 100,
    },
    {
        title: '扩展信息',
        dataIndex: 'productExtendInfo',
        width: 150,
    },
    {
        title: '库存',
        dataIndex: 'stock',
        width: 70,
    },
    {
        title: '单位',
        dataIndex: 'productUnit',
        width: 70,
    },
    {
        title: '数量',
        dataIndex: 'productNumber',
        width: 70,
    },
    {
        title: '单价',
        dataIndex: 'unitPrice',
        width: 70,
    },
    {
        title: '金额',
        dataIndex: 'amount',
        width: 70,
    },
    {
        title: '备注',
        dataIndex: 'remark',
        width: 130,
    },
]