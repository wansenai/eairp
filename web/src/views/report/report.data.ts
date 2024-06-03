import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {getWarehouseList} from "@/api/basic/warehouse";
import {getCategoryList} from "@/api/product/productCategory";
import {getMemberList} from "@/api/basic/member";
import {getSupplierList} from "@/api/basic/supplier";
import {getCustomerList} from "@/api/basic/customer";
import {getRelatedPerson} from "@/api/report/report";
import {getOperatorList} from "@/api/basic/operator";
import {useI18n} from "@/hooks/web/useI18n";

const { t } = useI18n();
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
        title: t('reports.productStock.table.stockFlow'),
        dataIndex: 'id',
        width: 80,
    },
    {
        title: t('reports.productStock.table.productBarcode'),
        dataIndex: 'productBarcode',
        width: 120,
    },
    {
        title: t('reports.productStock.table.warehouse'),
        dataIndex: 'warehouseName',
        width: 120,
    },
    {
        title: t('reports.productStock.table.productName'),
        dataIndex: 'productName',
        width: 350,
    },
    {
        title: t('reports.productStock.table.productCategory'),
        dataIndex: 'productCategoryName',
        width: 80,
    },
    {
        title: t('reports.productStock.table.standard'),
        dataIndex: 'productStandard',
        width: 120,
    },
    {
        title: t('reports.productStock.table.warehouseShelves'),
        dataIndex: 'warehouseShelves',
        width: 100,
    },
    {
        title: t('reports.productStock.table.unitPrice'),
        dataIndex: 'unitPrice',
        width: 70,
    },
    {
        title: t('reports.productStock.table.initStock'),
        dataIndex: 'initialStock',
        width: 70,
    },
    {
        title: t('reports.productStock.table.currentStock'),
        dataIndex: 'currentStock',
        width: 70,
    },
    {
        title: t('reports.productStock.table.stockAmount'),
        dataIndex: 'stockAmount',
        width: 90,
    },
]

export const searchProductStockSchema: FormSchema[] = [
    {
        label: t('reports.productStock.table.warehouse'),
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
        label: t('reports.productStock.header.productInfo'),
        field: 'productInfo',
        component: 'Input',
        colProps: { span: 5 },
    },
    {
        label: t('reports.productStock.table.productCategory'),
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
        label: t('reports.productStock.table.warehouseShelves'),
        field: 'warehouseShelves',
        component: 'Input',
        colProps: { span: 5 },
    },
]

export const stockFlowColumns: BasicColumn[] = [
    {
        title: t('reports.shipmentsDetail.header.receiptNumber'),
        dataIndex: 'receiptNumber',
        width: 180,
    },
    {
        title: t('reports.shipmentsDetail.table.type'),
        dataIndex: 'type',
        width: 80,
    },
    {
        title: t('reports.retail.table.barCode'),
        dataIndex: 'productBarcode',
        width: 120,
    },
    {
        title: t('reports.retail.table.name'),
        dataIndex: 'productName',
        width: 120,
    },
    {
        title: t('reports.retail.table.warehouse'),
        dataIndex: 'warehouseName',
        width: 120,
    },
    {
        title: t('reports.shipmentsDetail.table.quantity'),
        dataIndex: 'productNumber',
        width: 60,
    },
    {
        title: t('reports.storageSummary.header.receiptDate'),
        dataIndex: 'receiptDate',
        width: 150,
    }
]

export const searchStockFlowSchema: FormSchema[] = [
    {
        label: t('reports.shipmentsDetail.header.receiptNumber'),
        field: 'receiptNumber',
        component: 'Input',
        colProps: { span: 10 },
    },
    {
        field: '[startDate, endDate]',
        label: t('reports.retail.header.receiptDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('reports.retail.header.startDate'), t('reports.retail.header.endDate')],
        },
        colProps: { span: 10 },
    },
]

export const accountStatisticsColumns: BasicColumn[] = [
    {
        title: t('reports.account.table.accountFlow'),
        dataIndex: 'accountId',
        width: 80,
    },
    {
        title: t('reports.account.table.accountName'),
        dataIndex: 'accountName',
        width: 180,
    },
    {
        title: t('reports.account.table.accountNumber'),
        dataIndex: 'accountNumber',
        width: 120,
    },
    {
        title: t('reports.account.table.initialAmount'),
        dataIndex: 'initialAmount',
        width: 120,
    },
    {
        title: t('reports.account.table.thisMonthAmount'),
        dataIndex: 'thisMonthChangeAmount',
        width: 120,
    },
    {
        title: t('reports.account.table.currentAmount'),
        dataIndex: 'currentAmount',
        width: 120,
    },
]

export const searchAccountSchema: FormSchema[] = [
    {
        label: t('reports.account.header.account'),
        field: 'accountName',
        component: 'Input',
        colProps: { span: 10 },
    },
    {
        label: t('reports.account.header.accountNumber'),
        field: 'accountNumber',
        component: 'Input',
        colProps: { span: 10 },
    },
]

export const accountFlowColumns: BasicColumn[] = [
    {
        title: t('reports.shipmentsDetail.header.receiptNumber'),
        dataIndex: 'receiptNumber',
        width: 180,
    },
    {
        title: t('reports.shipmentsDetail.table.type'),
        dataIndex: 'subType',
        width: 80,
    },
    {
        title: t('reports.other.subType'),
        dataIndex: 'useType',
        width: 90,
    },
    {
        title: t('reports.storageDetail.table.name'),
        dataIndex: 'name',
        width: 120,
    },
    {
        title: t('reports.retail.table.amount'),
        dataIndex: 'amount',
        width: 110,
    },
    {
        title: t('reports.other.balance'),
        dataIndex: 'balance',
        width: 110,
    },
    {
        title: t('reports.retail.header.receiptDate'),
        dataIndex: 'receiptDate',
        width: 150,
    }
]

export const searchRetailSchema: FormSchema[] = [
    {
        label: t('reports.retail.header.productInfo'),
        field: 'productExtendInfo',
        component: 'Input',
        colProps: { span: 7 },
    },
    {
        field: '[startDate, endDate]',
        label: t('reports.retail.header.receiptDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('reports.retail.header.startDate'), t('reports.retail.header.endDate')],
        },
        colProps: { span: 7 },
    },
    {
        label: t('reports.retail.header.member'),
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
        title: t('reports.retail.table.barCode'),
        dataIndex: 'productBarcode',
        width: 100,
    },
    {
        title: t('reports.retail.table.warehouse'),
        dataIndex: 'warehouseName',
        width: 100,
    },
    {
        title: t('reports.retail.table.name'),
        dataIndex: 'productName',
        width: 120,
    },
    {
        title: t('reports.retail.table.standard'),
        dataIndex: 'productStandard',
        width: 120,
    },
    {
        title: t('reports.retail.table.model'),
        dataIndex: 'productModel',
        width: 90,
    },
    {
        title: t('reports.retail.table.extendInfo'),
        dataIndex: 'productExtendInfo',
        width: 120,
    },
    {
        title: t('reports.retail.table.unit'),
        dataIndex: 'productUnit',
        width: 80,
    },
    {
        title: t('reports.retail.table.quantity'),
        dataIndex: 'retailNumber',
        width: 70,
    },
    {
        title: t('reports.retail.table.amount'),
        dataIndex: 'retailAmount',
        width: 70,
    },
    {
        title: t('reports.retail.table.refundQuantity'),
        dataIndex: 'retailRefundNumber',
        width: 70,
    },
    {
        title: t('reports.retail.table.refundAmount'),
        dataIndex: 'retailRefundAmount',
        width: 70,
    },
    {
        title: t('reports.retail.table.actualAmount'),
        dataIndex: 'retailLastAmount',
        width: 70,
    }
]


export const searchPurchaseSchema: FormSchema[] = [
    {
        label: t('reports.purchase.header.productInfo'),
        field: 'productExtendInfo',
        component: 'Input',
        colProps: { span: 7 },
    },
    {
        field: '[startDate, endDate]',
        label: t('reports.purchase.header.receiptDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('reports.purchase.header.startDate'), t('reports.purchase.header.endDate')],
        },
        colProps: { span: 7 },
    },
    {
        label: t('reports.purchase.header.supplier'),
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
        title: t('reports.purchase.table.barCode'),
        dataIndex: 'productBarcode',
        width: 100,
    },
    {
        title: t('reports.purchase.table.warehouse'),
        dataIndex: 'warehouseName',
        width: 100,
    },
    {
        title: t('reports.purchase.table.name'),
        dataIndex: 'productName',
        width: 120,
    },
    {
        title: t('reports.purchase.table.standard'),
        dataIndex: 'productStandard',
        width: 120,
    },
    {
        title: t('reports.purchase.table.purchaseDate'),
        dataIndex: 'createTime',
        width: 110,
    },
    {
        title: t('reports.purchase.table.quantity'),
        dataIndex: 'purchaseNumber',
        width: 70,
    },
    {
        title: t('reports.purchase.table.amount'),
        dataIndex: 'purchaseAmount',
        width: 70,
    },
    {
        title: t('reports.purchase.table.refundQuantity'),
        dataIndex: 'purchaseRefundNumber',
        width: 70,
    },
    {
        title: t('reports.purchase.table.refundAmount'),
        dataIndex: 'purchaseRefundAmount',
        width: 70,
    },
    {
        title: t('reports.purchase.table.actualAmount'),
        dataIndex: 'purchaseLastAmount',
        width: 70,
    }
]

export const searchSalesSchema: FormSchema[] = [
    {
        label: t('reports.sales.header.productInfo'),
        field: 'productExtendInfo',
        component: 'Input',
        colProps: { span: 7 },
    },
    {
        field: '[startDate, endDate]',
        label: t('reports.sales.header.receiptDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('reports.sales.header.startDate'), t('reports.sales.header.endDate')],
        },
        colProps: { span: 7 },
    },
    {
        label: t('reports.sales.header.customer'),
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
        title: t('reports.sales.table.barCode'),
        dataIndex: 'productBarcode',
        width: 100,
    },
    {
        title: t('reports.sales.table.warehouse'),
        dataIndex: 'warehouseName',
        width: 100,
    },
    {
        title: t('reports.sales.table.name'),
        dataIndex: 'productName',
        width: 120,
    },
    {
        title: t('reports.sales.table.standard'),
        dataIndex: 'productStandard',
        width: 120,
    },
    {
        title: t('reports.sales.table.model'),
        dataIndex: 'productModel',
        width: 90,
    },
    {
        title: t('reports.sales.table.extendInfo'),
        dataIndex: 'productExtendInfo',
        width: 120,
    },
    {
        title: t('reports.sales.table.unit'),
        dataIndex: 'productUnit',
        width: 80,
    },
    {
        title: t('reports.sales.table.quantity'),
        dataIndex: 'salesNumber',
        width: 70,
    },
    {
        title: t('reports.sales.table.amount'),
        dataIndex: 'salesAmount',
        width: 70,
    },
    {
        title: t('reports.sales.table.refundQuantity'),
        dataIndex: 'salesRefundNumber',
        width: 70,
    },
    {
        title: t('reports.sales.table.refundAmount'),
        dataIndex: 'salesRefundAmount',
        width: 70,
    },
    {
        title: t('reports.sales.table.actualAmount'),
        dataIndex: 'salesLastAmount',
        width: 70,
    }
]

export const searchShipmentsDetailSchema: FormSchema[] = [
    {
        label: t('reports.shipmentsDetail.header.receiptNumber'),
        field: 'receiptNumber',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        field: '[startDate, endDate]',
        label: t('reports.shipmentsDetail.header.receiptDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('reports.shipmentsDetail.header.startDate'), t('reports.shipmentsDetail.header.endDate')],
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('reports.shipmentsDetail.header.productInfo'),
        field: 'productInfo',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('reports.shipmentsDetail.header.contact'),
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
        label: t('reports.shipmentsDetail.header.warehouse'),
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
        label: t('reports.shipmentsDetail.header.operator'),
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
        label: t('reports.shipmentsDetail.header.remark'),
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
        title: t('reports.shipmentsDetail.table.receiptNumber'),
        dataIndex: 'receiptNumber',
        width: 170,
    },
    {
        title: t('reports.shipmentsDetail.table.type'),
        dataIndex: 'type',
        width: 70,
    },
    {
        title: t('reports.shipmentsDetail.table.contact'),
        dataIndex: 'name',
        width: 70,
    },
    {
        title: t('reports.shipmentsDetail.table.barCode'),
        dataIndex: 'productBarcode',
        width: 100,
    },
    {
        title: t('reports.shipmentsDetail.table.warehouse'),
        dataIndex: 'warehouseName',
        width: 100,
    },
    {
        title: t('reports.shipmentsDetail.table.name'),
        dataIndex: 'productName',
        width: 120,
    },
    {
        title: t('reports.shipmentsDetail.table.standard'),
        dataIndex: 'productStandard',
        width: 120,
    },
    {
        title: t('reports.shipmentsDetail.table.model'),
        dataIndex: 'productModel',
        width: 90,
    },
    {
        title: t('reports.shipmentsDetail.table.unit'),
        dataIndex: 'productUnit',
        width: 60,
    },
    {
        title: t('reports.shipmentsDetail.table.quantity'),
        dataIndex: 'productNumber',
        width: 65,
    },
    {
        title: t('reports.shipmentsDetail.table.unitPrice'),
        dataIndex: 'unitPrice',
        width: 65,
    },
    {
        title: t('reports.shipmentsDetail.table.amount'),
        dataIndex: 'amount',
        width: 75,
    },
    {
        title: t('reports.shipmentsDetail.table.taxRate'),
        dataIndex: 'taxRate',
        width: 65,
    },
    {
        title: t('reports.shipmentsDetail.table.taxAmount'),
        dataIndex: 'taxAmount',
        width: 70,
    },
    {
        title: t('reports.shipmentsDetail.table.shipmentsDate'),
        dataIndex: 'createTime',
        width: 140,
    },
]

export const searchStorageDetailSchema: FormSchema[] = [
    {
        label: t('reports.storageDetail.header.receiptNumber'),
        field: 'receiptNumber',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        field: '[startDate, endDate]',
        label: t('reports.storageDetail.header.receiptDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('reports.storageDetail.header.startDate'), t('reports.storageDetail.header.endDate')],
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('reports.storageDetail.header.productInfo'),
        field: 'productInfo',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('reports.storageDetail.header.contact'),
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
        label: t('reports.storageDetail.header.warehouse'),
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
        label: t('reports.storageDetail.header.operator'),
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
        label: t('reports.storageDetail.header.remark'),
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
        title: t('reports.storageDetail.table.receiptNumber'),
        dataIndex: 'receiptNumber',
        width: 170,
    },
    {
        title: t('reports.storageDetail.table.type'),
        dataIndex: 'type',
        width: 70,
    },
    {
        title: t('reports.storageDetail.table.contact'),
        dataIndex: 'name',
        width: 70,
    },
    {
        title: t('reports.storageDetail.table.barCode'),
        dataIndex: 'productBarcode',
        width: 100,
    },
    {
        title: t('reports.storageDetail.table.warehouse'),
        dataIndex: 'warehouseName',
        width: 100,
    },
    {
        title: t('reports.storageDetail.table.name'),
        dataIndex: 'productName',
        width: 120,
    },
    {
        title: t('reports.storageDetail.table.standard'),
        dataIndex: 'productStandard',
        width: 120,
    },
    {
        title: t('reports.storageDetail.table.model'),
        dataIndex: 'productModel',
        width: 90,
    },
    {
        title: t('reports.storageDetail.table.unit'),
        dataIndex: 'productUnit',
        width: 80,
    },
    {
        title: t('reports.storageDetail.table.quantity'),
        dataIndex: 'productNumber',
        width: 65,
    },
    {
        title: t('reports.storageDetail.table.unitPrice'),
        dataIndex: 'unitPrice',
        width: 65,
    },
    {
        title: t('reports.storageDetail.table.amount'),
        dataIndex: 'amount',
        width: 65,
    },
    {
        title: t('reports.storageDetail.table.taxRate'),
        dataIndex: 'taxRate',
        width: 65,
    },
    {
        title: t('reports.storageDetail.table.taxAmount'),
        dataIndex: 'taxAmount',
        width: 65,
    },
    {
        title: t('reports.storageDetail.table.storageDate'),
        dataIndex: 'createTime',
        width: 140,
    },
]

export const searchShipmentsSummarySchema: FormSchema[] = [
    {
        label: t('reports.shipmentsSummary.header.productInfo'),
        field: 'productInfo',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        field: '[startDate, endDate]',
        label: t('reports.shipmentsSummary.header.receiptDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('reports.shipmentsSummary.header.startDate'), t('reports.shipmentsSummary.header.endDate')],
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('reports.shipmentsSummary.header.contact'),
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
        label: t('reports.shipmentsSummary.header.warehouse'),
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
        title: t('reports.shipmentsSummary.table.barCode'),
        dataIndex: 'productBarcode',
        width: 100,
    },
    {
        title: t('reports.shipmentsSummary.table.warehouse'),
        dataIndex: 'warehouseName',
        width: 100,
    },
    {
        title: t('reports.shipmentsSummary.table.name'),
        dataIndex: 'productName',
        width: 120,
    },
    {
        title: t('reports.shipmentsSummary.table.category'),
        dataIndex: 'productCategoryName',
        width: 100,
    },
    {
        title: t('reports.shipmentsSummary.table.standard'),
        dataIndex: 'productStandard',
        width: 120,
    },
    {
        title: t('reports.shipmentsSummary.table.model'),
        dataIndex: 'productModel',
        width: 90,
    },
    {
        title: t('reports.shipmentsSummary.table.unit'),
        dataIndex: 'productUnit',
        width: 60,
    },
    {
        title: t('reports.shipmentsSummary.table.quantity'),
        dataIndex: 'shipmentsNumber',
        width: 65,
    },
    {
        title: t('reports.shipmentsSummary.table.amount'),
        dataIndex: 'shipmentsAmount',
        width: 65,
    },
    {
        title: t('reports.shipmentsSummary.table.shipmentsDate'),
        dataIndex: 'createTime',
        width: 140,
    },
]

export const searchStorageSummarySchema: FormSchema[] = [
    {
        label: t('reports.storageSummary.header.productInfo'),
        field: 'productInfo',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        field: '[startDate, endDate]',
        label: t('reports.storageSummary.header.receiptDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('reports.storageSummary.header.startDate'), t('reports.storageSummary.header.endDate')],
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('reports.storageSummary.header.contact'),
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
        label: t('reports.storageSummary.header.warehouse'),
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
        title: t('reports.storageSummary.table.barCode'),
        dataIndex: 'productBarcode',
        width: 100,
    },
    {
        title: t('reports.storageSummary.table.warehouse'),
        dataIndex: 'warehouseName',
        width: 100,
    },
    {
        title: t('reports.storageSummary.table.name'),
        dataIndex: 'productName',
        width: 120,
    },
    {
        title: t('reports.storageSummary.table.category'),
        dataIndex: 'productCategoryName',
        width: 100,
    },
    {
        title: t('reports.storageSummary.table.standard'),
        dataIndex: 'productStandard',
        width: 120,
    },
    {
        title: t('reports.storageSummary.table.model'),
        dataIndex: 'productModel',
        width: 90,
    },
    {
        title: t('reports.storageSummary.table.unit'),
        dataIndex: 'productUnit',
        width: 60,
    },
    {
        title: t('reports.storageSummary.table.quantity'),
        dataIndex: 'storageNumber',
        width: 65,
    },
    {
        title: t('reports.storageSummary.table.amount'),
        dataIndex: 'storageAmount',
        width: 65,
    },
    {
        title: t('reports.storageSummary.table.storageDate'),
        dataIndex: 'createTime',
        width: 140,
    },
]

export const searchCustomerBillSchema: FormSchema[] = [
    {
        label: t('reports.customerBill.header.customer'),
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
    {
        field: '[startDate, endDate]',
        label: t('reports.customerBill.header.billDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('reports.customerBill.header.startDate'), t('reports.customerBill.header.endDate')],
        },
        colProps: {
            xl: 9,
            xxl: 9,
        },
    },
]

export const customerBillColumns: BasicColumn[] = [
    {
        title: t('reports.customerBill.table.arrearsDetail'),
        dataIndex: 'customerId',
        width: 60,
    },
    {
        title: t('reports.customerBill.table.customer'),
        dataIndex: 'customerName',
        width: 100,
    },
    {
        title: t('reports.customerBill.table.contacts'),
        dataIndex: 'contactName',
        width: 80,
    },
    {
        title: t('reports.customerBill.table.contactNumber'),
        dataIndex: 'contactPhone',
        width: 100,
    },
    {
        title: t('reports.customerBill.table.email'),
        dataIndex: 'email',
        width: 110,
    },
    {
        title: t('reports.customerBill.table.firstQuarterCollection'),
        dataIndex: 'firstQuarterReceivable',
        width: 80,
    },
    {
        title: t('reports.customerBill.table.secondQuarterCollection'),
        dataIndex: 'secondQuarterReceivable',
        width: 80,
    },
    {
        title: t('reports.customerBill.table.thirdQuarterCollection'),
        dataIndex: 'thirdQuarterReceivable',
        width: 80,
    },
    {
        title: t('reports.customerBill.table.fourthQuarterCollection'),
        dataIndex: 'fourthQuarterReceivable',
        width: 80,
    },
    {
        title: t('reports.customerBill.table.totalArrears'),
        dataIndex: 'totalQuarterArrears',
        width: 100,
    },
    {
        title: t('reports.customerBill.table.totalCollection'),
        dataIndex: 'totalQuarterReceivable',
        width: 100,
    },
    {
        title: t('reports.customerBill.table.receivableArrears'),
        dataIndex: 'remainingReceivableArrears',
        width: 100,
        helpMessage: t('reports.customerBill.table.helpMessage'),
    },
]


export const searchCustomerBillDetailSchema: FormSchema[] = [
    {
        label: t('reports.storageDetail.table.receiptNumber'),
        field: 'receiptNumber',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('reports.storageSummary.header.productInfo'),
        field: 'productInfo',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        field: '[startDate, endDate]',
        label: t('reports.customerBill.header.billDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('reports.customerBill.header.startDate'), t('reports.customerBill.header.endDate')],
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
]

export const customerBillDetailColumns: BasicColumn[] = [
    {
        title: t('reports.storageDetail.table.receiptNumber'),
        dataIndex: 'receiptNumber',
        width: 180,
    },
    {
        title: t('reports.customerBill.table.customer'),
        dataIndex: 'customerName',
        width: 120,
    },
    {
        title: t('reports.storageSummary.header.productInfo'),
        dataIndex: 'productInfo',
        width: 150,
    },
    {
        title: t('reports.storageSummary.header.receiptDate'),
        dataIndex: 'receiptDate',
        width: 130,
    },
    {
        title: t('reports.storageDetail.header.operator'),
        dataIndex: 'operator',
        width: 80,
    },
    {
        title: t('reports.other.thisReceiptArrears'),
        dataIndex: 'thisReceiptArrears',
        width: 65,
    },
    {
        title: t('reports.other.receivedArrears'),
        dataIndex: 'receivedArrears',
        width: 65,
    },
    {
        title: t('reports.other.receivableArrears'),
        dataIndex: 'receivableArrears',
        width: 65,
    },
]

export const searchSupplierBillSchema: FormSchema[] = [
    {
        label: t('reports.supplierBill.header.supplier'),
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
    {
        field: '[startDate, endDate]',
        label: t('reports.supplierBill.header.billDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('reports.supplierBill.header.startDate'), t('reports.supplierBill.header.endDate')],
        },
        colProps: {
            xl: 9,
            xxl: 9,
        },
    },
]

export const supplierBillColumns: BasicColumn[] = [
    {
        title: t('reports.supplierBill.table.arrearsDetail'),
        dataIndex: 'supplierId',
        width: 60,
    },
    {
        title: t('reports.supplierBill.table.supplier'),
        dataIndex: 'supplierName',
        width: 100,
    },
    {
        title: t('reports.supplierBill.table.contacts'),
        dataIndex: 'contactName',
        width: 80,
    },
    {
        title: t('reports.supplierBill.table.contactNumber'),
        dataIndex: 'contactPhone',
        width: 100,
    },
    {
        title: t('reports.supplierBill.table.email'),
        dataIndex: 'email',
        width: 110,
    },
    {
        title: t('reports.supplierBill.table.firstQuarterPayment'),
        dataIndex: 'firstQuarterPayment',
        width: 80,
    },
    {
        title: t('reports.supplierBill.table.secondQuarterPayment'),
        dataIndex: 'secondQuarterPayment',
        width: 80,
    },
    {
        title: t('reports.supplierBill.table.thirdQuarterPayment'),
        dataIndex: 'thirdQuarterPayment',
        width: 80,
    },
    {
        title: t('reports.supplierBill.table.fourthQuarterPayment'),
        dataIndex: 'fourthQuarterPayment',
        width: 80,
    },
    {
        title: t('reports.supplierBill.table.totalArrears'),
        dataIndex: 'totalArrears',
        width: 100,
    },
    {
        title: t('reports.supplierBill.table.totalPayment'),
        dataIndex: 'totalPayment',
        width: 100,
    },
    {
        title: t('reports.supplierBill.table.payableArrears'),
        dataIndex: 'remainingPaymentArrears',
        width: 100,
        helpMessage: t('reports.supplierBill.table.helpMessage'),
    },
]

export const searchSupplierBillDetailSchema: FormSchema[] = [
    {
        label: t('reports.storageDetail.table.receiptNumber'),
        field: 'receiptNumber',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('reports.storageSummary.header.productInfo'),
        field: 'productInfo',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        field: '[startDate, endDate]',
        label: t('reports.supplierBill.header.billDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('reports.supplierBill.header.startDate'), t('reports.supplierBill.header.endDate')],
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
]

export const supplierBillDetailColumns: BasicColumn[] = [
    {
        title: t('reports.storageDetail.table.receiptNumber'),
        dataIndex: 'receiptNumber',
        width: 180,
    },
    {
        title: t('reports.supplierBill.table.supplier'),
        dataIndex: 'supplierName',
        width: 140,
    },
    {
        title: t('reports.storageSummary.header.productInfo'),
        dataIndex: 'productInfo',
        width: 150,
    },
    {
        title: t('reports.storageSummary.header.receiptDate'),
        dataIndex: 'receiptDate',
        width: 130,
    },
    {
        title: t('reports.storageDetail.header.operator'),
        dataIndex: 'operator',
        width: 70,
    },
    {
        title: t('reports.other.thisReceiptArrears'),
        dataIndex: 'thisReceiptArrears',
        width: 65,
    },
    {
        title: t('reports.other.paidArrears'),
        dataIndex: 'prepaidArrears',
        width: 65,
    },
    {
        title: t('reports.supplierBill.table.payableArrears'),
        dataIndex: 'paymentArrears',
        width: 65,
    },
]