import {reactive, ref} from "vue";
import XEUtils from "xe-utils";
import {VxeGridInstance, VxeGridListeners, VxeGridProps, VXETable} from "vxe-table";

interface FormState {
    memberId: string;
    receiptNumber: string;
    paymentType: string;
    remark: string;
    receiptAmount: number;
    scanBarCode: string;
    paymentAmount: number;
    backAmount: number;
    accountReceivable: string;
}


interface RowVO {
    [key: string]: any
}

const serveApiUrl = 'https://api.vxetable.cn/demo'

const xGrid = ref<VxeGridInstance<RowVO>>()

const gridEvent: VxeGridListeners<RowVO> = {
    proxyQuery () {
        console.log('数据代理查询事件')
    },
    proxyDelete () {
        console.log('数据代理删除事件')
    },
    proxySave () {
        console.log('数据代理保存事件')
    }
}
const gridOptions = reactive<VxeGridProps<RowVO>>({
    border: true,
    showHeaderOverflow: true,
    showOverflow: true,
    keepSource: true,
    id: 'full_edit_1',
    height: 400,
    rowConfig: {
        keyField: 'id',
        isHover: true
    },
    columnConfig: {
        resizable: true
    },
    customConfig: {
        storage: true,
        checkMethod ({ column }) {
            if (['nickname', 'role'].includes(column.field)) {
                return false
            }
            return true
        }
    },
    printConfig: {
        columns: [
            { field: 'name' },
            { field: 'email' },
            { field: 'nickname' },
            { field: 'age' },
            { field: 'amount' }
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
        buttons: [
            { code: 'insert_actived', name: '新增' },
            { code: 'delete', name: '直接删除' },
            { code: 'mark_cancel', name: '临时删除/取消' },
            { code: 'save', name: '保存数据', status: 'success' }
        ],
        refresh: true, // 显示刷新按钮
        export: true, // 显示导出按钮
        print: true, // 显示打印按钮
        zoom: true, // 显示全屏按钮
        custom: true // 显示自定义列按钮
    },
    proxyConfig: {
        seq: true, // 启用动态序号代理，每一页的序号会根据当前页数变化
        sort: true, // 启用排序代理，当点击排序时会自动触发 query 行为
        filter: true, // 启用筛选代理，当点击筛选时会自动触发 query 行为
        form: true, // 启用表单代理，当点击表单提交按钮时会自动触发 reload 行为
        props: {
            // 对应响应结果 Promise<{ result: [], page: { total: 100 } }>
            result: 'result', // 配置响应结果列表字段
            total: 'page.total' // 配置响应结果总页数字段
        },
        // 只接收Promise，具体实现自由发挥
        ajax: {
            // 当点击工具栏查询按钮或者手动提交指令 query或reload 时会被触发
            query: ({ page, sorts, filters, form }) => {
                const queryParams: any = Object.assign({}, form)
                // 处理排序条件
                const firstSort = sorts[0]
                if (firstSort) {
                    queryParams.sort = firstSort.field
                    queryParams.order = firstSort.order
                }
                // 处理筛选条件
                filters.forEach(({ field, values }) => {
                    queryParams[field] = values.join(',')
                })
                return fetch(`${serveApiUrl}/api/pub/page/list/${page.pageSize}/${page.currentPage}?${XEUtils.serialize(queryParams)}`).then(response => response.json())
            },
            // 当点击工具栏删除按钮或者手动提交指令 delete 时会被触发
            delete: ({ body }) => {
                return fetch(`${serveApiUrl}/api/pub/save`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) }).then(response => response.json())
            },
            // 当点击工具栏保存按钮或者手动提交指令 save 时会被触发
            save: ({ body }) => {
                return fetch(`${serveApiUrl}/api/pub/save`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) }).then(response => response.json())
            }
        }
    },
    columns: [
        { type: 'checkbox', title: 'ID', width: 80 },
        {
            field: 'warehouseId',
            title: '仓库名称',
            width: 100,
            editRender: { name: '$select', options: [], props: { placeholder: '请选择仓库' } }
        },
        { field: 'barcode', title: '条码', sortable: true, titlePrefix: { message: '输入条码商品信息自动带出！' }, editRender: { name: 'input', attrs: { placeholder: '请输入条码' } } },
        {
            field: 'productName',
            title: '名称',

        },
        { field: 'productStandard', title: '规格', width: 90,  },
        { field: 'stockNumber', title: '库存',  width: 50},
        { field: 'productUnit', title: '单位',  width: 50},
        { field: 'productNumber', title: '数量',  sortable: true, editRender: { name: '$input', props: { type: 'number', min: 1, max: 9999 } } },
        {
            field: 'unitPrice',
            title: '单价',
            formatter ({ cellValue }) {
                return cellValue ? `￥${XEUtils.commafy(XEUtils.toNumber(cellValue), { digits: 2 })}` : ''
            },
            editRender: { name: '$input', props: { type: 'float', digits: 2, placeholder: '输入单价' } }
        },
        {
            field: 'amount',
            title: '金额',
            formatter ({ cellValue }) {
                return cellValue ? `￥${XEUtils.commafy(XEUtils.toNumber(cellValue), { digits: 2 })}` : ''
            },
            editRender: { name: '$input', props: { type: 'float', digits: 2, placeholder: '输入金额' } }
        },
        { field: 'remark', title: '备注', editRender: { name: 'input', attrs: { placeholder: '请输入备注' } } },

    ],
    exportConfig: {
        remote: true,
        types: ['xlsx'],
        modes: ['current', 'selected', 'all'],
        // 自定义服务端导出
        exportMethod ({ options }) {
            const $grid = xGrid.value
            if ($grid) {
                const proxyInfo = $grid.getProxyInfo()
                // 传给服务端的参数
                const body = {
                    filename: options.filename,
                    sheetName: options.sheetName,
                    isHeader: options.isHeader,
                    original: options.original,
                    mode: options.mode,
                    pager: proxyInfo ? proxyInfo.pager : null,
                    ids: options.mode === 'selected' ? options.data.map((item) => item.id) : [],
                    fields: options.columns.map((column) => {
                        return {
                            field: column.field,
                            title: column.title
                        }
                    })
                }
                // 开始服务端导出
                return fetch(`${serveApiUrl}/api/pub/export`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) }).then(response => response.json()).then(data => {
                    if (data.id) {
                        VXETable.modal.message({ content: '导出成功，开始下载', status: 'success' })
                        // 读取路径，请求文件
                        fetch(`${serveApiUrl}/api/pub/export/download/${data.id}`).then(response => {
                            response.blob().then(blob => {
                                // 开始下载
                                VXETable.saveFile({ filename: '导出数据', type: 'xlsx', content: blob })
                            })
                        })
                    }
                }).catch(() => {
                    VXETable.modal.message({ content: '导出失败！', status: 'error' })
                })
            }
            return Promise.resolve()
        }
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
        ],
        barcode: [
            { required: true, message: '条码不能为空' }
        ]
    },
    editConfig: {
        trigger: 'click',
        mode: 'row',
        showStatus: true
    }
})

const formState = reactive<FormState>({
    memberId: '',
    receiptNumber: '',
    paymentType: '',
    remark: '',
    receiptAmount: 0,
    scanBarCode: '',
    paymentAmount: 0,
    backAmount: 0,
    accountReceivable: '',
});

export {
    formState,
    gridOptions,
    xGrid,
    gridEvent
}