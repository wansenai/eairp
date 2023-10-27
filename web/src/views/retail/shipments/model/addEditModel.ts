import {reactive} from "vue";

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

const tableColumns = [
    { title: '仓库名称', key: 'depotId', width: '10%', placeholder: '请选择${title}', options: [],
        allowSearch:true, validateRules: [{ required: true, message: '${title}不能为空' }]
    },
    { title: '条码', key: 'barCode', width: '16%', kind: 'material', multi: true,
        validateRules: [{ required: true, message: '${title}不能为空' }]
    },
    { title: '名称', key: 'name', width: '12%' },
    { title: '规格', key: 'standard', width: '10%' },
    { title: '库存', key: 'stock', width: '8%' },
    { title: '单位', key: 'unit', width: '8%' },
    { title: '数量', key: 'operNumber', width: '6%', statistics: true,
        validateRules: [{ required: true, message: '${title}不能为空' }]
    },
    { title: '单价', key: 'unitPrice', width: '6%'},
    { title: '金额', key: 'allPrice', width: '6%', statistics: true },
    { title: '备注', key: 'remark', width: '7%'}
]

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
    tableColumns
}