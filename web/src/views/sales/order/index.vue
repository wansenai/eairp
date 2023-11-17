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
        <template v-else-if="column.key === 'status'">
          <Tag :color="record.status === 1 ? 'green' : (record.status === 2 ? 'yellow' : (record.status === 3 ? 'blue' : 'red'))">
            {{ record.status === 1 ? '已审核' : (record.status === 2 ? '部分销售' : (record.status === 3 ? '完成销售' : '未审核')) }}
          </Tag>
        </template>
      </template>
    </BasicTable>
    <AddEditModal ref="addEditModalRef" @cancel="handleCancel"/>
    <ViewOrderModal @register="viewOrderReceiptModal"/>
  </div>
</template>
<div>
</div>

<script lang="ts">
import {defineComponent, ref} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {useMessage} from "@/hooks/web/useMessage";
import {columns, searchFormSchema} from "@/views/sales/order/sales.data";
import {exportXlsx} from "@/api/basic/common";
import {useI18n} from "vue-i18n";
import {Tag} from "ant-design-vue";
import {getSaleOrderPageList, updateSaleOrderStatus, deleteSaleOrder} from "@/api/sale/order";
import AddEditModal from "@/views/sales/order/components/AddEditModal.vue";
import ViewOrderModal from "@/views/sales/order/components/ViewSaleOrderModal.vue";
import {useModal} from "@/components/Modal";
export default defineComponent({
  name: 'SaleOrderModal',
  components: {AddEditModal, Tag, TableAction, BasicTable, ViewOrderModal},
  setup() {
    const { t } = useI18n();
    const addEditModalRef = ref(null);
    const { createMessage } = useMessage();
    const [viewOrderReceiptModal, {openModal: openViewOrderReceiptModal}] = useModal();
    const [registerTable, { reload, getSelectRows }] = useTable({
      title: '销售订单列表',
      rowKey: 'id',
      api: getSaleOrderPageList,
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
      addEditModalRef.value?.openAddEditModal();
    }

    async function handleBatchDelete() {
      const data = getSelectRows();
      if (data.length === 0) {
        createMessage.warn('请选择一条数据');
        return;
      }
      const result = await deleteSaleOrder(data.map((item) => item.id));
      if (result.code === 'S0003') {
        createMessage.success('删除成功');
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
      const result = await deleteSaleOrder([record.id]);
      if (result.code === 'S0003') {
        createMessage.success('删除成功');
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
      openViewOrderReceiptModal(true, {
        receiptNumber: record.receiptNumber
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
      const {code} = await updateSaleOrderStatus(ids, newStatus);
      if (code === 'S0002') {
        createMessage.success('修改状态成功');
        await reload();
      }
    }

    async function handleExport() {
      const file = await exportXlsx("销售订单列表")
      const blob = new Blob([file]);
      const link = document.createElement("a");
      link.href = URL.createObjectURL(blob);
      const timestamp = getTimestamp(new Date());
      link.download = "销售订单数据" + timestamp + ".xlsx";
      link.target = "_blank";
      link.click();
    }


    return {
      t,
      addEditModalRef,
      viewOrderReceiptModal,
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