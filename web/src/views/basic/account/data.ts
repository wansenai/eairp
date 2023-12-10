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
    field: 'phoneNumber',
    component: 'Input',
    label: '联系电话',
    colProps: { span: 18 },
    rules: [
      {
        required: true,
        pattern: /^(0|86|17951)?(13[0-9]|15[012356789]|16[6]|19[89]]|17[01345678]|18[0-9]|14[579])[0-9]{8}$/,
        message: "请输入正确的手机号",
        trigger: 'change',
      }
    ],
  },
  {
    field: 'email',
    component: 'Input',
    label: '邮箱',
    colProps: { span: 18 },
    rules: [
      {
        pattern: /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/,
        message: "请输入正确的邮箱",
        trigger: 'change',
      }
    ],
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
    title: '备用邮箱',
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
    label: '旧密码',
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