<!--
  - Copyright (C) 2023-2033 WanSen AI Team
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -     http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  -->

<template>
  <PageWrapper dense contentFullHeight fixedHeight contentClass="flex">
    <DeptTree class="w-1/4 xl:w-1/5" @select="handleSelect" />
    <BasicTable @register="registerTable" class="w-3/4 xl:w-4/5" :searchInfo="searchInfo">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate"> {{ t('sys.user.addAccount') }}</a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
            :actions="[
              {
                icon: 'clarity:info-standard-line',
                tooltip: t('sys.user.viewUserDetails'),
                onClick: handleView.bind(null, record),
              },
              {
                icon: 'clarity:note-edit-line',
                tooltip: t('sys.user.editUserProfile'),
                onClick: handleEdit.bind(null, record),
              },
              {
                icon: 'ant-design:sync',
                tooltip: t('sys.user.resetUserPassword'),
                popConfirm: {
                  title: t('sys.user.confirmPasswordReset') + '?',
                  placement: 'left',
                  confirm: handleResetPassword.bind(null, record),
                },
              },
              {
                icon: 'ant-design:user-delete',
                color: 'error',
                tooltip: t('sys.user.deleteUserAccount') + '?',
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
    <AccountModal @register="registerModal" @success="handleSuccess" />
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent, reactive } from 'vue';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import { getUserList, deleteUser, resetPassword } from '/@/api/sys/user';
  import { PageWrapper } from '/@/components/Page';
  import { useModal } from '/@/components/Modal';
  import { columns, searchFormSchema } from '@/views/sys/user/account.data';
  import { useI18n } from 'vue-i18n';
  import { useMessage } from "@/hooks/web/useMessage";

  import AccountModal from '@/views/sys/user/components/AccountModal.vue';
  import DeptTree from '@/views/sys/user/components/DeptTree.vue';

  export default defineComponent({
    name: 'AccountManagement',
    components: { BasicTable, PageWrapper, DeptTree, AccountModal, TableAction },
    setup() {
      const { t } = useI18n();
      const { createMessage } = useMessage();
      const [registerModal, { openModal }] = useModal();
      const searchInfo = reactive<Recordable>({});
      const [registerTable, { reload }] = useTable({
        title: t('sys.user.userList'),
        api: getUserList,
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

      function handleSelect(deptId) {
        searchInfo.deptId = deptId;
        reload();
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
        const result = await deleteUser([record.id]);
        if (result.code === 'A0003') {
          await reload();
        }
      }

      async function handleResetPassword(record: Recordable) {
        if (record.username === 'admin') {
          createMessage.warn(t('common.notAllowResetAdmin'));
          return;
        }
        await resetPassword(record.id);
      }

      function handleSuccess() {
          reload();
      }

      function handleView(record: Recordable) {
        console.info(record.id);
        // go('/system/account_detail/' + record.id);
      }

      return {
        t,
        registerTable,
        registerModal,
        handleCreate,
        handleEdit,
        handleDelete,
        handleSuccess,
        handleSelect,
        handleView,
        handleResetPassword,
        searchInfo,
      };
    },
  });
</script>
