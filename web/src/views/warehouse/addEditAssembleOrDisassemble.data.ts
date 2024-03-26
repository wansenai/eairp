import {reactive, ref} from "vue";
import XEUtils from "xe-utils";
import {VxeGridInstance, VxeGridProps} from "vxe-table";
import {Dayjs} from "dayjs";
import {useI18n} from "@/hooks/web/useI18n";

export const { t } = useI18n();

export interface RowVO {
    [key: string]: any,
    type: string,
    warehouseId: number | string,
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

interface AssembleFormState {
    id: number | string | undefined;
    warehouseId: number | string;
    receiptDate: string | undefined | Dayjs;
    receiptNumber: string |undefined;
    remark: string;
}

interface DisAssembleFormState {
    id: number | string | undefined;
    warehouseId: number | string;
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
        zoom: true,
        custom: true
    },
    columns: [
        { type: 'checkbox', field:'productId', title: 'ID', width: 80},
        {   field: 'type',
            width:100,
            title: t('warehouse.assemble.form.table.productType'),
        },
        {   field: 'warehouseId',
            width:120,
            title: t('warehouse.assemble.form.table.warehouse'),
            slots: { edit: 'warehouseId_edit',default: 'warehouseId_default' },
            editRender: {}
        },
        {   field: 'barCode',
            width:160,
            title: t('warehouse.assemble.form.table.barCode'),
            slots: { edit: 'barCode_edit' },
            titlePrefix: { content: '输入条码商品信息自动带出！' },
            editRender: { name: 'input', attrs: { placeholder: '请输入条码并回车' } }
        },
        {
            field: 'productName',
            title: t('warehouse.assemble.form.table.name'),
            width:140,
        },
        { field: 'productStandard', title: t('warehouse.assemble.form.table.standard'), width: 110,  },
        { field: 'stock', title: t('warehouse.assemble.form.table.stock'),  width: 70},
        { field: 'productUnit', title: t('warehouse.assemble.form.table.unit'),  width: 70},
        { field: 'productNumber', title: t('warehouse.assemble.form.table.quantity'), width:80,
            slots: { edit: 'product_number_edit' },
            editRender: { name: '$input', props: { type: 'number', min: 1, max: 9999 } }, },
        {
            field: 'unitPrice',
            title: t('warehouse.assemble.form.table.purchasePrice'), width:90,
            formatter ({ cellValue }) {
                return cellValue ? `￥${XEUtils.commafy(XEUtils.toNumber(cellValue), { digits: 2 })}` : ''
            },
            slots: { edit: 'price_edit' },
            editRender: { name: '$input', props: { type: 'float', digits: 2, placeholder: '输入单价' } }
        },
        {
            field: 'amount',
            title: t('warehouse.assemble.form.table.amount'), width:90,
            formatter ({ cellValue }) {
                return cellValue ? `￥${XEUtils.commafy(XEUtils.toNumber(cellValue), { digits: 2 })}` : ''
            },
            slots: { edit: 'amount_edit' },
            editRender: { name: '$input', props: { type: 'float', digits: 2, placeholder: '输入金额' } }
        },
        { field: 'remark', title: t('warehouse.assemble.form.table.remark'), editRender: { name: 'input', attrs: { placeholder: '请输入备注' } }, width: 150},
    ],
    footerMethod ({ columns, data }) {
        return [
            columns.map((column, columnIndex) => {
                if (columnIndex === 0) {
                    return t('warehouse.assemble.form.table.total')
                }
                if (['productNumber'].includes(column.field)) {
                    return sumNum(data, column.field)
                }
                if (['amount'].includes(column.field)) {
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
    editConfig: {
        trigger: 'click',
        mode: 'row',
        showStatus: true
    },
    editRules: {
        warehouseId: [
            { required: true, message: t('warehouse.assemble.form.noticeThree') }
        ],
        barCode: [
            { required: true, message: t('warehouse.assemble.form.noticeFour') }
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
const assembleFormState = reactive<AssembleFormState>({
    id: undefined,
    warehouseId: '',
    receiptDate: '',
    receiptNumber: '',
    remark: '',
});

const disAssembleFormState = reactive<DisAssembleFormState>({
    id: undefined,
    warehouseId: '',
    receiptDate: '',
    receiptNumber: '',
    remark: '',
});

export {
    xGrid,
    sumNum,
    tableData,
    gridOptions,
    assembleFormState,
    disAssembleFormState
}