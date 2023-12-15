import {reactive, ref} from "vue";
import XEUtils from "xe-utils";
import {VxeGridInstance, VxeGridProps} from "vxe-table";
import {Dayjs} from "dayjs";

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
            title: '仓库',
            slots: { edit: 'warehouse_edit',default: 'warehouse_default' },
            editRender: {name: 'input', attrs: { placeholder: '请选择仓库' }}
        },
        {   field: 'barCode',
            width:160,
            title: '条码',
            slots: { edit: 'barCode_edit' },
            titlePrefix: { content: '输入条码商品信息自动带出！' },
            editRender: { name: 'input', attrs: { placeholder: '请输入条码并回车' } }
        },
        {
            field: 'productName',
            title: '名称',
            width:140,
        },
        { field: 'productStandard', title: '规格', width: 110,  },
        { field: 'stock', title: '库存',  width: 70},
        { field: 'productUnit', title: '单位',  width: 70},
        { field: 'productNumber', title: '数量', width:80,
            slots: { edit: 'product_number_edit' },
            editRender: { name: '$input', props: { type: 'number', min: 1, max: 9999 } }, },
        {
            field: 'unitPrice',
            title: '单价', width:90,
            titlePrefix: { content: '其他入/出库没有单价信息，若需要可自行修改' },
            formatter ({ cellValue }) {
                return cellValue ? `￥${XEUtils.commafy(XEUtils.toNumber(cellValue), { digits: 2 })}` : ''
            },
            slots: { edit: 'price_edit' },
            editRender: { name: '$input', props: { type: 'float', digits: 2, placeholder: '输入单价' } }
        },
        {
            field: 'amount',
            title: '金额', width:90,
            titlePrefix: { content: '其他入/出库没有金额信息，若需要可自行修改' },
            formatter ({ cellValue }) {
                return cellValue ? `￥${XEUtils.commafy(XEUtils.toNumber(cellValue), { digits: 2 })}` : ''
            },
            slots: { edit: 'amount_edit' },
            editRender: { name: '$input', props: { type: 'float', digits: 2, placeholder: '输入金额' } }
        },
        { field: 'remark', title: '备注', editRender: { name: 'input', attrs: { placeholder: '请输入备注' } }, width: 150},
    ],
    footerMethod ({ columns, data }) {
        return [
            columns.map((column, columnIndex) => {
                if (columnIndex === 0) {
                    return '总计'
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
            { required: true, message: '仓库不能为空' }
        ],
        barCode: [
            { required: true, message: '商品条码不能为空' }
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