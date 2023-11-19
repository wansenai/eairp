import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {getWarehouseList} from "@/api/basic/warehouse";
import {getCategoryList} from "@/api/product/productCategory";
import {getMemberList} from "@/api/basic/member";
export const productStockColumns: BasicColumn[] = [
    {
        title: '产品id',
        dataIndex: 'productId',
        width: 120,
        ifShow: false
    },
    {
        title: '仓库id',
        dataIndex: 'warehouseId',
        width: 60,
        ifShow: false
    },
    {
        title: '库存流水',
        dataIndex: 'id',
        width: 80,
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
        width: 100,
    },
    {
        title: '单价',
        dataIndex: 'unitPrice',
        width: 70,
    },
    {
        title: '初始库存',
        dataIndex: 'initialStock',
        width: 70,
    },
    {
        title: '当前库存',
        dataIndex: 'currentStock',
        width: 70,
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
        width: 180,
    },
    {
        title: '类型',
        dataIndex: 'type',
        width: 80,
    },
    {
        title: '商品条码',
        dataIndex: 'productBarcode',
        width: 120,
    },
    {
        title: '商品名称',
        dataIndex: 'productName',
        width: 120,
    },
    {
        title: '仓库',
        dataIndex: 'warehouseName',
        width: 120,
    },
    {
        title: '数量',
        dataIndex: 'productNumber',
        width: 60,
    },
    {
        title: '单据日期',
        dataIndex: 'receiptDate',
        width: 150,
    }
]

export const searchStockFlowSchema: FormSchema[] = [
    {
        label: '单据编号',
        field: 'receiptNumber',
        component: 'Input',
        colProps: { span: 10 },
    },
    {
        field: '[startDate, endDate]',
        label: '单据日期',
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: ['开始日期', '结束日期'],
        },
        colProps: { span: 10 },
    },
]

export const accountStatisticsColumns: BasicColumn[] = [
    {
        title: '账户流水',
        dataIndex: 'accountId',
        width: 80,
    },
    {
        title: '账户名称',
        dataIndex: 'accountName',
        width: 180,
    },
    {
        title: '账户编号',
        dataIndex: 'accountNumber',
        width: 120,
    },
    {
        title: '期初金额',
        dataIndex: 'initialAmount',
        width: 120,
    },
    {
        title: '本月发生金额',
        dataIndex: 'thisMonthChangeAmount',
        width: 120,
    },
    {
        title: '当前余额',
        dataIndex: 'currentAmount',
        width: 120,
    },
]

export const searchAccountSchema: FormSchema[] = [
    {
        label: '账户名称',
        field: 'accountName',
        component: 'Input',
        colProps: { span: 10 },
    },
    {
        label: '账户编号',
        field: 'accountNumber',
        component: 'Input',
        colProps: { span: 10 },
    },
]

export const accountFlowColumns: BasicColumn[] = [
    {
        title: '单据编号',
        dataIndex: 'receiptNumber',
        width: 180,
    },
    {
        title: '单据类型',
        dataIndex: 'subType',
        width: 80,
    },
    {
        title: '收付款放',
        dataIndex: 'useType',
        width: 90,
    },
    {
        title: '名称',
        dataIndex: 'name',
        width: 120,
    },
    {
        title: '金额',
        dataIndex: 'amount',
        width: 110,
    },
    {
        title: '余额',
        dataIndex: 'balance',
        width: 110,
    },
    {
        title: '单据日期',
        dataIndex: 'receiptDate',
        width: 150,
    }
]

export const searchRetailSchema: FormSchema[] = [
    {
        label: '商品信息',
        field: 'productExtendInfo',
        component: 'Input',
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
    {
        label: '会员卡号',
        field: 'memberId',
        component: 'ApiSelect',
        componentProps: {
            api: getMemberList,
            resultField: 'data',
            labelField: 'memberName',
            valueField: 'id',
        },
        colProps: { span: 7 },
    },
]

export const retailStatisticsColumns: BasicColumn[] = [
    {
        title: '商品条码',
        dataIndex: 'productBarcode',
        width: 120,
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
        width: 90,
    },
    {
        title: '扩展信息',
        dataIndex: 'productExtendInfo',
        width: 120,
    },
    {
        title: '单位',
        dataIndex: 'productUnit',
        width: 80,
    },
    {
        title: '零售数量',
        dataIndex: 'retailNumber',
        width: 70,
    },
    {
        title: '零售金额',
        dataIndex: 'retailAmount',
        width: 70,
    },
    {
        title: '退货数量',
        dataIndex: 'retailRefundNumber',
        width: 70,
    },
    {
        title: '退货金额',
        dataIndex: 'retailRefundAmount',
        width: 70,
    },
    {
        title: '实际零售金额',
        dataIndex: 'retailLastAmount',
        width: 70,
    }
]
