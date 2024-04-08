import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {reactive, UnwrapRef} from 'vue';
import {Dayjs} from "dayjs";
import {useI18n} from "@/hooks/web/useI18n";

export const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: t('financial.advance.table.paymentMember'),
        dataIndex: 'memberName',
        width: 120,
    },
    {
        title: t('financial.advance.table.receiptNumber'),
        dataIndex: 'receiptNumber',
        width: 150,
    },
    {
        title: t('financial.advance.table.receiptDate'),
        dataIndex: 'receiptDate',
        width: 150,
    },
    {
        title: t('financial.advance.table.amountCollected'),
        dataIndex: 'collectedAmount',
        width: 90,
    },
    {
        title: t('financial.advance.table.totalAmount'),
        dataIndex: 'totalAmount',
        width: 90,
    },
    {
        title: t('financial.advance.table.financialPerson'),
        dataIndex: 'financialPersonnel',
        width: 80,
    },
    {
        title: t('financial.advance.table.operator'),
        dataIndex: 'operator',
        width: 80,
    },
    {
        title: t('financial.advance.table.status'),
        dataIndex: 'status',
        width: 100,
    },
    {
        title: t('financial.advance.table.remark'),
        dataIndex: 'remark',
        width: 150,
    },
]

export const searchFormSchema: FormSchema[] = [
    {
        label: t('financial.advance.header.receiptNumber'),
        field: 'receiptNumber',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        field: '[startDate, endDate]',
        label: t('financial.advance.header.receiptDate'),
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: [t('financial.advance.header.starDate'), t('financial.advance.header.endDate')],
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('financial.advance.header.paymentMember'),
        field: 'paymentMember',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('financial.advance.header.status'),
        field: 'type',
        component: 'Select',
        colProps: {
            xl: 8,
            xxl: 8,
        },
        componentProps: {
            options: [
                { label: t('sys.table.unaudited'), value: 0, key: 0 },
                { label: t('sys.table.audited'), value: 1, key: 1 },
            ],
        },
    },
    {
        label: t('financial.advance.header.remark'),
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
        title: t('financial.advance.view.accountName'),
        key: 'accountId',
        width: '25%',
        placeholder: '请选择${title}',
        options: [],
        allowSearch: true,
        validateRules: [{required: true, message: '${title}不能为空'}],
    },
    {
        title: t('financial.advance.view.amount'),
        key: 'amount',
        width: '20%',
        statistics: true,
        placeholder: '请选择${title}',
        validateRules: [{required: true, message: '${title}不能为空'}]
    },
    {
        title: t('financial.advance.view.remark'),
        key: 'remark',
        width: '50%',
        placeholder: '请选择${title}'
    }
]

interface FormState {
    id: string | undefined;
    memberId: string;
    receiptNumber: string;
    financialPersonnelId: string;
    receiptDate: string | undefined | Dayjs;
    remark: string;
    totalAmount: number;
    collectedAmount: number;
}

export const formState: UnwrapRef<FormState> = reactive<FormState>({
    id: '',
    memberId: '',
    receiptNumber: '',
    financialPersonnelId: '',
    receiptDate: undefined,
    remark: '',
    totalAmount: 0,
    collectedAmount: 0,
});

export const advanceChargeTableColumns: BasicColumn[] = [
    {
        title: t('financial.advance.view.accountName'),
        dataIndex: 'accountName',
        width: 200,
    },
    {
        title: t('financial.advance.view.amount'),
        dataIndex: 'amount',
        width: 180,
    },
    {
        title: t('financial.advance.view.remark'),
        dataIndex: 'remark',
        width: 200,
    },
]