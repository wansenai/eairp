<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate"> 新增</a-button>
        <a-button type="primary" @click="handleBatchDelete"> 批量删除</a-button>
        <a-button type="primary" @click="handleExport"> 导出</a-button>
        <a-button type="primary" @click=""> 审核</a-button>
        <a-button type="primary" @click=""> 反审核</a-button>
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
    <AddEditModal ref="addEditModalRef" @cancel="handleCancel"></AddEditModal>
  </div>
</template>
<div>
</div>

<script lang="ts">
import {defineComponent, ref} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {useMessage} from "@/hooks/web/useMessage";
import {columns, searchFormSchema} from "@/views/retail/shipments/shipments.data";
import {exportXlsx} from "@/api/basic/common";
import {useI18n} from "vue-i18n";
import AddEditModal from "@/views/retail/shipments/components/AddEditModal.vue"

export default defineComponent({
  name: 'Shipments',
  components: {TableAction, BasicTable, AddEditModal},
  setup() {
    const { t } = useI18n();
    const { createMessage } = useMessage();
    const addEditModalRef = ref(null);
    const [registerTable, { reload, getSelectRows }] = useTable({
      title: '零售出库列表',
      rowKey: 'id',
      columns: columns,
      rowSelection: {
        type: 'checkbox',
      },
      formConfig: {
        labelWidth: 110,
        schemas: searchFormSchema,
      },
      bordered: true,
      tableSetting: { fullScreen: true },
      useSearchForm: true,
      clickToRowSelect: false,
      showTableSetting: true,
      actionColumn: {
        width: 80,
        title: '操作',
        dataIndex: 'action',
        fixed: undefined,
      },
    });

    function handleCreate() {
      addEditModalRef.value.title = '新增-零售出库';
      addEditModalRef.value.openAddEditModal();
    }

    async function handleBatchDelete() {
      const data = getSelectRows();
      if (data.length === 0) {
        createMessage.warn('请选择一条数据');
        return;
      }
    }

    function handleEdit(record: Recordable) {
    }

    async function handleDelete(record: Recordable) {
    }

    async function handleSuccess() {
      await reload();
    }

    async function handleOnStatus(newStatus: number) {
      const data = getSelectRows();
      if (data.length === 0) {
        createMessage.warn('请选择一条数据');
        return;
      }
    }

    async function handleCancel() {
      await reload();
    }

    async function handleOk() {
      await reload();
    }

    function handleView(){

    }

    function handleBatchShipmentsInfo() {
      const data = getSelectRows();
      if (data.length === 0) {
        createMessage.warn('请选择一条数据');
        return;
      }
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
      const file = await exportXlsx("零售出库列表")
      const blob = new Blob([file]);
      const link = document.createElement("a");
      link.href = URL.createObjectURL(blob);
      const timestamp = getTimestamp(new Date());
      link.download = "零售出库数据" + timestamp + ".xlsx";
      link.target = "_blank";
      link.click();
    }


    return {
      t,
      addEditModalRef,
      registerTable,
      handleCreate,
      handleDelete,
      handleBatchDelete,
      handleEdit,
      handleSuccess,
      handleOnStatus,
      handleCancel,
      handleView,
      handleOk,
      handleExport,
    }
  }
})
</script>