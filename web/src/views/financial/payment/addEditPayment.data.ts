import {reactive, ref} from "vue";
import XEUtils from "xe-utils";
import {VxeGridInstance, VxeGridProps} from "vxe-table";
import {Dayjs} from "dayjs";

export interface RowVO {
    [key: string]: any,
    paymentId: number | string;
    purchaseReceiptNumber: string | undefined,
    paymentArrears: number,
    prepaidArrears: number |string,
    thisPaymentAmount: number,
    remark: string,
}

interface PaymentFormState {
    id: number | string | undefined;
    supplierId: number | string | undefined;
    receiptDate: string | undefined | Dayjs;
    receiptNumber: string |undefined;
    financialPersonId: number | string |undefined;
    paymentAccountId: number | string |undefined;
    totalPaymentAmount: number | string;
    discountAmount: number | string;
    actualPaymentAmount: number | string;
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
        { type: 'checkbox', field:'id', title: 'ID', width: 180},
        {   field: 'purchaseReceiptNumber',
            width:200,
            title: '采购单据编号',
        },
        {   field: 'paymentArrears',
            width:180,
            title: '应付欠款',
        },
        {   field: 'prepaidArrears',
            width:180,
            title: '已付欠款',
        },
        {   field: 'thisPaymentAmount',
            width:200,
            title: '本次付款',
            slots: { edit: 'amount_edit' },
            sortable: true,
            editRender: { name: 'input', attrs: { placeholder: '请输入本次收款金额' } }
        },
        { field: 'remark', title: '备注', editRender: { name: 'input', attrs: { placeholder: '请输入备注' } } },

    ],
    footerMethod ({ columns, data }) {
        return [
            columns.map((column, columnIndex) => {
                if (columnIndex === 0) {
                    return '总计'
                }
                if (['thisPaymentAmount', 'rate'].includes(column.field)) {
                    paymentFormState.actualPaymentAmount = `￥${XEUtils.commafy(XEUtils.toNumber(sumNum(data, column.field)), { digits: 2 })}`
                    paymentFormState.totalPaymentAmount = `￥${XEUtils.commafy(XEUtils.toNumber(sumNum(data, column.field)), { digits: 2 })}`
                    getThisPaymentAmount.value = `￥${XEUtils.commafy(XEUtils.toNumber(sumNum(data, column.field)), { digits: 2 })}`
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
    }
})

const getThisPaymentAmount = ref<string>('0')

const sumNum = (list: RowVO[], field: string) => {
    let count = 0
    list.forEach(item => {
        count += Number(item[field])
    })
    return count
}
const paymentFormState = reactive<PaymentFormState>({
    id: undefined,
    supplierId: '',
    receiptDate: '',
    receiptNumber: '',
    financialPersonId: '',
    paymentAccountId: '',
    totalPaymentAmount: 0,
    discountAmount: 0,
    actualPaymentAmount: 0,
    remark: '',
});

export {
    xGrid,
    sumNum,
    tableData,
    gridOptions,
    paymentFormState,
    getThisPaymentAmount
}