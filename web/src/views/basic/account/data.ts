import { FormSchema } from '/@/components/Form/index';

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
    name: '基本设置',
    component: 'BaseSetting',
  },
  {
    key: '2',
    name: '安全设置',
    component: 'SecureSetting',
  },
  {
    key: '4',
    name: '新消息通知',
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
    label: '昵称',
    colProps: { span: 18 },
    required: true,
  },
  {
    field: 'position',
    component: 'Input',
    label: '职位',
    colProps: { span: 18 },
  },
  {
    field: 'description',
    component: 'InputTextArea',
    label: '个人简介',
    colProps: { span: 18 },
  },
];

// 安全设置 list
export const secureSettingList: ListItem[] = [
  {
    key: '1',
    title: '账户密码',
    description: '当前密码强度：中',
    extra: '修改',
  },
  {
    key: '2',
    title: '密保手机',
    description: '已绑定手机：',
    extra: '修改',
  },
  {
    key: '3',
    title: '密保邮箱',
    description: '已绑定邮箱：',
    extra: '修改',
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
    label: '原密码',
    field: 'password',
    component: 'InputPassword',
    required: true,
    rules: [
        {
            required: true,
            message: '请输入旧密码',
            trigger: 'change',
        },
    ],
  },
  {
    label: '新密码',
    field: 'newPassword',
    valueField: 'newPassword',
    component: 'InputPassword',
    required: true,
    rules: [
        {
            required: true,
            message: '请输入新密码',
            trigger: 'change',
        },
    ],
  },
  {
    label: '确认密码',
    field: 'confirmPassword',
    component: 'InputPassword',
    required: true,
    rules: [
        {
            required: true,
            message: '请输入确认密码',
            trigger: 'change',
        }
    ],
  },
]