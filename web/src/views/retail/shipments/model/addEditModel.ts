import {reactive, ref} from "vue";
import XEUtils from "xe-utils";
import {VxeGridInstance, VxeGridProps} from "vxe-table";
import {Dayjs} from "dayjs";
import {useI18n} from "@/hooks/web/useI18n";
import {useLocaleStore} from "@/store/modules/locale";

export const { t } = useI18n();

const amountSymbol = ref<string>('')
const localeStore = useLocaleStore().getLocale;
if(localeStore === 'zh_CN') {
    amountSymbol.value = '￥'
} else if (localeStore === 'en') {
    amountSymbol.value = '$'
}

interface FormState {
    id: number | string | undefined;
    warehouseId: number | string;
    memberId: string;
    receiptNumber: string;
    paymentType: string;
    remark: string;
    receiptAmount: number;
    paymentAmount: number;
    scanBarCode: string;
    otherReceipt: string;
    collectAmount: number;
    backAmount: number;
    accountId: string;
    receiptDate: string | undefined | Dayjs;
}


export interface RowVO {
    [key: string]: any,
    warehouseId: number | string,
    barCode: number | string,
    productName:string,
    productStandard: string,
    stock: number,
    productUnit: string,
    productNumber: number,
    retailPrice: number,
    amount: number,
}

const xGrid = ref<VxeGridInstance<RowVO>>()
const tableData = ref<RowVO[]>([])
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
            { field: 'warehouseId' },
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
        export: true, // 显示导出按钮
        print: true, // 显示打印按钮
        zoom: true, // 显示全屏按钮
        custom: true // 显示自定义列按钮
    },
    columns: [
        { type: 'checkbox', field:'productId', title: 'ID', width: 80},
        {
            field: 'warehouseId',
            title: t('retail.shipments.form.table.warehouse'),
            width: 130,
            slots: { edit: 'warehouse_edit' },
            editRender: { name: '$select', options: [], props: { placeholder: t('retail.shipments.form.table.inputWarehouse') } }
        },
        {   field: 'barCode',
            width:160,
            title: t('retail.shipments.form.table.barCode'),
            slots: { edit: 'barCode_edit' },
            titlePrefix: { content: t('retail.shipments.form.noticeThree') },
            editRender: { name: '$select', options: []}
        },
        {
            field: 'productName',
            title: t('retail.shipments.form.table.name'),
            width:160,
        },
        { field: 'productStandard', title: t('retail.shipments.form.table.standard'), width: 120,  },
        { field: 'stock', title: t('retail.shipments.form.table.stock'),  width: 70},
        { field: 'productUnit', title: t('retail.shipments.form.table.unit'),  width: 70},
        { field: 'productNumber', title: t('retail.shipments.form.table.quantity'),  sortable: true, width:100,
            slots: { edit: 'product_number_edit' },
            editRender: { name: '$input', props: { type: 'number', min: 1, max: 9999 } }, },
        {
            field: 'retailPrice',
            title: t('retail.shipments.form.table.unitPrice'), width:105,
            formatter ({ cellValue }) {
                return cellValue ? amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(cellValue), { digits: 2 })}` : ''
            },
            editRender: { name: '$input', props: { type: 'float', digits: 2, placeholder: '输入单价' } }
        },
        {
            field: 'amount',
            title: t('retail.shipments.form.table.amount'), width:105,
            formatter ({ cellValue }) {
                return cellValue ? amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(cellValue), { digits: 2 })}` : ''
            },
            slots: { edit: 'amount_edit' },
            editRender: { name: '$input', props: { type: 'float', digits: 2, placeholder: '输入金额' } }
        },
        // { field: 'remark', title: '备注', editRender: { name: 'input', attrs: { placeholder: '请输入备注' } } },

    ],
    footerMethod ({ columns, data }) {
        return [
            columns.map((column, columnIndex) => {
                if (columnIndex === 0) {
                    return t('retail.shipments.form.table.total')
                }
                if (['amount', 'rate'].includes(column.field)) {
                    // 设置单价 = 金额 / 数量 设置保留两位小数
                    data.forEach(item => {
                        const price = item.amount / item.productNumber
                        item.retailPrice = XEUtils.toFixed(price, 2)
                    })
                    receiptAmount.value = amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(sumNum(data, column.field)), { digits: 2 })}`
                    collectAmount.value = amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(sumNum(data, column.field)), { digits: 2 })}`
                    paymentAmount.value = amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(sumNum(data, column.field)), { digits: 2 })}`
                    return amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(sumNum(data, column.field)), { digits: 2 })}`
                }
                if (['productNumber', 'rate'].includes(column.field)) {
                    // 设置单价 = 金额 / 数量
                    data.forEach(item => {
                        const price = item.amount / item.productNumber
                        item.retailPrice = XEUtils.toFixed(price, 2)
                    })
                    return sumNum(data, column.field)
                }
                if (['productUnit', 'rate'].includes(column.field)) {
                    // 获取单价和数量进相乘计算赋值给金额 保留两位小数
                    data.forEach(item => {
                        const amount = item.productNumber * item.retailPrice
                        item.amount = XEUtils.toFixed(amount, 2)
                    })
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
            { required: true, message: t('retail.shipments.form.noticeOne') }
        ],
        barCode: [
            { required: true, message: t('sales.shipments.form.table.inputBarCode') }
        ]
    },
    editConfig: {
        trigger: 'click',
        mode: 'row',
        showStatus: true
    }
})
const receiptAmount = ref<string>('');
const collectAmount = ref<string>('');

const paymentAmount = ref<string>('');
const sumNum = (list: RowVO[], field: string) => {
    let count = 0
    list.forEach(item => {
        count += Number(item[field])
    })
    return count
}

const formState = reactive<FormState>({
    id: undefined,
    warehouseId: '',
    memberId: '',
    receiptNumber: '',
    paymentType: '',
    remark: '',
    receiptAmount: 0,
    scanBarCode: '',
    collectAmount: 0,
    paymentAmount: 0,
    backAmount: 0,
    accountId: '',
    receiptDate: '',
    otherReceipt: '',
});

export {
    formState,
    gridOptions,
    xGrid,
    receiptAmount,
    collectAmount,
    paymentAmount,
    tableData
}