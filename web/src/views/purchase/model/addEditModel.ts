import {reactive, ref} from "vue";
import XEUtils from "xe-utils";
import {VxeGridInstance, VxeGridProps} from "vxe-table";
import {Dayjs} from "dayjs";
import {useI18n} from "@/hooks/web/useI18n";

export const { t } = useI18n();

export interface RowVO {
    [key: string]: any,
    barCode: number | string,
    productName:string,
    productStandard: string,
    stock: number,
    productUnit: string,
    productNumber: number,
    unitPrice: number,
    amount: number,
    taxRate: number,
    taxAmount: number,
    taxTotalPrice: number,
    remark: string,
}
interface PurchaseOrderFormState {
    id: number | string | undefined;
    supplierId: string;
    receiptNumber: string;
    discountRate: number;
    discountAmount: number;
    discountLastAmount: number | string;
    deposit: number;
    remark: string;
    receiptDate: string | undefined | Dayjs;
    warehouseId: number | string;
    accountId: string | undefined;
    multipleAccountIds: number[] | undefined;
    multipleAccountAmounts: number[] | undefined;
}

interface PurchaseStorageFormState {
    id: number | string | undefined;
    supplierId: string;
    accountId: number | string | undefined;
    receiptNumber: string;
    receiptDate: string | undefined | Dayjs;
    otherReceipt: string;
    paymentRate: number;
    paymentAmount: number;
    paymentLastAmount: number | string;
    otherAmount: number;
    thisPaymentAmount: number | string;
    thisArrearsAmount: number | string;
    remark: string;
    status: number | undefined;
    warehouseId: number | string;
    multipleAccountIds: number[] | undefined;
    multipleAccountAmounts: number[] | undefined;
}

interface PurchaseRefundFormState {
    id: number | string | undefined;
    supplierId: string;
    accountId: number | string | undefined;
    receiptNumber: string;
    receiptDate: string | undefined | Dayjs;
    otherReceipt: string;
    refundOfferRate: number;
    refundOfferAmount: number;
    refundLastAmount: number | string;
    otherAmount: number;
    thisRefundAmount: number;
    thisArrearsAmount: number;
    remark: string;
    status: number | undefined;
    warehouseId: number | string;
    multipleAccountIds: number[] | undefined;
    multipleAccountAmounts: number[] | undefined;
}

const xGrid = ref<VxeGridInstance<RowVO>>()
const tableData = ref<RowVO[]>([])
const orderGridOptions = reactive<VxeGridProps<RowVO>>({
    border: true,
    showHeaderOverflow: true,
    showOverflow: true,
    showFooter: true,
    keepSource: true,
    id: 'full_edit',
    height: 400,
    rowConfig: {
        keyField: 'id',
        isHover: true
    },
    columnConfig: {
        resizable: true
    },
    printConfig: {
        columns: [
            { field: 'barCode' },
            { field: 'productName' },
            { field: 'productStandard' },
            { field: 'stockNumber' },
            { field: 'productUnit' },
            { field: 'productNumber' },
            { field: 'retailPrice' },
            { field: 'amount' },
            { field: 'remark' }
        ]
    },
    sortConfig: {
        trigger: 'cell',
        remote: true
    },
    filterConfig: {
        remote: true
    },
    pagerConfig: {
        enabled: true,
        pageSize: 10,
        pageSizes: [5, 10, 15, 20, 50, 100, 200, 500, 1000]
    },
    formConfig: {
        titleWidth: 100,
        titleAlign: 'right',
        items: [
        ]
    },
    toolbarConfig: {
        slots: {
            buttons: 'toolbar_buttons'
        },
        refresh: true, // 显示刷新按钮
        print: true, // 显示打印按钮
        zoom: true, // 显示全屏按钮
        custom: true // 显示自定义列按钮
    },
    columns: [
        { type: 'checkbox', field:'productId', title: 'ID', width: 80},
        {   field: 'barCode',
            width:160,
            title: t('purchase.order.form.table.barCode'),
            slots: { edit: 'barCode_edit' },
            titlePrefix: { content:  t('purchase.order.form.noticeSix') },
            editRender: { name: 'input', attrs: { placeholder: '请输入条码并回车' } }
        },
        {
            field: 'productName',
            title: t('purchase.order.form.table.name'),
            width:160,
        },
        { field: 'productStandard', title: t('purchase.order.form.table.standard'), width: 120,  },
        { field: 'stock', title: t('purchase.order.form.table.stock'),  width: 70},
        { field: 'productUnit', title: t('purchase.order.form.table.unit'),  width: 70},
        { field: 'productNumber', title: t('purchase.order.form.table.quantity'),  sortable: true, width:100,
            slots: { edit: 'product_number_edit' },
            editRender: { name: '$input', props: { type: 'number', min: 1, max: 9999 } },
        },
        {
            field: 'unitPrice',
            title: t('purchase.order.form.table.unitPrice'), width:105,
            formatter ({ cellValue }) {
                return cellValue ? `￥${XEUtils.commafy(XEUtils.toNumber(cellValue), { digits: 2 })}` : ''
            },
            slots: { edit: 'price_edit' },
            editRender: { name: '$input', props: { type: 'float', digits: 2, placeholder: '输入单价' } }
        },
        {
            field: 'amount',
            title: t('purchase.order.form.table.amount'), width:105,
            formatter ({ cellValue }) {
                return cellValue ? `￥${XEUtils.commafy(XEUtils.toNumber(cellValue), { digits: 2 })}` : ''
            },
            slots: { edit: 'amount_edit' },
            editRender: { name: '$input', props: { type: 'float', digits: 2, placeholder: '输入金额' } }
        },
        { field: 'taxRate', title: t('purchase.order.form.table.taxRate'), width: 105,
            slots: { edit: 'tax_rate_edit' },
            editRender: { name: '$input', attrs: { type: 'float', digits: 2, placeholder: '请输入税率' } }
        },
        { field: 'taxAmount', title: t('purchase.order.form.table.taxAmount'),  width: 105,
            editRender:{attrs: {type: 'float', digits: 2}},
            slots: { edit: 'tax_amount_edit' },
        },
        { field: 'taxTotalPrice', title: t('purchase.order.form.table.totalIncludingTax'),  width: 105,
            slots: { edit: 'tax_total_price_edit' },
            editRender: { name: '$input', attrs: {type: 'float', digits: 2, placeholder: '请输入价税合计' } }
        },
        { field: 'remark', title: t('purchase.order.form.table.remark'), editRender: { name: 'input', attrs: { placeholder: '请输入备注' } } },

    ],
    footerMethod ({ columns, data }) {
        return [
            columns.map((column, columnIndex) => {
                if (columnIndex === 0) {
                    return t('purchase.order.form.table.total')
                }
                if (['amount', 'rate'].includes(column.field)) {
                    return `￥${XEUtils.commafy(XEUtils.toNumber(sumNum(data, column.field)), { digits: 2 })}`
                }
                if (['productNumber', 'rate'].includes(column.field)) {
                    return sumNum(data, column.field)
                }
                if (['taxAmount', 'rate'].includes(column.field)) {
                    return `￥${XEUtils.commafy(XEUtils.toNumber(sumNum(data, column.field)), { digits: 2 })}`
                }

                if (['taxTotalPrice', 'rate'].includes(column.field)) {
                    getTaxTotalPrice.value = `￥${XEUtils.commafy(XEUtils.toNumber(sumNum(data, column.field)), { digits: 2 })}`
                    return `￥${XEUtils.commafy(XEUtils.toNumber(sumNum(data, column.field)), { digits: 2 })}`
                }
                return ''
            })
        ]
    },
    checkboxConfig: {
        labelField: 'id',
        reserve: true,
        highlight: true,
        range: true
    },
    editRules: {
        barCode: [
            { required: true, message: t('purchase.order.form.noticeFive') }
        ]
    },
    editConfig: {
        trigger: 'click',
        mode: 'row',
        showStatus: true
    }
})

const gridOptions = reactive<VxeGridProps<RowVO>>({
    border: true,
    showHeaderOverflow: true,
    showOverflow: true,
    showFooter: true,
    keepSource: true,
    id: 'full_edit',
    height: 400,
    rowConfig: {
        keyField: 'id',
        isHover: true
    },
    columnConfig: {
        resizable: true
    },
    printConfig: {
        columns: [
            { field: 'barCode' },
            { field: 'productName' },
            { field: 'productStandard' },
            { field: 'stockNumber' },
            { field: 'productUnit' },
            { field: 'productNumber' },
            { field: 'retailPrice' },
            { field: 'amount' },
            { field: 'remark' }
        ]
    },
    sortConfig: {
        trigger: 'cell',
        remote: true
    },
    filterConfig: {
        remote: true
    },
    pagerConfig: {
        enabled: true,
        pageSize: 10,
        pageSizes: [5, 10, 15, 20, 50, 100, 200, 500, 1000]
    },
    formConfig: {
        titleWidth: 100,
        titleAlign: 'right',
        items: [
        ]
    },
    toolbarConfig: {
        slots: {
            buttons: 'toolbar_buttons'
        },
        refresh: true, // 显示刷新按钮
        print: true, // 显示打印按钮
        zoom: true, // 显示全屏按钮
        custom: true // 显示自定义列按钮
    },
    columns: [
        { type: 'checkbox', field:'productId', title: 'ID', width: 80},
        {
            field: 'warehouseId',
            title: t('purchase.storage.form.table.barCode'),
            width: 130,
            slots: { edit: 'warehouse_edit', default: 'warehouse_default'},
            editRender: { name: 'input', attrs: { placeholder: t('purchase.storage.form.table.inputWarehouse') } }
        },
        {   field: 'barCode',
            width:160,
            title: t('purchase.storage.form.table.barCode'),
            slots: { edit: 'barCode_edit' },
            titlePrefix: { content: t('purchase.storage.form.noticeSix') },
            editRender: { name: 'input', attrs: { placeholder: '请输入条码并回车' } }
        },
        {
            field: 'productName',
            title: t('purchase.storage.form.table.name'),
            width:160,
        },
        { field: 'productStandard', title: t('purchase.storage.form.table.standard'), width: 120,  },
        { field: 'stock', title: t('purchase.storage.form.table.stock'),  width: 70},
        { field: 'productUnit', title: t('purchase.storage.form.table.unit'),  width: 70},
        { field: 'productNumber', title: t('purchase.storage.form.table.quantity'),  sortable: true, width:100,
            slots: { edit: 'product_number_edit' },
            editRender: { name: '$input', props: { type: 'number', min: 1, max: 9999 } },
        },
        {
            field: 'unitPrice',
            title: t('purchase.storage.form.table.unitPrice'), width:105,
            formatter ({ cellValue }) {
                return cellValue ? `￥${XEUtils.commafy(XEUtils.toNumber(cellValue), { digits: 2 })}` : ''
            },
            slots: { edit: 'price_edit' },
            editRender: { name: '$input', props: { type: 'float', digits: 2, placeholder: '输入单价' } }
        },
        {
            field: 'amount',
            title: t('purchase.storage.form.table.amount'), width:105,
            formatter ({ cellValue }) {
                return cellValue ? `￥${XEUtils.commafy(XEUtils.toNumber(cellValue), { digits: 2 })}` : ''
            },
            slots: { edit: 'amount_edit' },
            editRender: { name: '$input', props: { type: 'float', digits: 2, placeholder: '输入金额' } }
        },
        { field: 'taxRate', title: t('purchase.storage.form.table.taxRate'), width: 105,
            slots: { edit: 'tax_rate_edit' },
            editRender: { name: '$input', attrs: { type: 'float', digits: 2, placeholder: '请输入税率' } }
        },
        { field: 'taxAmount', title: t('purchase.storage.form.table.taxAmount'),  width: 105,
            editRender:{attrs: {type: 'float', digits: 2}},
            slots: { edit: 'tax_amount_edit' },
        },
        { field: 'taxTotalPrice', title: t('purchase.storage.form.table.totalIncludingTax'),  width: 105,
            slots: { edit: 'tax_total_price_edit' },
            editRender: { name: '$input', attrs: {type: 'float', digits: 2, placeholder: '请输入价税合计' } }
        },
        { field: 'remark', title: t('purchase.storage.form.table.remark'), editRender: { name: 'input', attrs: { placeholder: '请输入备注' } } },

    ],
    footerMethod ({ columns, data }) {
        return [
            columns.map((column, columnIndex) => {
                if (columnIndex === 0) {
                    return t('purchase.storage.form.table.total')
                }
                if (['amount', 'rate'].includes(column.field)) {
                    return `￥${XEUtils.commafy(XEUtils.toNumber(sumNum(data, column.field)), { digits: 2 })}`
                }
                if (['productNumber', 'rate'].includes(column.field)) {
                    return sumNum(data, column.field)
                }
                if (['taxAmount', 'rate'].includes(column.field)) {
                    return `￥${XEUtils.commafy(XEUtils.toNumber(sumNum(data, column.field)), { digits: 2 })}`
                }

                if (['taxTotalPrice', 'rate'].includes(column.field)) {
                    getTaxTotalPrice.value = `￥${XEUtils.commafy(XEUtils.toNumber(sumNum(data, column.field)), { digits: 2 })}`
                    return `￥${XEUtils.commafy(XEUtils.toNumber(sumNum(data, column.field)), { digits: 2 })}`
                }
                return ''
            })
        ]
    },
    checkboxConfig: {
        labelField: 'id',
        reserve: true,
        highlight: true,
        range: true
    },
    editRules: {
        warehouseId: [
            { required: true, message: t('purchase.storage.form.table.inputWarehouse') }
        ],
        barCode: [
            { required: true, message: t('purchase.storage.form.table.inputBarCode') }
        ]
    },
    editConfig: {
        trigger: 'click',
        mode: 'row',
        showStatus: true
    }
})

const sumNum = (list: RowVO[], field: string) => {
    let count = 0
    list.forEach(item => {
        count += Number(item[field])
    })
    return count
}

const getTaxTotalPrice = ref('');

const purchaseOrderFormState = reactive<PurchaseOrderFormState>({
    id: undefined,
    supplierId: '',
    receiptNumber: '',
    remark: '',
    discountRate: 0,
    discountAmount: 0,
    discountLastAmount: 0,
    deposit: 0,
    accountId: undefined,
    receiptDate: '',
    warehouseId: '',
    multipleAccountIds: undefined,
    multipleAccountAmounts: undefined,
});

const purchaseStorageFormState = reactive<PurchaseStorageFormState>({
    id: undefined,
    supplierId: '',
    receiptNumber: '',
    otherReceipt: '',
    remark: '',
    paymentRate: 0,
    paymentAmount: 0,
    paymentLastAmount: 0,
    otherAmount: 0,
    thisPaymentAmount: 0,
    thisArrearsAmount: 0,
    status: undefined,
    accountId: undefined,
    receiptDate: '',
    warehouseId: '',
    multipleAccountIds: undefined,
    multipleAccountAmounts: undefined,
});

const purchaseRefundFormState = reactive<PurchaseRefundFormState>({
    id: undefined,
    supplierId: '',
    receiptNumber: '',
    otherReceipt: '',
    remark: '',
    refundOfferRate: 0,
    refundOfferAmount: 0,
    refundLastAmount: 0,
    otherAmount: 0,
    thisRefundAmount: 0,
    thisArrearsAmount: 0,
    status: undefined,
    accountId: undefined,
    receiptDate: '',
    warehouseId: '',
    multipleAccountIds: undefined,
    multipleAccountAmounts: undefined,
});

export {
    xGrid,
    sumNum,
    tableData,
    orderGridOptions,
    gridOptions,
    purchaseOrderFormState,
    purchaseStorageFormState,
    purchaseRefundFormState,
    getTaxTotalPrice,
}