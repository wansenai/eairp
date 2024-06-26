<template>
  <div>
    <BasicTable @register="registerTable" :searchInfo="searchInfo">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate"> {{ t('sys.tenant.addTenant') }}</a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
              :actions="[
              {
                icon: 'clarity:note-edit-line',
                tooltip: t('sys.tenant.editTenant'),
                onClick: handleEdit.bind(null, record),
              },
              {
                icon: 'ant-design:user-delete',
                color: 'error',
                tooltip: t('sys.tenant.deleteTenant') + '?',
                popConfirm: {
                  title: t('common.deleteConfirm'),
                  placement: 'left',
                  confirm: handleDelete.bind(null, record),
                },
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>
    <TenantModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
import { defineComponent, reactive } from 'vue';
import { BasicTable, useTable, TableAction } from '@/components/Table';
import {deleteTenant, getTenantList} from "@/api/sys/tenant";
import { PageWrapper } from '@/components/Page';
import { useModal } from '@/components/Modal';
import { columns, searchFormSchema } from '@/views/sys/tenant/tenant.data';
import { useI18n } from 'vue-i18n';
import { useMessage } from "@/hooks/web/useMessage";

import TenantModal from "@/views/sys/tenant/components/TenantModal.vue";

export default defineComponent({
  name: 'TenantManagement',
  components: {TenantModal, BasicTable, PageWrapper, TableAction },
  setup() {
    const { t } = useI18n();
    const { createMessage } = useMessage();
    const [registerModal, { openModal }] = useModal();
    const searchInfo = reactive<Recordable>({});
    const [registerTable, { reload }] = useTable({
      title: t('sys.tenant.tenantList'),
      api: getTenantList,
      rowKey: 'id',
      columns,
      formConfig: {
        labelWidth: 120,
        schemas: searchFormSchema,
        autoSubmitOnEnter: true,
      },
      useSearchForm: true,
      showTableSetting: true,
      bordered: true,
      handleSearchInfoFn(info) {
        return info;
      },
      actionColumn: {
        width: 120,
        title: t('common.action'),
        dataIndex: 'action',
      },
    });

    function handleCreate() {
      openModal(true, {
        isUpdate: false,
      });
    }

    function handleEdit(record: Recordable) {
      if (record.username === 'admin') {
        createMessage.warn(t('common.notAllowEditAdminData'));
        return;
      }
      openModal(true, {
        record,
        isUpdate: true,
      });
    }

    async function handleDelete(record: Recordable) {
      if (record.username === 'admin') {
        createMessage.warn(t('common.notAllowDeleteAdminData'));
        return;
      }
      const result = await deleteTenant(record.id);
      if (result.code === 'A0307') {
        await reload();
      }
    }


    function handleSuccess() {
      reload();
    }

    return {
      t,
      registerTable,
      registerModal,
      handleCreate,
      handleEdit,
      handleDelete,
      handleSuccess,
      searchInfo,
    };
  },
});
</script>
