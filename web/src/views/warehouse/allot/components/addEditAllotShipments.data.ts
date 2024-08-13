import {reactive, ref} from "vue";
import XEUtils from "xe-utils";
import {VxeGridInstance, VxeGridProps} from "vxe-table";
import {Dayjs} from "dayjs";
import {useI18n} from "@/hooks/web/useI18n";

export const { t } = useI18n();

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
    salePrice: number,
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
        zoom: true,
        custom: true
    },
    columns: [
        { type: 'checkbox', field:'productId', title: 'ID', width: 80},
        {   field: 'warehouseId',
            width:150,
            title: t('warehouse.allotShipments.form.table.outWarehouse'),
            slots: { edit: 'warehouse_edit',default: 'warehouse_default' },
            editRender: { name: 'input', attrs: { placeholder: t('warehouse.allotShipments.form.table.outWarehouse') } }
        },
        {   field: 'barCode',
            width:160,
            title: t('warehouse.allotShipments.form.table.barCode'),
            slots: { edit: 'barCode_edit' },
            titlePrefix: { content: t('warehouse.allotShipments.form.noticeSex') },
            editRender: { name: 'input', attrs: { placeholder: '请输入条码并回车' } }
        },
        {
            field: 'productName',
            title:  t('warehouse.allotShipments.form.table.name'),
            width:140,
        },
        // { field: 'productStandard', title: t('warehouse.allotShipments.form.table.standard'), width: 110,  },

        { field: 'stock', title: t('warehouse.allotShipments.form.table.stock'),  width: 70},
        {   field: 'otherWarehouseId',
            width:150,
            title: t('warehouse.allotShipments.form.table.inWarehouse'),
            slots: { edit: 'otherWarehouse_edit',default: 'otherWarehouse_default' },
            editRender: {}
        },
        { field: 'salePrice', title: t('warehouse.allotShipments.form.table.salePrice'), width: 150,
            slots: { edit: 'sale_price_edit',},
            editRender: { name: '$input', props: { type: 'number'} } },
        { field: 'productNumber', title: t('warehouse.allotShipments.form.table.quantity'), width:100,
            slots: { edit: 'product_number_edit' },
            editRender: { name: '$input', props: { type: 'number', min: 1, max: 9999 } }, },
        { field: 'productUnit', title: t('warehouse.allotShipments.form.table.unit'),  width: 70},
        { field: 'remark', title: t('warehouse.allotShipments.form.table.remark'), editRender: { name: 'input', attrs: { placeholder: t('warehouse.allotShipments.form.inputRemark') } }, width: 150},
    ],
    footerMethod ({ columns, data }) {
        return [
            columns.map((column, columnIndex) => {
                if (columnIndex === 0) {
                    return t('warehouse.allotShipments.form.total')
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
            { required: true, message: t('warehouse.allotShipments.form.noticeThree') }
        ],
        barCode: [
            { required: true, message: t('warehouse.allotShipments.form.noticeFour') }
        ],
        otherWarehouseId: [
            { required: true, message: t('warehouse.allotShipments.form.noticeFive') }
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