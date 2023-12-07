import {reactive, ref} from "vue";
import XEUtils from "xe-utils";
import {VxeGridInstance, VxeGridProps} from "vxe-table";
import {Dayjs} from "dayjs";

export interface RowVO {
    [key: string]: any,
    warehouseId: number | string;
    otherWarehouseId: number | string;
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

interface AllotShipmentsFormState {
    id: number | string | undefined;
    warehouseId: number | string | undefined;
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
    proxyConfig: {
        sort: false,
        filter: false,
        form: true,
    },
    columns: [
        { type: 'checkbox', field:'productId', title: 'ID', width: 80},
        {   field: 'warehouseId',
            width:150,
            title: '仓库',
            slots: { edit: 'warehouseId_edit',default: 'warehouseId_default' },
            editRender: {}
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
        {   field: 'otherWarehouseId',
            width:150,
            title: '调入仓库',
            slots: { edit: 'otherWarehouseId_edit',default: 'otherWarehouseId_default' },
            editRender: {}
        },
        { field: 'productUnit', title: '单位',  width: 70},
        { field: 'productNumber', title: '数量', width:80,
            slots: { edit: 'product_number_edit' },
            editRender: { name: '$input', props: { type: 'number', min: 1, max: 9999 } }, },
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
        otherWarehouseId: [
            { required: true, message: '调入方仓库不能为空' }
        ]
    },
})

const sumNum = (list: RowVO[], field: string) => {
    let count = 0
    list.forEach(item => {
        count += Number(item[field])
    })
    return count
}
const allotShipmentsFormState = reactive<AllotShipmentsFormState>({
    id: undefined,
    receiptDate: '',
    receiptNumber: '',
    warehouseId: '',
    remark: '',
});

export {
    xGrid,
    sumNum,
    tableData,
    gridOptions,
    allotShipmentsFormState
}