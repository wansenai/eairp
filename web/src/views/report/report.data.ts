import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import { h } from 'vue';
import {getWarehouseList} from "@/api/basic/warehouse";
import {getCategoryList} from "@/api/product/productCategory";

export const productStockColumns: BasicColumn[] = [
    {
        title: '库存流水',
        dataIndex: 'id',
        width: 80,
        customRender: ({ record }) => {
            return h(
            'a',
                {
                    onClick: () => {
                        console.info(record)
                    },
                },
                record.key !== '0' ? "流水" : undefined
            )
        }
    },
    {
        title: '商品条码',
        dataIndex: 'productBarcode',
        width: 120,
    },
    {
        title: '仓库',
        dataIndex: 'warehouseName',
        width: 120,
    },
    {
        title: '商品名称',
        dataIndex: 'productName',
        width: 120,
    },
    {
        title: '商品类别',
        dataIndex: 'productCategoryName',
        width: 80,
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
        title: '重量',
        dataIndex: 'productWeight',
        width: 80,
    },
    {
        title: '颜色',
        dataIndex: 'productColor',
        width: 80,
    },
    {
        title: '单位',
        dataIndex: 'productUnit',
        width: 80,
    },
    {
        title: '仓位货架',
        dataIndex: 'warehouseShelves',
        width: 120,
    },
    {
        title: '单价',
        dataIndex: 'unitPrice',
        width: 80,
    },
    {
        title: '初始库存',
        dataIndex: 'initialStock',
        width: 90,
    },
    {
        title: '当前库存',
        dataIndex: 'currentStock',
        width: 90,
    },
    {
        title: '库存金额',
        dataIndex: 'stockAmount',
        width: 90,
    },
]

export const searchProductStockSchema: FormSchema[] = [
    {
        label: '仓库',
        field: 'warehouseId',
        component: 'ApiSelect',
        componentProps: {
            api: getWarehouseList,
            resultField: 'data',
            labelField: 'warehouseName',
            valueField: 'id',
        },
        colProps: { span: 5 },
    },
    {
        label: '商品信息',
        field: 'productInfo',
        component: 'Input',
        colProps: { span: 5 },
    },
    {
        label: '商品类别',
        field: 'productCategoryId',
        component: 'ApiTreeSelect',
        componentProps: {
            api: getCategoryList,
            resultField: 'data',
            labelField: 'categoryName',
            valueField: 'id',
        },
        colProps: { span: 5 }
    },
    {
        label: '仓位货架',
        field: 'warehouseShelves',
        component: 'Input',
        colProps: { span: 5 },
    },
]

export const stockFlowColumns: BasicColumn[] = [
    {
        title: '单据编号',
        dataIndex: 'receiptNumber',
        width: 120,
    },
    {
        title: '类型',
        dataIndex: 'subType',
        width: 120,
    },
    {
        title: '商品名称',
        dataIndex: 'productName',
        width: 120,
    },
    {
        title: '数量',
        dataIndex: 'productNumber',
        width: 120,
    },
    {
        title: '日期',
        dataIndex: 'receiptDate',
        width: 120,
    }
]

export const searchStockFlowSchema: FormSchema[] = [
    {
        label: '单据编号',
        field: 'receiptNumber',
        component: 'Input',
        colProps: { span: 5 },
    },
    {
        field: '[startDate, endDate]',
        label: '单据日期',
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: ['开始日期', '结束日期'],
        },
        colProps: { span: 5 },
    },
]