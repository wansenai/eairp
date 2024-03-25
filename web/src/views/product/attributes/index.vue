<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate" v-text="t('product.attribute.add')" />
        <a-button type="primary" @click="handleBatchDelete" v-text="t('product.attribute.batchDelete')" />
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
              :actions="[
              {
                icon: 'clarity:note-edit-line',
                tooltip: t('product.attribute.table.edit'),
                onClick: handleEdit.bind(null, record),
              },
              {
                icon: 'ant-design:delete-outlined',
                color: 'error',
                tooltip: t('product.attribute.table.delete'),
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
    <AttributeModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
import { defineComponent } from 'vue';
import {BasicTable, TableAction, useTable} from '/@/components/Table';
import {columns, searchFormSchema} from "@/views/product/attributes/attributes.data";
import {getAttributeList, deleteBatchAttribute} from "@/api/product/productAttribute";
import {useModal} from "@/components/Modal";
import {useMessage} from "@/hooks/web/useMessage";
import AttributeModal from "@/views/product/attributes/components/AttributeModal.vue";
import {useI18n} from "vue-i18n";

export default defineComponent({
  name: 'ProductAttributes',
  components: {TableAction, AttributeModal, BasicTable },
  setup() {
    const { t } = useI18n();
    const [registerModal, {openModal}] = useModal();
    const { createMessage } = useMessage();
    const [registerTable, { reload, getSelectRows }] = useTable({
      title: t('product.attribute.title'),
      api: getAttributeList,
      columns: columns,
      rowSelection: {
        type: 'checkbox',
      },
      formConfig: {
        schemas: searchFormSchema,
        autoSubmitOnEnter: true,
      },
      striped: true,
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
      const result = await deleteBatchAttribute([record.id]);
      if (result.code === 'P0005') {
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
      const result = await deleteBatchAttribute(ids);
      if (result.code === 'P0005') {
        await reload();
      }
    }

    return { t, registerTable, registerModal, handleCreate, handleEdit, handleDelete, handleBatchDelete, handleSuccess };
  }
});
</script>