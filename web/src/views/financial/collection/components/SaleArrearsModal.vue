<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicTable @register="registerTable">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'receiptNumber'">
          <a @click="openReceipt(record.receiptNumber)"> {{record.receiptNumber}} </a>
        </template>
      </template>
    </BasicTable>
  </BasicModal>
  <ViewSaleRefundModal @register="handleSaleRefundModal" />
  <ViewSaleShipmentsModal @register="handleSaleShipmentsModal" />
</template>

<script lang="ts">
import {defineComponent, ref} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {useMessage} from "@/hooks/web/useMessage";
import {saleArrearsReceiptTableColumns, searchSaleArrearsFormSchema} from "@/views/financial/collection/collection.data";
import {BasicModal, useModal, useModalInner} from "@/components/Modal";
import {getArrearsPageList} from "@/api/financial/collection";
import ViewSaleRefundModal from "@/views/sales/refund/components/ViewSaleRefundModal.vue";
import ViewSaleShipmentsModal from "@/views/sales/shipments/components/ViewSaleShipmentsModal.vue";
export default defineComponent({
  name: 'SaleArrearsModal',
  components: {ViewSaleShipmentsModal, ViewSaleRefundModal, BasicModal, BasicTable, TableAction},
  emits: ['selectedRows'],
  setup(_, context) {
    const [handleSaleShipmentsModal, {openModal: openSaleShipmentsModal}] = useModal();
    const [handleSaleRefundModal, {openModal: openSaleRefundModal}] = useModal();
    const getTitle = ref('选择销售欠款单据');
    const { createMessage } = useMessage();
    const customerId = ref('');
    const [registerTable, { getSelectRows, reload}] = useTable({
      rowKey: 'id',
      columns: saleArrearsReceiptTableColumns,
      api: getArrearsPageList,
      beforeFetch: (data) => {
        data.customerId = customerId.value;
      },
      formConfig: {
        labelWidth: 110,
        schemas: searchSaleArrearsFormSchema,
      },
      rowSelection: {
        type: 'checkbox',
      },
      bordered: true,
      showTableSetting: false,
      canResize: false,
      tableSetting: { fullScreen: true },
      useSearchForm: true,
      clickToRowSelect: false,
      showIndexColumn: false,
    });

    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false, destroyOnClose: true, width: 1200});
      customerId.value = data.customerId
    });

    function openReceipt(openReceiptNumber: string) {
      if (openReceiptNumber.startsWith('XSCK')) {
        openSaleShipmentsModal(true, {
          receiptNumber: openReceiptNumber,
        });
      } else if (openReceiptNumber.startsWith('XSTH')) {
        openSaleRefundModal(true, {
          receiptNumber: openReceiptNumber,
        });
      }
    }

    function handleSubmit() {
      const rows = getSelectRows();
      if (rows.length === 0) {
        createMessage.error('请选择单据');
        return;
      }
      context.emit('handleReceiptSuccess', rows);
      closeModal();
    }

    return {
      registerTable,
      registerModal,
      getTitle,
      handleSubmit,
      openReceipt,
      handleSaleRefundModal,
      handleSaleShipmentsModal
    }
  }
})
</script>