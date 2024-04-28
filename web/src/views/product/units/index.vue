<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate" v-text="t('product.unit.add')" />
        <a-button type="primary" @click="handleBatchDelete" v-text="t('product.unit.batchDelete')" />
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
              :actions="[
              {
                icon: 'clarity:note-edit-line',
                tooltip: t('product.unit.table.edit'),
                onClick: handleEdit.bind(null, record),
              },
              {
                icon: 'ant-design:delete-outlined',
                tooltip: t('product.unit.table.delete'),
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
    <UnitModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
import { defineComponent } from 'vue';
import {BasicTable, TableAction, useTable} from '/@/components/Table';
import {columns, searchFormSchema} from "@/views/product/units/units.data";
import {getUnitList, deleteBatchUnits} from "@/api/product/productUnit";
import {useModal} from "@/components/Modal";
import {useMessage} from "@/hooks/web/useMessage";
import UnitModal from "@/views/product/units/components/UnitModal.vue";
import {useI18n} from "vue-i18n";
export default defineComponent({
  name: 'ProductUnits',
  components: {TableAction, UnitModal, BasicTable },
  setup() {
    const { t } = useI18n();
    const [registerModal, {openModal}] = useModal();
    const { createMessage } = useMessage();
    const [registerTable, { reload, getSelectRows }] = useTable({
      title: t('product.unit.title'),
      api: getUnitList,
      columns: columns,
      rowSelection: {
        type: 'checkbox',
      },
      formConfig: {
        schemas: searchFormSchema,
        autoSubmitOnEnter: true,
      },
      striped: false,
      clickToRowSelect: false,
      bordered: true,
      showTableSetting: true,
      useSearchForm: true,
      rowKey: 'id',
      canResize: false,
      actionColumn: {
        width: 80,
        title: t('common.operating'),
        dataIndex: 'action',
        fixed: undefined,
      },
    });

    async function handleEdit(record: Recordable) {
      openModal(true, {
        record,
        isUpdate: true,
      });
    }
    function handleSuccess() {
      reload();
    }
    async function handleCreate() {
      openModal(true, {
        isUpdate: false,
      });
    }
    async function handleDelete(record: Recordable) {
      const result = await deleteBatchUnits([record.id]);
      if (result.code === 'P0008') {
        await reload();
      }
    }
    async function handleBatchDelete() {
      const data = getSelectRows();
      if (data.length === 0) {
        createMessage.warn(t('product.selectData'));
        return;
      }
      const ids = data.map((item) => item.id);
      const result = await deleteBatchUnits(ids);
      if (result.code === 'P0008') {
        await reload();
      }
    }

    return { t, registerTable, registerModal, handleCreate, handleEdit, handleDelete, handleBatchDelete, handleSuccess };
  }
});
</script>