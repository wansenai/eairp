import {reactive, ref} from "vue";
import XEUtils from "xe-utils";
import {VxeGridInstance, VxeGridProps} from "vxe-table";
import {Dayjs} from "dayjs";

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

interface SaleShipmentsFormState {
    id: number | string | undefined;
    customerId: string;
    accountId: number | string | undefined;
    receiptNumber: string;
    receiptDate: string | undefined | Dayjs;
    otherReceipt: string;
    collectOfferRate: number;
    collectOfferAmount: number;
    collectOfferLastAmount: number | string;
    otherAmount: number;
    thisCollectAmount: number;
    thisArrearsAmount: number;
    remark: string;
    status: number | undefined;
    operatorIds: number[] | undefined;
    warehouseId: number | string;
    multipleAccountIds: number[] | undefined;
    multipleAccountAmounts: number[] | undefined;
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
    proxyConfig: {
        seq: true, // 启用动态序号代理，每一页的序号会根据当前页数变化
        sort: false, // 启用排序代理，当点击排序时会自动触发 query 行为
        filter: false, // 启用筛选代理，当点击筛选时会自动触发 query 行为
        form: true, // 启用表单代理，当点击表单提交按钮时会自动触发 reload 行为
        props: {
            // 对应响应结果 Promise<{ result: [], page: { total: 100 } }>
            result: 'result', // 配置响应结果列表字段
            total: 'page.total' // 配置响应结果总页数字段
        },
    },
    columns: [
        { type: 'checkbox', field:'productId', title: 'ID', width: 80},
        {   field: 'barCode',
            width:160,
            title: '条码',
            slots: { edit: 'barCode_edit' },
            sortable: true,
            titlePrefix: { content: '输入条码商品信息自动带出！' },
            editRender: { name: 'input', attrs: { placeholder: '请输入条码并回车' } }
        },
        {
            field: 'productName',
            title: '名称',
            width:160,
        },
        { field: 'productStandard', title: '规格', width: 120,  },
        { field: 'stock', title: '库存',  width: 70},
        { field: 'productUnit', title: '单位',  width: 70},
        { field: 'productNumber', title: '数量',  sortable: true, width:100,
            slots: { edit: 'product_number_edit' },
            editRender: { name: '$input', props: { type: 'number', min: 1, max: 9999 } },
        },
        {
            field: 'unitPrice',
            title: '单价', width:105,
            formatter ({ cellValue }) {
                return cellValue ? `￥${XEUtils.commafy(XEUtils.toNumber(cellValue), { digits: 2 })}` : ''
            },
            slots: { edit: 'price_edit' },
            editRender: { name: '$input', props: { type: 'float', digits: 2, placeholder: '输入单价' } }
        },
        {
            field: 'amount',
            title: '金额', width:105,
            formatter ({ cellValue }) {
                return cellValue ? `￥${XEUtils.commafy(XEUtils.toNumber(cellValue), { digits: 2 })}` : ''
            },
            slots: { edit: 'amount_edit' },
            editRender: { name: '$input', props: { type: 'float', digits: 2, placeholder: '输入金额' } }
        },
        { field: 'taxRate', title: '税率', width: 105,
            slots: { edit: 'tax_rate_edit' },
            editRender: { name: '$input', attrs: { type: 'float', digits: 2, placeholder: '请输入税率' } }
        },
        { field: 'taxAmount', title: '税额',  width: 105,
            editRender:{attrs: {type: 'float', digits: 2}},
            slots: { edit: 'tax_amount_edit' },
        },
        { field: 'taxTotalPrice', title: '价税合计',  width: 105,
            slots: { edit: 'tax_total_price_edit' },
            editRender: { name: '$input', attrs: {type: 'float', digits: 2, placeholder: '请输入价税合计' } }
        },
        { field: 'remark', title: '备注', editRender: { name: 'input', attrs: { placeholder: '请输入备注' } } },

    ],
    footerMethod ({ columns, data }) {
        return [
            columns.map((column, columnIndex) => {
                if (columnIndex === 0) {
                    return '总计'
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
    exportConfig: {
        remote: true,
        types: ['xlsx'],
        modes: ['current', 'selected', 'all'],
    },
    checkboxConfig: {
        labelField: 'id',
        reserve: true,
        highlight: true,
        range: true
    },
    editRules: {
        warehouseId: [
            { required: true, message: '仓库名称不能为空' }
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
const saleShipmentsFormState = reactive<SaleShipmentsFormState>({
    id: undefined,
    customerId: '',
    receiptNumber: '',
    otherReceipt: '',
    remark: '',
    collectOfferRate: 0,
    collectOfferAmount: 0,
    collectOfferLastAmount: 0,
    otherAmount: 0,
    thisCollectAmount: 0,
    thisArrearsAmount: 0,
    status: undefined,
    accountId: undefined,
    operatorIds: undefined,
    receiptDate: '',
    warehouseId: '',
    multipleAccountIds: undefined,
    multipleAccountAmounts: undefined,
});

export {
    xGrid,
    sumNum,
    tableData,
    gridOptions,
    saleShipmentsFormState,
    getTaxTotalPrice,
}