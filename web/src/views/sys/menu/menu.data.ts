import { BasicColumn, FormSchema } from '/@/components/Table';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';
import Icon from '@/components/Icon/Icon.vue';
import {getMenuList} from "@/api/sys/menu";
import {ParentIdEnum} from "@/enums/appEnum";
import {useI18n} from "@/hooks/web/useI18n";

const { t } = useI18n();

export const columns: BasicColumn[] = [
    {
        title: t('system.menu.table.menuTitle'),
        dataIndex: 'title',
        width: 150,
        align: 'left',
    },
    {
        title: t('system.menu.table.icon'),
        dataIndex: 'icon',
        width: 80,
        customRender: ({ record }) => {
            return h(Icon, { icon: record.icon });
        },
    },
    {
        title: t('system.menu.table.path'),
        dataIndex: 'path',
        width: 180,
    },
    {
        title: t('system.menu.table.component'),
        dataIndex: 'component',
        width: 200,
    },
    {
        title: t('system.menu.table.sort'),
        dataIndex: 'sort',
        width: 50,
    },
    {
        title: t('system.menu.table.status'),
        dataIndex: 'status',
        width: 80,
        customRender: ({ record }) => {
            const status = record.status;
            const enable = ~~status === 0;
            const color = enable ? 'green' : 'red';
            const text = enable ? t('system.menu.form.enable') : t('system.menu.form.disable');
            return h(Tag, { color: color }, () => text);
        },
    },
    {
        title: t('system.menu.table.createTime'),
        dataIndex: 'createTime',
        width: 180,
    },
];
const isMenu = (type: number) => type === 1;

export const searchFormSchema: FormSchema[] = [
    {
        field: 'title',
        label: t('system.menu.table.menuTitle'),
        component: 'Input',
        colProps: { span: 8 },
    },
    {
        field: 'status',
        label: t('system.menu.table.status'),
        component: 'Select',
        componentProps: {
            options: [
                { label: t('system.menu.form.enable'), value: 0 },
                { label: t('system.menu.form.disable'), value: 1 },
            ],
        },
        colProps: { span: 8 },
    },
];

export const formSchema: FormSchema[] = [
    {
        field: 'id',
        label: '菜单ID',
        component: 'Input',
        show: false,
    },
    {
        field: 'menuType',
        label: t('system.menu.form.menuType'),
        component: 'RadioButtonGroup',
        defaultValue: 1,
        componentProps: {
            options: [
                { label: t('system.menu.form.catalogue'), value: 0 },
                { label: t('system.menu.form.menu'), value: 1 },
            ],
        },
        colProps: { lg: 24, md: 24 },
    },
    {
        field: 'name',
        label: t('system.menu.form.menuName'),
        component: 'Input',
        required: true,
    },
    {
        field: 'title',
        label: t('system.menu.form.menuTitle'),
        component: 'Input',
        required: true,
    },
    {
        field: 'parentId',
        label: t('system.menu.form.parent'),
        component: 'ApiTreeSelect',
        helpMessage: [t('system.menu.form.notice')],
        componentProps: {
            api: getMenuList,
            resultField: 'data.data',
            labelField: 'title',
            valueField: 'id',
            defaultValue: {
                id: ParentIdEnum.DEFAULT,
                parentId: -1,
                label: t('system.menu.form.rootMenu'),
                value: ParentIdEnum.DEFAULT,
            },
        },
    },
    {
        field: 'sort',
        label: t('system.menu.form.sort'),
        component: 'InputNumber',
        required: true,
    },
    {
        field: 'icon',
        label: t('system.menu.form.icon'),
        component: 'IconPicker',
        required: true,
    },

    {
        field: 'path',
        label: t('system.menu.form.routeAddress'),
        component: 'Input',
        required: true,
        ifShow: ({ values }) => isMenu(values.menuType),
    },
    {
        field: 'component',
        label: t('system.menu.form.componentPath'),
        component: 'Input',
        ifShow: ({ values }) => isMenu(values.menuType),
    },
    {
        field: 'status',
        label: t('system.menu.form.status'),
        component: 'RadioButtonGroup',
        defaultValue: 0,
        componentProps: {
            options: [
                { label: t('system.menu.form.enable'), value: 0 },
                { label: t('system.menu.form.disable'), value: 1 },
            ],
        },
    },
    {
        field: 'blank',
        label: t('system.menu.form.isExternalLink'),
        component: 'RadioButtonGroup',
        defaultValue: 0,
        componentProps: {
            options: [
                { label: t('system.menu.form.no'), value: 0 },
                { label: t('system.menu.form.yes'), value: 1 },
            ],
        },
        ifShow: ({ values }) => isMenu(values.menuType),
    },

    {
        field: 'ignoreKeepAlive',
        label: t('system.menu.form.isCached'),
        component: 'RadioButtonGroup',
        defaultValue: 0,
        componentProps: {
            options: [
                { label: t('system.menu.form.no'), value: 0 },
                { label: t('system.menu.form.yes'), value: 1 },
            ],
        },
        ifShow: ({ values }) => isMenu(values.menuType),
    },

    {
        field: 'hideMenu',
        label: t('system.menu.form.isDisplayed'),
        component: 'RadioButtonGroup',
        defaultValue: 0,
        componentProps: {
            options: [
                { label: t('system.menu.form.yes'), value: 0 },
                { label: t('system.menu.form.no'), value: 1 },
            ],
        },
        ifShow: ({ values }) => isMenu(values.menuType),
    },
];
