import {reactive, ref} from "vue";
import XEUtils from "xe-utils";
import {VxeGridInstance, VxeGridProps} from "vxe-table";
import {Dayjs} from "dayjs";

interface FormState {
    id: number | string | undefined;
    memberId: string;
    receiptNumber: string;
    paymentType: string;
    remark: string;
    receiptAmount: number;
    scanBarCode: string;
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
        {
            field: 'warehouseId',
            title: '仓库名称',
            width: 130,
            editRender: { name: '$select', options: [], props: { placeholder: '请选择仓库' } }
        },
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
            editRender: { name: '$input', props: { type: 'number', min: 1, max: 9999 } }, },
        {
            field: 'retailPrice',
            title: '单价', width:105,
            formatter ({ cellValue }) {
                return cellValue ? `￥${XEUtils.commafy(XEUtils.toNumber(cellValue), { digits: 2 })}` : ''
            },
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
        // { field: 'remark', title: '备注', editRender: { name: 'input', attrs: { placeholder: '请输入备注' } } },

    ],
    footerMethod ({ columns, data }) {
        return [
            columns.map((column, columnIndex) => {
                if (columnIndex === 0) {
                    return '总计'
                }
                if (['amount', 'rate'].includes(column.field)) {
                    // 设置单价 = 金额 / 数量 设置保留两位小数
                    data.forEach(item => {
                        const price = item.amount / item.productNumber
                        item.retailPrice = XEUtils.toFixed(price, 2)
                    })
                    receiptAmount.value = `￥${XEUtils.commafy(XEUtils.toNumber(sumNum(data, column.field)), { digits: 2 })}`
                    collectAmount.value = `￥${XEUtils.commafy(XEUtils.toNumber(sumNum(data, column.field)), { digits: 2 })}`
                    return `￥${XEUtils.commafy(XEUtils.toNumber(sumNum(data, column.field)), { digits: 2 })}`
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
const receiptAmount = ref<string>('');
const collectAmount = ref<string>('');
const sumNum = (list: RowVO[], field: string) => {
    let count = 0
    list.forEach(item => {
        count += Number(item[field])
    })
    return count
}

const formState = reactive<FormState>({
    id: undefined,
    memberId: '',
    receiptNumber: '',
    paymentType: '',
    remark: '',
    receiptAmount: 0,
    scanBarCode: '',
    collectAmount: 0,
    backAmount: 0,
    accountId: '',
    receiptDate: '',
});

export {
    formState,
    gridOptions,
    xGrid,
    receiptAmount,
    collectAmount,
    tableData
}