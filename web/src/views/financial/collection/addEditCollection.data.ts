import {reactive, ref} from "vue";
import XEUtils from "xe-utils";
import {VxeGridInstance, VxeGridProps} from "vxe-table";
import {Dayjs} from "dayjs";

export interface RowVO {
    [key: string]: any,
    collectionId: string,
    saleReceiptNumber: string,
    receivableArrears: number,
    receivedArrears: number,
    thisCollectionAmount: number,
    remark: string,
}

interface CollectionFormState {
    id: number | string | undefined;
    customerId: number | string | undefined;
    receiptDate: string | undefined | Dayjs;
    receiptNumber: string |undefined;
    financialPersonId: number | string |undefined;
    collectionAccountId: number | string |undefined;
    totalCollectionAmount: number | string;
    discountAmount: number | string;
    actualCollectionAmount: number | string;
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
        {   field: 'saleReceiptNumber',
            width:180,
            title: '销售单据编号',
        },
        {   field: 'receivableArrears',
            width:180,
            title: '应收欠款',
        },
        {   field: 'receivedArrears',
            width:180,
            title: '已收欠款',
        },
        {   field: 'thisCollectionAmount',
            width:200,
            title: '本次收款',
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
                if (['thisCollectionAmount', 'rate'].includes(column.field)) {
                    collectionFormState.actualCollectionAmount = `￥${XEUtils.commafy(XEUtils.toNumber(sumNum(data, column.field)), { digits: 2 })}`
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

const sumNum = (list: RowVO[], field: string) => {
    let count = 0
    list.forEach(item => {
        count += Number(item[field])
    })
    return count
}
const collectionFormState = reactive<CollectionFormState>({
    id: undefined,
    customerId: '',
    receiptDate: '',
    receiptNumber: '',
    financialPersonId: '',
    collectionAccountId: '',
    totalCollectionAmount: 0,
    discountAmount: 0,
    actualCollectionAmount: 0,
    remark: '',
});

export {
    xGrid,
    sumNum,
    tableData,
    gridOptions,
    collectionFormState,
}