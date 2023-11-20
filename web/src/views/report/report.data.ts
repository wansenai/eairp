import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {getWarehouseList} from "@/api/basic/warehouse";
import {getCategoryList} from "@/api/product/productCategory";
import {getMemberList} from "@/api/basic/member";
import {getSupplierList} from "@/api/basic/supplier";
import {getCustomerList} from "@/api/basic/customer";
import {getRelatedPerson} from "@/api/report/report";
import {getOperatorList} from "@/api/basic/operator";
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
        width: 100,
    },
    {
        title: '仓库',
        dataIndex: 'warehouseName',
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


export const searchPurchaseSchema: FormSchema[] = [
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
        label: '供应商',
        field: 'supplierId',
        component: 'ApiSelect',
        componentProps: {
            api: getSupplierList,
            resultField: 'data',
            labelField: 'supplierName',
            valueField: 'id',
        },
        colProps: { span: 7 },
    },
]

export const purchaseStatisticsColumns: BasicColumn[] = [
    {
        title: '商品条码',
        dataIndex: 'productBarcode',
        width: 100,
    },
    {
        title: '仓库',
        dataIndex: 'warehouseName',
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
        title: '采购数量',
        dataIndex: 'purchaseNumber',
        width: 70,
    },
    {
        title: '采购金额',
        dataIndex: 'purchaseAmount',
        width: 70,
    },
    {
        title: '退货数量',
        dataIndex: 'purchaseRefundNumber',
        width: 70,
    },
    {
        title: '退货金额',
        dataIndex: 'purchaseRefundAmount',
        width: 70,
    },
    {
        title: '实际采购金额',
        dataIndex: 'purchaseLastAmount',
        width: 70,
    }
]

export const searchSalesSchema: FormSchema[] = [
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
        label: '客户',
        field: 'customerId',
        component: 'ApiSelect',
        componentProps: {
            api: getCustomerList,
            resultField: 'data',
            labelField: 'customerName',
            valueField: 'id',
        },
        colProps: { span: 7 },
    },
]

export const salesStatisticsColumns: BasicColumn[] = [
    {
        title: '商品条码',
        dataIndex: 'productBarcode',
        width: 100,
    },
    {
        title: '仓库',
        dataIndex: 'warehouseName',
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
        title: '销售数量',
        dataIndex: 'salesNumber',
        width: 70,
    },
    {
        title: '销售金额',
        dataIndex: 'salesAmount',
        width: 70,
    },
    {
        title: '退货数量',
        dataIndex: 'salesRefundNumber',
        width: 70,
    },
    {
        title: '退货金额',
        dataIndex: 'salesRefundAmount',
        width: 70,
    },
    {
        title: '实际销售金额',
        dataIndex: 'salesLastAmount',
        width: 70,
    }
]

export const searchShipmentsDetailSchema: FormSchema[] = [
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
        label: '商品信息',
        field: 'productInfo',
        component: 'Input',
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
        label: '仓库',
        field: 'warehouseId',
        component: 'ApiSelect',
        componentProps: {
            api: getWarehouseList,
            resultField: 'data',
            labelField: 'warehouseName',
            valueField: 'id',
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: '操作人员',
        field: 'operatorId',
        component: 'ApiSelect',
        componentProps: {
            api: getOperatorList,
            params: "所有",
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
        label: '单据备注',
        field: 'remark',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    }
]

export const shipmentsDetailStatisticsColumns: BasicColumn[] = [
    {
        title: '单据编号',
        dataIndex: 'receiptNumber',
        width: 170,
    },
    {
        title: '类型',
        dataIndex: 'type',
        width: 70,
    },
    {
        title: '往来人员',
        dataIndex: 'name',
        width: 70,
    },
    {
        title: '商品条码',
        dataIndex: 'productBarcode',
        width: 100,
    },
    {
        title: '仓库',
        dataIndex: 'warehouseName',
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
        width: 90,
    },
    {
        title: '单位',
        dataIndex: 'productUnit',
        width: 60,
    },
    {
        title: '数量',
        dataIndex: 'productNumber',
        width: 65,
    },
    {
        title: '单价',
        dataIndex: 'unitPrice',
        width: 65,
    },
    {
        title: '金额',
        dataIndex: 'amount',
        width: 65,
    },
    {
        title: '税率(%)',
        dataIndex: 'taxRate',
        width: 65,
    },
    {
        title: '税额',
        dataIndex: 'taxAmount',
        width: 65,
    },
    {
        title: '出库日期',
        dataIndex: 'createTime',
        width: 140,
    },
]

export const searchStorageDetailSchema: FormSchema[] = [
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
        label: '商品信息',
        field: 'productInfo',
        component: 'Input',
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
        label: '仓库',
        field: 'warehouseId',
        component: 'ApiSelect',
        componentProps: {
            api: getWarehouseList,
            resultField: 'data',
            labelField: 'warehouseName',
            valueField: 'id',
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: '操作人员',
        field: 'operatorId',
        component: 'ApiSelect',
        componentProps: {
            api: getOperatorList,
            params: "所有",
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
        label: '单据备注',
        field: 'remark',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    }
]

export const storageDetailStatisticsColumns: BasicColumn[] = [
    {
        title: '单据编号',
        dataIndex: 'receiptNumber',
        width: 170,
    },
    {
        title: '类型',
        dataIndex: 'type',
        width: 70,
    },
    {
        title: '往来人员',
        dataIndex: 'name',
        width: 70,
    },
    {
        title: '商品条码',
        dataIndex: 'productBarcode',
        width: 100,
    },
    {
        title: '仓库',
        dataIndex: 'warehouseName',
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
        width: 90,
    },
    {
        title: '单位',
        dataIndex: 'productUnit',
        width: 80,
    },
    {
        title: '数量',
        dataIndex: 'productNumber',
        width: 65,
    },
    {
        title: '单价',
        dataIndex: 'unitPrice',
        width: 65,
    },
    {
        title: '金额',
        dataIndex: 'amount',
        width: 65,
    },
    {
        title: '税率(%)',
        dataIndex: 'taxRate',
        width: 65,
    },
    {
        title: '税额',
        dataIndex: 'taxAmount',
        width: 65,
    },
    {
        title: '出库日期',
        dataIndex: 'createTime',
        width: 140,
    },
]

export const searchShipmentsSummarySchema: FormSchema[] = [
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
        label: '仓库',
        field: 'warehouseId',
        component: 'ApiSelect',
        componentProps: {
            api: getWarehouseList,
            resultField: 'data',
            labelField: 'warehouseName',
            valueField: 'id',
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    }
]

export const shipmentsSummaryStatisticsColumns: BasicColumn[] = [
    {
        title: '商品条码',
        dataIndex: 'productBarcode',
        width: 100,
    },
    {
        title: '仓库',
        dataIndex: 'warehouseName',
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
        width: 90,
    },
    {
        title: '单位',
        dataIndex: 'productUnit',
        width: 60,
    },
    {
        title: '出库数量',
        dataIndex: 'shipmentsNumber',
        width: 65,
    },
    {
        title: '出库金额',
        dataIndex: 'shipmentsAmount',
        width: 65,
    },
    {
        title: '出库日期',
        dataIndex: 'createTime',
        width: 140,
    },
]

export const searchStorageSummarySchema: FormSchema[] = [
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
        label: '仓库',
        field: 'warehouseId',
        component: 'ApiSelect',
        componentProps: {
            api: getWarehouseList,
            resultField: 'data',
            labelField: 'warehouseName',
            valueField: 'id',
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    }
]

export const storageSummaryStatisticsColumns: BasicColumn[] = [
    {
        title: '商品条码',
        dataIndex: 'productBarcode',
        width: 100,
    },
    {
        title: '仓库',
        dataIndex: 'warehouseName',
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
        width: 90,
    },
    {
        title: '单位',
        dataIndex: 'productUnit',
        width: 60,
    },
    {
        title: '入库数量',
        dataIndex: 'storageNumber',
        width: 65,
    },
    {
        title: '入库金额',
        dataIndex: 'storageAmount',
        width: 65,
    },
    {
        title: '入库日期',
        dataIndex: 'createTime',
        width: 140,
    },
]