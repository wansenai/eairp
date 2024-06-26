import { BasicColumn, FormSchema } from '@/components/Table';
import { useI18n } from '@/hooks/web/useI18n';
import { h } from 'vue';
import { Switch } from 'ant-design-vue';
import { useMessage } from '@/hooks/web/useMessage';
import { updateStatus } from "@/api/sys/user";
import {getDeptList} from "@/api/sys/dept";
import { getRoleList } from "@/api/sys/role";

const { t } = useI18n();

export const columns: BasicColumn[] = [
  {
    title: t('sys.login.userName'),
    dataIndex: 'username',
    width: 120,
  },
  {
    title: t('sys.user.name'),
    dataIndex: 'name',
    width: 120,
  },
  {
    title: t('sys.user.roleName'),
    dataIndex: 'roleName',
    width: 120,
  },
  {
    title: t('sys.user.status'),
    dataIndex: 'status',
    width: 100,
    customRender: ({ record }) => {
      if (!Reflect.has(record, 'pendingStatus')) {
        record.pendingStatus = false;
      }
      return h(Switch, {
        checked: record.status === 0,
        checkedChildren: t('common.on'),
        unCheckedChildren: t('common.off'),
        loading: record.pendingStatus,
        onChange(checked, _) {
          const {createMessage} = useMessage();
          if (record.id == 1) {
            createMessage.warn(t('common.notice'));
            return;
          }
          record.pendingStatus = true;
          const newStatus = checked ? 0 : 1;
          updateStatus({id: record.id, status: newStatus} )
            .then(() => {
              record.status = newStatus;
            })
            .finally(() => {
              record.pendingStatus = false;
            });
        },
      });
    }
  },
  {
    title: t('sys.login.email'),
    dataIndex: 'email',
    width: 120,
  },
  {
    title: t('sys.login.mobile'),
    dataIndex: 'phoneNumber',
    width: 120,
  },
  {
    title: t('common.createTime'),
    dataIndex: 'createTime',
    width: 180,
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'username',
    label: t('sys.login.userName'),
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    field: 'name',
    label: t('sys.user.name'),
    component: 'Input',
    colProps: { span: 8 },
  },
];

function isNotExist({ values }) {
  return !Boolean(values.id)
}

export const accountFormSchema: FormSchema[] = [
  {
    field: 'id',
    label: '用户id',
    show: false,
    component: 'Input',
  },
  {
    field: 'username',
    label: t('sys.login.userName'),
    component: 'Input',
    helpMessage: [t('sys.tenant.form.noticeThree')],
    rules: [
      {
        required: true,
        message: '请输入用户名',
      },
    ],
    ifShow: isNotExist
  },
  {
    field: 'password',
    label: t('sys.login.password'),
    component: 'InputPassword',
    required: false,
    helpMessage: [t('sys.tenant.form.noticeTwo')],
    ifShow: isNotExist
  },
  {
    field: 'deptId',
    label: t('sys.user.department'),
    component: 'ApiMultipleTreeSelect',
    required: true,
    componentProps: {
      api: getDeptList,
      resultField: 'data',
      labelField: 'deptName',
      valueField: 'id',
    },
  },
  {
    field: 'roleId',
    label: t('sys.user.roleName'),
    required: true,
    component: 'ApiMultipleSelect',
    componentProps: {
      api: getRoleList,
      resultField: 'data',
      labelField: 'roleName',
      valueField: 'id',
    },
  },
  {
    field: 'name',
    label: t('sys.user.name'),
    component: 'Input',
    required: true,
  },
  {
    field: 'phoneNumber',
    label: t('sys.login.mobile'),
    component: 'Input',
    required: true,
  },
  {
    field: 'email',
    label: t('sys.login.email'),
    component: 'Input',
  },
  {
    field: 'remark',
    label: t('sys.user.remake'),
    component: 'InputTextArea',
  },
];
