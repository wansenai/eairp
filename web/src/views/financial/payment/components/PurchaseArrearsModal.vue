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
  <ViewPurchaseStorageModal @register="handlePurchaseStorageModal" />
  <ViewPurchaseRefundModal @register="handlePurchaseRefundModal" />
</template>

<script lang="ts">
import {defineComponent, ref} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {useMessage} from "@/hooks/web/useMessage";
import {purchaseArrearsReceiptTableColumns, searchPurchaseArrearsFormSchema} from "@/views/financial/payment/payment.data";
import {BasicModal, useModal, useModalInner} from "@/components/Modal";
import {getArrearsPageList} from "@/api/financial/payment";
import ViewPurchaseRefundModal from "@/views/purchase/refund/components/ViewRefundModal.vue";
import ViewPurchaseStorageModal from "@/views/purchase/storage/components/ViewStorageModal.vue";
import {useI18n} from "vue-i18n";
export default defineComponent({
  name: 'PurchaseArrearsModal',
  components: {ViewPurchaseStorageModal, ViewPurchaseRefundModal, BasicModal, BasicTable, TableAction},
  emits: ['handleReceiptSuccess', 'register'],
  setup(_, context) {
    const { t } = useI18n();
    const [handlePurchaseStorageModal, {openModal: openPurchaseStorageModal}] = useModal();
    const [handlePurchaseRefundModal, {openModal: openPurchaseRefundModal}] = useModal();
    const getTitle = ref(t('financial.payment.selectPurchaseArrearsReceipt'));
    const { createMessage } = useMessage();
    const supplierId = ref('');
    const [registerTable, { getSelectRows}] = useTable({
      rowKey: 'id',
      columns: purchaseArrearsReceiptTableColumns,
      api: getArrearsPageList,
      beforeFetch: (data) => {
        data.supplierId = supplierId.value;
      },
      formConfig: {
        labelWidth: 110,
        schemas: searchPurchaseArrearsFormSchema,
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
      supplierId.value = data.supplierId
    });

    function openReceipt(openReceiptNumber: string) {
      if (openReceiptNumber.startsWith('CGRK')) {
        openPurchaseStorageModal(true, {
          receiptNumber: openReceiptNumber,
        });
      } else if (openReceiptNumber.startsWith('CGTH')) {
        openPurchaseRefundModal(true, {
          receiptNumber: openReceiptNumber,
        });
      }
    }

    function handleSubmit() {
      const rows = getSelectRows();
      if (rows.length === 0) {
        createMessage.error(t('financial.payment.form.noticeOne'));
        return;
      }
      context.emit('handleReceiptSuccess', rows);
      closeModal();
    }

    return {
      t,
      registerTable,
      registerModal,
      getTitle,
      handleSubmit,
      openReceipt,
      handlePurchaseStorageModal,
      handlePurchaseRefundModal
    }
  }
})
</script>