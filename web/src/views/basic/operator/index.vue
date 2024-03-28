<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate" v-text="t('basic.operator.add')"/>
        <a-button type="primary" @click="handleBatchDelete" v-text="t('basic.operator.batchDelete')"/>
        <a-button type="primary" @click="handleOnStatus(0)" v-text="t('basic.operator.batchEnable')"/>
        <a-button type="primary" @click="handleOnStatus(1)" v-text="t('basic.operator.batchDisable')"/>
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
      </template>
    </BasicTable>
    <OperatorModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<div>
</div>

<script lang="ts">
import {defineComponent} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {useModal} from "@/components/Modal";
import {useMessage} from "@/hooks/web/useMessage";
import {columns, searchFormSchema} from "@/views/basic/operator/operator.data";
import {getOperatorPageList, deleteBatchOperator, updateOperatorStatus} from "@/api/basic/operator";
import OperatorModal from "@/views/basic/operator/components/OperatorModal.vue";
import {useI18n} from "vue-i18n";

export default defineComponent({
  name: 'Operator',
  components: {TableAction, BasicTable, OperatorModal },
  setup() {
    const { t } = useI18n();
    const [registerModal, {openModal}] = useModal();
    const { createMessage } = useMessage();
    const [registerTable, { reload, getSelectRows }] = useTable({
      title: t('basic.operator.title'),
      api: getOperatorPageList,
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
      const result = await deleteBatchOperator(ids)
      if (result.code === 'O0003') {
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
      const result = await deleteBatchOperator([record.id])
      if (result.code === 'O0003') {
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
      const result = await updateOperatorStatus(ids,newStatus )
      if (result.code === 'O0004') {
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