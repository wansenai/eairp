import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {h, reactive} from 'vue';
import {Switch} from "ant-design-vue";
import {useMessage} from "@/hooks/web/useMessage";
import {useI18n} from "@/hooks/web/useI18n";

const {t} = useI18n();

export const columns: BasicColumn[] = [
    {
        title: '付款会员',
        dataIndex: 'memberName',
        width: 120,
    },
    {
        title: '单据编号',
        dataIndex: 'receiptNumber',
        width: 150,
    },
    {
        title: '单据日期',
        dataIndex: 'receiptDate',
        width: 150,
    },
    {
        title: '收款金额',
        dataIndex: 'collectedAmount',
        width: 90,
    },
    {
        title: '合计金额',
        dataIndex: 'totalAmount',
        width: 90,
    },
    {
        title: '备注',
        dataIndex: 'remark',
        width: 150,
    },
    {
        title: '财务人员',
        dataIndex: 'financialPersonnel',
        width: 80,
    },
    {
        title: '操作员',
        dataIndex: 'operator',
        width: 80,
    },
    {
        title: '状态',
        dataIndex: 'createTime',
        width: 100,
    }
]

export const searchFormSchema: FormSchema[] = [
    {
        label: '单据编号',
        field: 'memberNumber',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        field: '[startDate, endDate]',
        label: '单据日期',
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: ['开始日期', '结束日期'],
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: '付款会员',
        field: 'phoneNumber',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: '单据状态',
        field: 'type',
        component: 'Select',
        colProps: {
            xl: 8,
            xxl: 8,
        },
        componentProps: {
            options: [
                {label: '未审核', value: 0, key: 0},
                {label: '已审核', value: 1, key: 1},
            ],
        },
    },
    {
        label: '单据备注',
        field: 'type',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    }
]

export const formSchema: FormSchema[] = []

export const tableColumns = [
    {
        title: '账户名称',
        key: 'accountId',
        width: '25%',
        placeholder: '请选择${title}',
        options: [],
        allowSearch: true,
        validateRules: [{required: true, message: '${title}不能为空'}],
    },
    {
        title: '金额',
        key: 'amount',
        width: '20%',
        statistics: true,
        placeholder: '请选择${title}',
        validateRules: [{required: true, message: '${title}不能为空'}]
    },
    {
        title: '备注',
        key: 'remark',
        width: '50%',
        placeholder: '请选择${title}'
    }
]

interface FormState {
    memberId: string;
    receiptNumber: string;
    financialPersonnelId: string;
    remark: string;
    totalPrice: number;
    changeAmount: number;
}

export const formState = reactive<FormState>({
    memberId: '',
    receiptNumber: '',
    financialPersonnelId: '',
    remark: '',
    totalPrice: 0,
    changeAmount: 0,

});