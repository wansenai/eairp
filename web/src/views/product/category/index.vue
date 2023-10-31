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
    <BasicTable @register="registerTable" @fetch-success="onFetchSuccess">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate"> 新增产品分类</a-button>
        <a-button type="primary" @click="handleBatchDelete"> 批量删除产品分类</a-button>
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
    <CategoryModal @register="registerModal" @success="handleSuccess"/>
  </div>
</template>

<script lang="ts">
import {defineComponent, nextTick} from 'vue';
import {useModal} from "@/components/Modal";
import CategoryModal from "@/views/product/category/components/CategoryModal.vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {getCategoryList, deleteCategory} from "@/api/product/productCategory";
import {columns} from "@/views/product/category/category.data";
import {useMessage} from "@/hooks/web/useMessage";

export default defineComponent({
  name: 'ProductCategory',
  components: {
    BasicTable, TableAction, CategoryModal,
  },
  setup() {
    const { createMessage } = useMessage();
    const [registerModal, {openModal}] = useModal();
    const [registerTable, {reload, expandAll, getSelectRows}] = useTable({
      title: '产品分类列表',
      api: getCategoryList,
      columns,
      formConfig: {
        labelWidth: 120,
      },
      isTreeTable: true,
      rowSelection: {
        type: 'checkbox',
      },
      titleHelpMessage: '产品分类列表可以添加多个产品分类和下级分类',
      // 产品分类不开启分页和行选中
      pagination: false,
      clickToRowSelect: false,
      striped: false,
      useSearchForm: false,
      showTableSetting: true,
      bordered: true,
      showIndexColumn: false,
      canResize: false,
      actionColumn: {
        width: 80,
        title: '操作',
        dataIndex: 'action',
        fixed: undefined,
      },
    });

    function handleSuccess() {
      reload();
    }

    function onFetchSuccess() {
      nextTick(expandAll);
    }

    async function handleEdit(record: Recordable) {
      openModal(true, {
        record,
        isUpdate: true,
      });
    }

    async function handleDelete(record: Recordable) {
      const result = await deleteCategory([record.id]);
      if (result.code === 'P0002') {
        await reload();
      }
    }

    async function handleCreate() {
      openModal(true, {
        isUpdate: false,
      });
    }

    async function handleBatchDelete() {
      // 通过getSelectRowKeys获取勾选的复选框，如果没有勾选复选框，就提示选择一条数据
      const data = getSelectRows();
      if (data.length === 0) {
        createMessage.warn('请选择一条数据');
        return;
      }
      const ids = data.map((item) => item.id);
      const result = await deleteCategory(ids);
      if (result.code === 'P0002') {
        await reload();
      }
    }

    return {
      registerModal,
      registerTable,
      handleEdit,
      handleDelete,
      onFetchSuccess,
      handleSuccess,
      handleCreate,
      handleBatchDelete,
    };
  }
})
</script>