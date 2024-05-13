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

export interface RowVO {
    [key: string]: any,
    warehouseId: number | string;
    warehouseName: string | undefined,
    barCode: string | number,
    productId: number |string,
    productName: string,
    productModel: string,
    productUnit: string,
    productStandard: string,
    stock: number,
    productNumber: number,
    unitPrice: number,
    amount: number,
    remark: string,
}

interface OtherStorageFormState {
    id: number | string | undefined;
    warehouseId: number | string;
    supplierId: number | string | undefined;
    receiptDate: string | undefined | Dayjs;
    receiptNumber: string |undefined;
    remark: string;
}

interface OtherShipmentFormState {
    id: number | string | undefined;
    warehouseId: number | string;
    customerId: number | string | undefined;
    receiptDate: string | undefined | Dayjs;
    receiptNumber: string |undefined;
    remark: string;
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
    sortConfig: {
        trigger: 'cell',
        remote: true
    },
    filterConfig: {
        remote: true
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
        export: true,
        zoom: true,
        custom: true
    },
    columns: [
        { type: 'checkbox', field:'productId', title: 'ID', width: 80},
        {   field: 'warehouseId',
            width:120,
            title: t('warehouse.otherStorage.form.table.warehouse'),
            slots: { edit: 'warehouse_edit',default: 'warehouse_default' },
            editRender: {name: 'input', attrs: { placeholder: '请选择仓库' }}
        },
        {   field: 'barCode',
            width:160,
            title: t('warehouse.otherStorage.form.table.barCode'),
            slots: { edit: 'barCode_edit' },
            titlePrefix: { content: '输入条码商品信息自动带出！' },
            editRender: { name: 'input', attrs: { placeholder: '请输入条码并回车' } }
        },
        {
            field: 'productName',
            title: t('warehouse.otherStorage.form.table.name'),
            width:140,
        },
        { field: 'productStandard', title: t('warehouse.otherStorage.form.table.standard'), width: 110,  },
        { field: 'stock', title: t('warehouse.otherStorage.form.table.stock'),  width: 70},
        { field: 'productUnit', title: t('warehouse.otherStorage.form.table.unit'),  width: 70},
        { field: 'productNumber', title: t('warehouse.otherStorage.form.table.quantity'), width:80,
            slots: { edit: 'product_number_edit' },
            editRender: { name: '$input', props: { type: 'number', min: 1, max: 9999 } }, },
        {
            field: 'unitPrice',
            title: t('warehouse.otherStorage.form.table.unitPrice'),
            width:90,
            titlePrefix: { content: t('warehouse.otherStorage.form.noticeFive') },
            formatter ({ cellValue }) {
                return cellValue ? amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(cellValue), { digits: 2 })}` : ''
            },
            slots: { edit: 'price_edit' },
            editRender: { name: '$input', props: { type: 'float', digits: 2, placeholder: '输入单价' } }
        },
        {
            field: 'amount',
            title: t('warehouse.otherStorage.form.table.amount'),
            width:90,
            titlePrefix: { content: t('warehouse.otherStorage.form.noticeFive') },
            formatter ({ cellValue }) {
                return cellValue ? amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(cellValue), { digits: 2 })}` : ''
            },
            slots: { edit: 'amount_edit' },
            editRender: { name: 'input', props: { type: 'float', digits: 2 }, }
        },
        { field: 'remark', title: t('warehouse.otherStorage.view.remark'), editRender: { name: 'input', attrs: { placeholder: t('warehouse.otherStorage.form.table.inputRemark') } }, width: 150},
    ],
    footerMethod ({ columns, data }) {
        return [
            columns.map((column, columnIndex) => {
                if (columnIndex === 0) {
                    return t('warehouse.otherStorage.form.table.total')
                }
                if (['productNumber'].includes(column.field)) {
                    return sumNum(data, column.field)
                }
                if (['amount'].includes(column.field)) {
                    return amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(sumNum(data, column.field)), { digits: 2 })}`
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
    editConfig: {
        trigger: 'click',
        mode: 'row',
        showStatus: true
    },
    editRules: {
        warehouseId: [
            { required: true, message: t('warehouse.otherStorage.form.noticeThree') }
        ],
        barCode: [
            { required: true, message: t('warehouse.otherStorage.form.noticeFour') }
        ],
    },
})

const sumNum = (list: RowVO[], field: string) => {
    let count = 0
    list.forEach(item => {
        count += Number(item[field])
    })
    return count
}
const otherStorageFormState = reactive<OtherStorageFormState>({
    id: undefined,
    warehouseId: '',
    supplierId: '',
    receiptDate: '',
    receiptNumber: '',
    remark: '',
});

const otherShipmentFormState = reactive<OtherShipmentFormState>({
    id: undefined,
    warehouseId: '',
    customerId: '',
    receiptDate: '',
    receiptNumber: '',
    remark: '',
});

export {
    xGrid,
    sumNum,
    tableData,
    gridOptions,
    otherStorageFormState,
    otherShipmentFormState
}