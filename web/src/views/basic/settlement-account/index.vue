<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate" v-text="t('basic.settlement.add')"/>
        <a-button type="primary" @click="handleBatchDelete" v-text="t('basic.settlement.batchDelete')"/>
        <a-button type="primary" @click="handleOnStatus(0)" v-text="t('basic.settlement.batchEnable')"/>
        <a-button type="primary" @click="handleOnStatus(1)" v-text="t('basic.settlement.batchDisable')"/>
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
                icon: 'ant-design:delete-outlined',
                tooltip: t('sys.table.delete'),
                color: 'error',
                popConfirm: {
                  title: t('sys.table.confirmDelete'),
                  placement: 'left',
                  confirm: handleDelete.bind(null, record),
                },
              },
            ]"
          />
        </template>
        <template v-else-if="column.key === 'isDefault'">
          <Tag :color="record.isDefault === 1 ? 'green' : 'red'">
            {{ record.isDefault === 1 ? t('basic.settlement.yes') : t('basic.settlement.no') }}
          </Tag>
        </template>
      </template>
    </BasicTable>
    <FinancialAccountModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<div>
</div>

<script lang="ts">
import {defineComponent} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {useModal} from "@/components/Modal";
import {useMessage} from "@/hooks/web/useMessage";
import {columns, searchFormSchema} from "@/views/basic/settlement-account/financialAccount.data";
import {getAccountPageList, deleteBatchAccount, updateAccountStatus} from "@/api/financial/account";
import FinancialAccountModal from "@/views/basic/settlement-account/components/FinancialAccountModal.vue";
import { Tag } from 'ant-design-vue';
import {useI18n} from "vue-i18n";

export default defineComponent({
  name: 'financialAccount',
  components: {TableAction, BasicTable, Tag, FinancialAccountModal },
  setup() {
    const { t } = useI18n();
    const [registerModal, {openModal}] = useModal();
    const { createMessage } = useMessage();
    const [registerTable, { reload, getSelectRows }] = useTable({
      title: t('basic.settlement.title'),
      api: getAccountPageList,
      rowKey: 'id',
      columns: columns,
      rowSelection: {
        type: 'checkbox',
      },
      formConfig: {
        labelWidth: 110,
        schemas: searchFormSchema,
        autoSubmitOnEnter: true,
      },
      useSearchForm: true,
      clickToRowSelect: false,
      bordered: true,
      showTableSetting: true,
      striped: true,
      showIndexColumn: true,
      actionColumn: {
        width: 80,
        title: t('common.operating'),
        dataIndex: 'action',
        fixed: undefined,
      },
    });

    async function handleCreate() {
      openModal(true, {
        isUpdate: false,
      });
    }

    async function handleBatchDelete(record: Recordable) {
      const data = getSelectRows();
      if (data.length === 0) {
        createMessage.warn(t('basic.selectData'));
        return;
      }
      const ids = data.map((item) => item.id);
      const result = await deleteBatchAccount(ids)
      if (result.code === 'F0003') {
        await reload();
      }
    }

    async function handleEdit(record: Recordable) {
      openModal(true, {
        record,
        isUpdate: true,
      });
    }

    async function handleDelete(record: Recordable) {
      const result = await deleteBatchAccount([record.id])
      if (result.code === 'F0003') {
        await reload();
      }
    }

    async function handleSuccess() {
      reload();
    }

    async function handleOnStatus(newStatus: number) {
      const data = getSelectRows();
      if (data.length === 0) {
        createMessage.warn(t('basic.selectData'));
        return;
      }
      const ids = data.map((item) => item.id);
      const result = await updateAccountStatus(ids,newStatus )
      if (result.code === 'F0004') {
        await reload();
      }
    }

    async function handleCancel() {
      reload();
    }

    return {
      t,
      registerTable,
      registerModal,
      handleCreate,
      handleDelete,
      handleBatchDelete,
      handleEdit,
      handleSuccess,
      handleOnStatus,
      handleCancel,
    }
  }
})
</script>