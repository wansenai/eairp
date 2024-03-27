import {reactive, ref} from "vue";
import XEUtils from "xe-utils";
import {VxeGridInstance, VxeGridProps} from "vxe-table";
import {Dayjs} from "dayjs";
import {useI18n} from "@/hooks/web/useI18n";

export const { t } = useI18n();

export interface RowVO {
    [key: string]: any,
    collectionId: number | string;
    saleReceiptNumber: string | undefined,
    receivableArrears: number,
    receivedArrears: number |string,
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
        zoom: true,
        custom: true
    },
    columns: [
        { type: 'checkbox', field:'id', title: 'ID', width: 180},
        {   field: 'saleReceiptNumber',
            width:200,
            title:  t('financial.collection.view.saleReceiptNumber'),
        },
        {   field: 'receivableArrears',
            width:180,
            title: t('financial.collection.view.receivableArrears'),
        },
        {   field: 'receivedArrears',
            width:180,
            title: t('financial.collection.view.receivedArrears'),
        },
        {   field: 'thisCollectionAmount',
            width:200,
            title: t('financial.collection.view.thisTimeCollection'),
            slots: { edit: 'amount_edit' },
            sortable: true,
            editRender: { name: 'input', attrs: { placeholder: '请输入本次收款金额' } }
        },
        { field: 'remark', title: t('financial.collection.view.remark'), editRender: { name: 'input', attrs: { placeholder: '请输入备注' } } },

    ],
    footerMethod ({ columns, data }) {
        return [
            columns.map((column, columnIndex) => {
                if (columnIndex === 0) {
                    return t('financial.collection.form.total')
                }
                if (['thisCollectionAmount', 'rate'].includes(column.field)) {
                    collectionFormState.actualCollectionAmount = `￥${XEUtils.commafy(XEUtils.toNumber(sumNum(data, column.field)), { digits: 2 })}`
                    collectionFormState.totalCollectionAmount = `￥${XEUtils.commafy(XEUtils.toNumber(sumNum(data, column.field)), { digits: 2 })}`
                    getThisCollectionAmount.value = `￥${XEUtils.commafy(XEUtils.toNumber(sumNum(data, column.field)), { digits: 2 })}`
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

const getThisCollectionAmount = ref<string>('0')

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
    getThisCollectionAmount
}