<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate"> 新增产品属性</a-button>
        <a-button type="primary" @click="handleBatchDelete"> 批量删除产品属性</a-button>
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

export default defineComponent({
  name: 'ProductAttributes',
  components: {TableAction, AttributeModal, BasicTable },
  setup() {
    const [registerModal, {openModal}] = useModal();
    const { createMessage } = useMessage();
    const [registerTable, { reload, getSelectRows }] = useTable({
      title: '商品多属性列表',
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
        title: '操作',
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
        createMessage.warn('请选择一条数据');
        return;
      }
      const ids = data.map((item) => item.id);
      const result = await deleteBatchAttribute(ids);
      if (result.code === 'P0005') {
        await reload();
      }
    }

    return { registerTable, registerModal, handleCreate, handleEdit, handleDelete, handleBatchDelete, handleSuccess };
  }
});
</script>