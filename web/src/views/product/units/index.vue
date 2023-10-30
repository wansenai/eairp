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
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate"> 新增</a-button>
        <a-button type="primary" @click="handleBatchDelete"> 批量删除</a-button>
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

export default defineComponent({
  name: 'ProductUnits',
  components: {TableAction, UnitModal, BasicTable },
  setup() {
    const [registerModal, {openModal}] = useModal();
    const { createMessage } = useMessage();
    const [registerTable, { reload, getSelectRows }] = useTable({
      title: '商品计量单位列表',
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
      const result = await deleteBatchUnits([record.id]);
      if (result.code === 'P0008') {
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
      const result = await deleteBatchUnits(ids);
      if (result.code === 'P0008') {
        await reload();
      }
    }

    return { registerTable, registerModal, handleCreate, handleEdit, handleDelete, handleBatchDelete, handleSuccess };
  }
});
</script>