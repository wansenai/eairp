import { FormSchema } from '/@/components/Form';

const colProps = {
    span: 12,
};

export const schemas: FormSchema[] = [
    {
        field: 'id',
        component: 'Input',
        label: '系统配置id',
        ifShow: false
    },
    {
        field: 'companyName',
        component: 'Input',
        label: '公司名称',
        helpMessage: '更换公司名称后，请刷新浏览器生效',
        colProps,
        componentProps: {
            placeholder: '请输入公司名称',
        },
        defaultValue: 'EAIRP',
        required: true,
    },
    {
        field: 'companyContact',
        component: 'Input',
        label: '联系人',
        colProps,
        componentProps: {
            placeholder: '请输入联系人',
        },
    },
    {
        field: 'companyAddress',
        component: 'Input',
        label: '公司地址',
        colProps,
        componentProps: {
            placeholder: '请输入公司地址',
        },
    },
    {
        field: 'companyPhone',
        component: 'Input',
        label: '公司电话',
        colProps,
        componentProps: {
            placeholder: '请输入公司电话',
        },
    },
    {
        field: 'companyFax',
        component: 'Input',
        label: '公司传真',
        colProps,
        componentProps: {
            placeholder: '请输入公司传真',
        },
    },
    {
        field: 'companyPostCode',
        component: 'Input',
        label: '公司邮编',
        colProps,
        componentProps: {
            placeholder: '请输入公司邮编',
        },
    },
    {
        field: 'saleAgreement',
        component: 'InputTextArea',
        label: '销售协议',
        subLabel: '( 选填 )',
        colProps,
        componentProps: {
            placeholder: '请输入销售协议',
            rows: 4,
        },
    },
];
