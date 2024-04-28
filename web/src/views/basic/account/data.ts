import { FormSchema } from '/@/components/Form/index';
import {useI18n} from "@/hooks/web/useI18n";

const { t } = useI18n();

export interface ListItem {
  key: string;
  title: string;
  description: string;
  extra?: string;
  avatar?: string;
  color?: string;
}

// tab的list
export const settingList = [
  {
    key: '1',
    name: t('basic.account.basicSetting'),
    component: 'BaseSetting',
  },
  {
    key: '2',
    name: t('basic.account.safeSetting'),
    component: 'SecureSetting',
  },
  {
    key: '4',
    name: t('basic.account.notice.title'),
    component: 'MsgNotify',
  },
];

// 基础设置 form
export const baseSetSchemas: FormSchema[] = [
  {
    field: 'id',
    component: 'Input',
    ifShow: false,
  },
  {
    field: 'name',
    component: 'Input',
    label: t('basic.account.name'),
    colProps: { span: 18 },
    required: true,
  },
  {
    field: 'position',
    component: 'Input',
    label: t('basic.account.position'),
    colProps: { span: 18 },
  },
  {
    field: 'systemLanguage',
    component: 'Select',
    label: t('basic.account.systemLanguage'),
    helpMessage(renderCallbackParams) {
        return t('basic.account.systemLanguageTip');
    },
    componentProps: {
      options: [
        { label: t('sys.language.zhCN'), value: 'zh_CN', key: 'zh_CN' },
        { label: t('sys.language.enUS'), value: 'en_US', key: 'en_US' },
      ],
    },
    colProps: { span: 18 },
  },
  {
    field: 'description',
    component: 'InputTextArea',
    label: t('basic.account.personalProfile'),
    colProps: { span: 18 },
  },
];

// 安全设置 list
export const secureSettingList: ListItem[] = [
  {
    key: '1',
    title: '账户密码',
    description: t('basic.account.accountPasswordTip'),
    extra: t('basic.account.update'),
  },
  {
    key: '2',
    title: '密保手机',
    description: '已绑定手机：',
    extra: t('basic.account.update'),
  },
  {
    key: '3',
    title: '密保邮箱',
    description: '已绑定邮箱：',
    extra: t('basic.account.update'),
  },
];


export const resetPasswordFormSchema: FormSchema[] = [
  {
    field: 'id',
    component: 'Input',
    ifShow: false,
  },
  {
    field: 'userName',
    component: 'Input',
    ifShow: false,
  },
  {
    label: t('basic.account.password.oldPassword'),
    field: 'password',
    component: 'InputPassword',
    required: true,
    rules: [
        {
            required: true,
            message: t('basic.account.password.inputOldPassword'),
            trigger: 'change',
        },
    ],
  },
  {
    label: t('basic.account.password.newPassword'),
    field: 'newPassword',
    valueField: 'newPassword',
    component: 'InputPassword',
    required: true,
    rules: [
        {
            required: true,
            message: t('basic.account.password.inputNewPassword'),
            trigger: 'change',
        },
    ],
  },
  {
    label: t('basic.account.password.confirmPassword'),
    field: 'confirmPassword',
    component: 'InputPassword',
    required: true,
    rules: [
        {
            required: true,
            message: t('basic.account.password.inputConfirmPassword'),
            trigger: 'change',
        }
    ],
  },
]