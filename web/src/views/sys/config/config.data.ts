import { FormSchema } from '/@/components/Form';
import {useI18n} from "@/hooks/web/useI18n";

const colProps = {
    span: 12,
};

const { t } = useI18n();

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
        label: t('system.configure.name'),
        helpMessage: t('system.configure.noticeOne'),
        colProps,
        componentProps: {
            placeholder: t('system.configure.inputName'),
        },
        defaultValue: 'EAIRP',
        required: true,
    },
    {
        field: 'companyContact',
        component: 'Input',
        label: t('system.configure.contact'),
        colProps,
        componentProps: {
            placeholder: t('system.configure.inputContact'),
        },
    },
    {
        field: 'companyAddress',
        component: 'Input',
        label: t('system.configure.address'),
        colProps,
        componentProps: {
            placeholder: t('system.configure.inputAddress'),
        },
    },
    {
        field: 'companyPhone',
        component: 'Input',
        label: t('system.configure.phone'),
        colProps,
        componentProps: {
            placeholder: t('system.configure.inputPhone'),
        },
    },
    {
        field: 'companyFax',
        component: 'Input',
        label: t('system.configure.fax'),
        colProps,
        componentProps: {
            placeholder: t('system.configure.inputFax'),
        },
    },
    {
        field: 'companyPostCode',
        component: 'Input',
        label: t('system.configure.postalCode'),
        colProps,
        componentProps: {
            placeholder: t('system.configure.inputPostalCode'),
        },
    },
    {
        field: 'saleAgreement',
        component: 'InputTextArea',
        label: t('system.configure.salesProtocol'),
        subLabel: t('system.configure.noticeTwo'),
        colProps,
        componentProps: {
            placeholder: t('system.configure.inputSalesProtocol'),
            rows: 4,
        },
    },
];
