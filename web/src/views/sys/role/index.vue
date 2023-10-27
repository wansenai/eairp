<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate"> 新增角色 </a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
              :actions="[
              {
                icon: 'clarity:note-edit-line',
                onClick: handleEdit.bind(null, record),
              },
              {
                icon: 'ant-design:setting',
                onClick: handleRole.bind(null, record),
              },
              {
                icon: 'ant-design:delete-outlined',
                color: 'error',
                popConfirm: {
                  title: '是否确认删除',
                  placement: 'left',
                  confirm: handleDelete.bind(null, record),
                },
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>
    <RoleDrawer @register="registerDrawer" @success="handleSuccess" />
    <RolePermissionModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
import { defineComponent } from 'vue';

import { BasicTable, useTable, TableAction } from '/@/components/Table';
import {deleteRole, getPageList} from '/@/api/sys/role';
import { useDrawer } from '/@/components/Drawer';
import RoleDrawer from '@/views/sys/role/components/RoleDrawer.vue';
import { useI18n } from 'vue-i18n';
import { columns, searchFormSchema } from '@/views/sys/role/role.data';
import {useMessage} from "@/hooks/web/useMessage";
import RolePermissionModal from "@/views/sys/role/components/RolePermissionModal.vue";
import {useModal} from "@/components/Modal";

export default defineComponent({
  name: 'RoleManagement',
  components: {RolePermissionModal, BasicTable, RoleDrawer, TableAction },
  setup() {
    const { t } = useI18n();
    const { createMessage } = useMessage();
    const [registerDrawer, { openDrawer }] = useDrawer();
    const [registerModal, { openModal }] = useModal();
    const [registerTable, { reload }] = useTable({
      title: '角色列表',
      api: getPageList,
      rowKey: 'id',
      columns,
      formConfig: {
        labelWidth: 120,
        schemas: searchFormSchema,
      },
      titleHelpMessage: '角色列表可以给角色赋予不同的权限, 点击操作栏中的齿轮按钮',
      useSearchForm: true,
      showTableSetting: true,
      bordered: true,
      showIndexColumn: true,
      actionColumn: {
        width: 80,
        title: '操作',
        dataIndex: 'action',
        // slots: { customRender: 'action' },
        fixed: undefined,
      },
    });

    function handleCreate() {
      openDrawer(true, {
        isUpdate: false,
      });
    }

    function handleEdit(record: Recordable) {
      if (record.roleName === '管理员') {
        createMessage.warn(t('common.notAllowDeleteAdminData'));
        return;
      }
      openDrawer(true, {
        record,
        isUpdate: true,
      });
    }

    function handleRole(record: Recordable) {
      openModal(true, {
        record
      })
    }

    async function handleDelete(record: Recordable) {
      if (record.roleName === '管理员') {
        createMessage.warn(t('common.notAllowDeleteAdminData'));
        return;
      }
      const result = await deleteRole(record.id);
      if (result.code === 'A0007') {
        await reload();
      }
    }

    function handleSuccess() {
      reload();
    }

    return {
      registerTable,
      registerDrawer,
      registerModal,
      handleCreate,
      handleEdit,
      handleDelete,
      handleSuccess,
      handleRole,
    };
  },
});
</script>