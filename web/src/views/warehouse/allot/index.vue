<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate"> 新增</a-button>
        <a-button type="primary" @click="handleBatchDelete"> 批量删除</a-button>
        <a-button type="primary" @click="handleExport"> 导出</a-button>
        <a-button type="primary" @click="handleOnStatus(1)"> 审核</a-button>
        <a-button type="primary" @click="handleOnStatus(0)"> 反审核</a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
              :actions="[
              {
                icon: 'clarity:info-standard-line',
                tooltip: '查看单据详情',
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
        <template v-else-if="column.key === 'status'">
          <Tag :color="record.status === 1 ? 'green' : 'red'">
            {{ record.status === 1 ? '已审核' : '未审核' }}
          </Tag>
        </template>
      </template>
    </BasicTable>
    <AddEditAllotShipmentsModal ref="addEditModalRef" @cancel="handleCancel" />
    <ViewAllotShipmentsModal @register="receiptViewModal" @ok="handleOk"/>
  </div>
</template>
<div>
</div>

<script lang="ts">
import {defineComponent, ref} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {useMessage} from "@/hooks/web/useMessage";
import {columns, searchFormSchema} from "@/views//warehouse/allot/allotShipments.data";
import {useI18n} from "vue-i18n";
import {getAllotShipmentsPageList, deleteBatchAllotShipments, updateAllotShipmentsStatus, exportAllotShipments} from "@/api/warehouse/allotShipments";
import AddEditAllotShipmentsModal from "@/views/warehouse/allot/components/AddEditAllotShipmentsModal.vue"
import ViewAllotShipmentsModal from "@/views/warehouse/allot/components/ViewAllotShipmentsModal.vue";
import {Tag} from "ant-design-vue";
import {useModal} from "@/components/Modal";
export default defineComponent({
  name: 'AllotShipments',
  components: {Tag, TableAction, BasicTable, AddEditAllotShipmentsModal, ViewAllotShipmentsModal},
  setup() {
    const { t } = useI18n();
    const { createMessage } = useMessage();
    const addEditModalRef = ref(null);
    const [receiptViewModal, {openModal: openReceiptViewModal}] = useModal();
    const [registerTable, { reload, getSelectRows, getForm }] = useTable({
      title: '调拨出库列表',
      rowKey: 'id',
      api: getAllotShipmentsPageList,
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
      defaultExpandAllRows: false,
      actionColumn: {
        width: 80,
        title: '操作',
        dataIndex: 'action',
        fixed: undefined,
      },
    });

    function handleCreate() {
      addEditModalRef.value.openAddEditModal();
    }

    async function handleBatchDelete() {
      const data = getSelectRows();
      if (data.length === 0) {
        createMessage.warn('请选择一条数据');
        return;
      }
      const ids = data.map((item) => item.id);
      const result = await deleteBatchAllotShipments(ids);
      if (result.code === 'S0012') {
        await reload();
      }
    }

    function handleEdit(record: Recordable) {
      if (record.status === 1) {
        createMessage.warn('抱歉，只有未审核的单据才能编辑！');
        return;
      }
      addEditModalRef.value.openAddEditModal(record.id);
    }

    async function handleDelete(record: Recordable) {
      const result = await deleteBatchAllotShipments([record.id]);
      if (result.code === 'S0012') {
        await reload();
      }
    }

    async function handleSuccess() {
      await reload();
    }

    async function handleCancel() {
      await reload();
    }

    async function handleOk() {
      await reload();
    }

    function handleView(record: Recordable){
      openReceiptViewModal(true, {
        isUpdate: false,
        id: record.id,
      });
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

    async function handleOnStatus(newStatus: number) {
      const data = getSelectRows();
      if (data.length === 0) {
        createMessage.warn('请选择一条数据');
        return;
      }

      const ids = data.map((item) => item.id);
      const {code} = await updateAllotShipmentsStatus(ids, newStatus);
      if (code === "S0011") {
        await reload();
      }
    }

    async function handleExport() {
      const data = getForm().getFieldsValue();
      const file: any = await exportAllotShipments(data)
      if (file.size > 0) {
        const blob = new Blob([file]);
        const link = document.createElement("a");
        link.href = URL.createObjectURL(blob);
        const timestamp = getTimestamp(new Date());
        link.download = "调拨出库数据" + timestamp + ".xlsx";
        link.target = "_blank";
        link.click();
      }
    }


    return {
      t,
      receiptViewModal,
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