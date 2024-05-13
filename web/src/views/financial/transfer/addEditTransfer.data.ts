import {reactive, ref} from "vue";
import XEUtils from "xe-utils";
import {VxeGridInstance, VxeGridProps} from "vxe-table";
import {Dayjs} from "dayjs";
import {useI18n} from "@/hooks/web/useI18n";
import {useLocaleStore} from "@/store/modules/locale";

export const { t } = useI18n();
const amountSymbol = ref<string>('')
const localeStore = useLocaleStore().getLocale;
if(localeStore === 'zh_CN') {
    amountSymbol.value = '￥'
} else if (localeStore === 'en') {
    amountSymbol.value = '$'
}

export interface RowVO {
    [key: string]: any,
    accountId: number | string,
    transferAmount: number,
    remark: string,
}

interface TransferFormState {
    id: number | string | undefined;
    receiptDate: string | undefined | Dayjs;
    receiptNumber: string |undefined;
    financialPersonId: number | string |undefined;
    paymentAccountId: number | string |undefined;
    accountId: number | string |undefined;
    paymentAmount: number | string;
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
        zoom: true, // 显示全屏按钮
        custom: true // 显示自定义列按钮
    },
    columns: [
        { type: 'checkbox', field:'id', title: 'ID', width: 180},
        {   field: 'accountId',
            width:200,
            title: t('financial.transfer.view.accountName'),
            slots: { edit: 'id_edit',default: 'id_default' },
            sortable: true,
            editRender: {}
        },
        {   field: 'transferAmount',
            width:200,
            title: t('financial.transfer.view.amount'),
            slots: { edit: 'amount_edit' },
            sortable: true,
            editRender: { name: 'input', attrs: { placeholder: '请输入金额' } }
        },
        { field: 'remark', title: t('financial.transfer.view.remark'), editRender: { name: 'input', attrs: { placeholder: '请输入备注' } } },

    ],
    footerMethod ({ columns, data }) {
        return [
            columns.map((column, columnIndex) => {
                if (columnIndex === 0) {
                    return t('financial.transfer.form.total')
                }
                if (['transferAmount'].includes(column.field)) {
                    transferFormState.paymentAmount = amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(sumNum(data, column.field)), { digits: 2 })}`
                    return amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(sumNum(data, column.field)), { digits: 2 })}`
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
    editRules: {
        accountId: [
            { required: true, message: t('financial.transfer.form.noticeOne') }
        ],
        transferAmount: [
            { required: true, message: t('financial.transfer.form.noticeTwo') }
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
const transferFormState = reactive<TransferFormState>({
    id: undefined,
    receiptDate: '',
    receiptNumber: '',
    financialPersonId: '',
    paymentAccountId: '',
    accountId: 0,
    paymentAmount: 0,
    remark: '',
});

export {
    xGrid,
    sumNum,
    tableData,
    gridOptions,
    transferFormState,
}