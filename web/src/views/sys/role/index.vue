<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate"> {{ t('system.role.addRole') }} </a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
              :actions="[
              {
                icon: 'clarity:note-edit-line',
                tooltip: t('sys.table.edit'),
                onClick: handleEdit.bind(null, record),
              },
              {
                icon: 'ant-design:setting',
                tooltip: t('system.role.menuAllocation'),
                onClick: handleRole.bind(null, record),
              },
              {
                icon: 'ant-design:delete-outlined',
                color: 'error',
                tooltip: t('sys.table.delete'),
                popConfirm: {
                  title: t('sys.table.confirmDelete'),
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
import { columns, searchFormSchema } from '@/views/sys/role/role.data';
import {useMessage} from "@/hooks/web/useMessage";
import RolePermissionModal from "@/views/sys/role/components/RolePermissionModal.vue";
import {useModal} from "@/components/Modal";
import {useI18n} from "vue-i18n";
export default defineComponent({
  name: 'RoleManagement',
  components: {RolePermissionModal, BasicTable, RoleDrawer, TableAction },
  setup() {
    const { t } = useI18n();
    const { createMessage } = useMessage();
    const [registerDrawer, { openDrawer }] = useDrawer();
    const [registerModal, { openModal }] = useModal();
    const [registerTable, { reload }] = useTable({
      title: t('system.role.title'),
      api: getPageList,
      rowKey: 'id',
      columns,
      formConfig: {
        labelWidth: 120,
        schemas: searchFormSchema,
      },
      titleHelpMessage: t('system.role.titleNotice'),
      useSearchForm: true,
      showTableSetting: true,
      bordered: true,
      showIndexColumn: true,
      actionColumn: {
        width: 80,
        title: t('common.operating'),
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
      t,
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