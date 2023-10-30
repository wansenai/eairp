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
        <a-button type="primary" @click="handleCreate"> 新增部门</a-button>
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
    <DeptModal @register="registerModal" @success="handleSuccess"/>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import {BasicTable, useTable, TableAction} from '/@/components/Table';
import {getDeptList} from '/@/api/sys/dept'
import {useModal} from '/@/components/Modal'
import DeptModal from '@/views/sys/department/components/DeptModal.vue';
import {columns, searchFormSchema} from '@/views/sys/department/dept.data';
import { deleteDept } from "@/api/sys/dept";

export default defineComponent({
  name: 'DeptManagement',
  components: { BasicTable, DeptModal, TableAction},
  setup() {
    const [registerModal, { openModal }] = useModal();
    const [registerTable, { reload }] = useTable({
      title: '部门列表',
      api: getDeptList,
      columns,
      formConfig: {
        labelWidth: 120,
        schemas: searchFormSchema,
      },
      pagination: false,
      striped: false,
      useSearchForm: true,
      showTableSetting: true,
      bordered: true,
      showIndexColumn: false,
      canResize: false,
      actionColumn: {
        width: 80,
        title: '操作',
        dataIndex: 'action',
        // slots: { customRender: 'action' },
        fixed: undefined,
      },
    });

    function handleCreate() {
      openModal(true, {
        isUpdate: false,
      });
    }

    function handleEdit(record: Recordable) {
      openModal(true, {
        record,
        isUpdate: true,
      });
    }

    async function handleDelete(record: Recordable) {
      const result = await deleteDept(record.id);
      if (result.code === 'A0011') {
        await reload();
      }
    }

    function handleSuccess() {
      reload();
    }

    return {
      registerTable,
      registerModal,
      handleCreate,
      handleSuccess,
      handleEdit,
      handleDelete,
    };
  },
});
</script>

<style scoped lang="less">

</style>
