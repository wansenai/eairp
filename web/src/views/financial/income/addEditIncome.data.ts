import {reactive, ref} from "vue";
import XEUtils from "xe-utils";
import {VxeGridInstance, VxeGridProps} from "vxe-table";
import {Dayjs} from "dayjs";
import {useI18n} from "@/hooks/web/useI18n";

export const { t } = useI18n();

export interface RowVO {
    [key: string]: any,
    incomeExpenseId: number | string,
    incomeExpenseAmount: number,
    remark: string,
}

interface IncomeFormState {
    id: number | string | undefined;
    relatedPersonId: number | string | undefined;
    receiptDate: string | undefined | Dayjs;
    receiptNumber: string |undefined;
    financialPersonId: number | string |undefined;
    incomeAccountId: number | string |undefined;
    incomeExpenseAmount: number | string;
    incomeAmount: number | string;
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
        {   field: 'incomeExpenseId',
            width:200,
            title: t('financial.income.view.incomeExpenseName'),
            slots: { edit: 'id_edit',default: 'id_default' },
            sortable: true,
            editRender: {}
        },
        {   field: 'incomeExpenseAmount',
            width:200,
            title: t('financial.income.view.amount'),
            slots: { edit: 'amount_edit' },
            sortable: true,
            editRender: { name: 'input', attrs: { placeholder: '请输入金额' } }
        },
        { field: 'remark', title: t('financial.income.view.remark'), editRender: { name: 'input', attrs: { placeholder: '请输入备注' } } },

    ],
    footerMethod ({ columns, data }) {
        return [
            columns.map((column, columnIndex) => {
                if (columnIndex === 0) {
                    return t('financial.income.view.total')
                }
                if (['incomeExpenseAmount', 'rate'].includes(column.field)) {
                    incomeFormState.incomeAmount = `￥${XEUtils.commafy(XEUtils.toNumber(sumNum(data, column.field)), { digits: 2 })}`
                    return `￥${XEUtils.commafy(XEUtils.toNumber(sumNum(data, column.field)), { digits: 2 })}`
                }
                return ''
            })
        ]
    },
    editRules: {
        incomeExpenseId: [
            { required: true, message: t('financial.income.form.noticeOne') }
        ],
        incomeExpenseAmount: [
            { required: true, message: t('financial.income.form.noticeTwo') }
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
const incomeFormState = reactive<IncomeFormState>({
    id: undefined,
    relatedPersonId: '',
    receiptDate: '',
    receiptNumber: '',
    financialPersonId: '',
    incomeAccountId: 0,
    incomeExpenseAmount: 0,
    incomeAmount: 0,
    remark: '',
});

export {
    xGrid,
    sumNum,
    tableData,
    gridOptions,
    incomeFormState,
}