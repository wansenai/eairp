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
        <a-button type="primary" @click="handleOnStatus(0)"> 批量启用</a-button>
        <a-button type="primary" @click="handleOnStatus(1)"> 批量禁用</a-button>
        <a-button type="primary" @click="handleImport"> 导入</a-button>
        <a-button type="primary" @click="handleExport"> 导出</a-button>
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
    <CustomerModal @register="registerModal" @success="handleSuccess" />
    <ImportFileModal ref="importModalRef" @cancel="handleCancel"/>
  </div>
</template>
<div>
</div>

<script lang="ts">
import {defineComponent, ref} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {useModal} from "@/components/Modal";
import {useMessage} from "@/hooks/web/useMessage";
import {columns, searchFormSchema} from "@/views/basic/customer/customer.data";
import {getCustomerList, deleteBatchCustomer, updateCustomerStatus} from "@/api/basic/customer";
import CustomerModal from "@/views/basic/customer/components/CustomerModal.vue";
import ImportFileModal from '@/components/Tools/ImportFileModal.vue';
import { exportXlsx } from '@/api/basic/common';

export default defineComponent({
  name: 'Customer',
  components: {TableAction, BasicTable, CustomerModal, ImportFileModal },
  setup() {
    const currentTime = ref(null);
    const timestamp = ref(null);
    const [registerModal, {openModal}] = useModal();
    const { createMessage } = useMessage();
    const importModalRef = ref(null);
    const [registerTable, { reload, getSelectRows }] = useTable({
      title: '客户信息列表',
      api: getCustomerList,
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
      showTableSetting: true,
      bordered: true,
      showIndexColumn: true,
      actionColumn: {
        width: 80,
        title: '操作',
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
        createMessage.warn('请选择一条数据');
        return;
      }
      const ids = data.map((item) => item.id);
      const result = await deleteBatchCustomer(ids)
      if (result.code === 'U0003') {
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
      const result = await deleteBatchCustomer([record.id])
      if (result.code === 'U0003') {
        await reload();
      }
    }

    async function handleSuccess() {
      reload();
    }

    async function handleOnStatus(newStatus: number) {
      // 获取选中行的id组成一个数组
      const data = getSelectRows();
      if (data.length === 0) {
        createMessage.warn('请选择一条数据');
        return;
      }
      const ids = data.map((item) => item.id);
      const result = await updateCustomerStatus(ids,newStatus )
      if (result.code === 'U0004') {
        await reload();
      }
    }

    async function handleCancel() {
      reload();
    }

    function handleImport() {
      const templateUrl  = 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/%E5%AE%A2%E6%88%B7%E4%BF%A1%E6%81%AF%E6%A8%A1%E6%9D%BF.xlsx'
      const templateName  = '客户信息Excel模板[下载]'
      importModalRef.value.initModal(templateUrl, templateName);
      importModalRef.value.title = "客户数据导入";
    }

    const getTimestamp = (date) => {
      return (
          date.getFullYear() * 10000000000 +
          (date.getMonth() + 1) * 100000000 +
          date.getDate() * 1000000 +
          date.getHours() * 10000 +
          date.getMinutes() * 100 +
          date.getSeconds()
      ).toString();
    };


    async function handleExport() {
      const file = await exportXlsx("客户信息列表")
      const blob = new Blob([file]);
      const link = document.createElement("a");
      link.href = URL.createObjectURL(blob);
      const timestamp = getTimestamp(new Date());
      link.download = "客户信息数据" + timestamp + ".xlsx";
      link.target = "_blank";
      link.click();
    }

    return {
      registerTable,
      registerModal,
      handleCreate,
      handleDelete,
      handleBatchDelete,
      handleEdit,
      handleSuccess,
      handleOnStatus,
      handleImport,
      handleExport,
      importModalRef,
      handleCancel,
      currentTime,
      timestamp,
    }
  }
})
</script>