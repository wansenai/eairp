<template>
  <BasicModal v-bind="$attrs" @register="registerModal">
    <BasicTable @register="registerTable">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'receiptNumber'">
          <a @click="openReceipt(record.receiptNumber)"> {{record.receiptNumber}} </a>
        </template>
      </template>
    </BasicTable>
  </BasicModal>
  <ViewShipmentModal @register="handleRetailShipmentModal" />
  <ViewRefundModal @register="handleRetailRefundModal" />
  <ViewSaleOrderModal @register="handleSaleOrderModal" />
  <ViewSaleShipmentsModal @register="handleSaleShipmentsModal" />
  <ViewSaleRefundModal @register="handleSaleRefundModal" />
  <ViewPurchaseOrderModal @register="handlePurchaseOrderModal" />
  <ViewPurchaseStorageModal @register="handlePurchaseStorageModal" />
  <ViewPurchaseRefundModal @register="handlePurchaseRefundModal" />
</template>
<div>
</div>

<script lang="ts">
import {defineComponent, ref} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {BasicModal, useModal, useModalInner} from "@/components/Modal";
import {accountFlowColumns, searchAccountSchema} from "@/views/report/report.data";
import {Tag} from "ant-design-vue";
import {getAccountFlow} from "@/api/report/report";
import ViewSaleOrderModal from "@/views/sales/order/components/ViewSaleOrderModal.vue";
import ViewPurchaseOrderModal from "@/views/purchase/order/components/ViewOrderModal.vue";
import ViewShipmentModal from "@/views/retail/shipments/components/ViewShipmentModal.vue";
import ViewSaleShipmentsModal from "@/views/sales/shipments/components/ViewSaleShipmentsModal.vue";
import ViewPurchaseStorageModal from "@/views/purchase/storage/components/ViewStorageModal.vue";
import ViewPurchaseRefundModal from "@/views/purchase/refund/components/ViewRefundModal.vue";
import ViewRefundModal from "@/views/retail/refund/components/ViewRefundModal.vue";
import ViewSaleRefundModal from "@/views/sales/refund/components/ViewSaleRefundModal.vue";

export default defineComponent({
  name: 'AccountFlowModal',
  components: {
    ViewSaleRefundModal,
    ViewRefundModal,
    ViewPurchaseRefundModal,
    ViewPurchaseStorageModal,
    ViewSaleShipmentsModal,
    ViewShipmentModal, ViewPurchaseOrderModal, ViewSaleOrderModal, BasicModal, Tag, TableAction, BasicTable},
  setup() {
    const accountId = ref();
    const [handleRetailShipmentModal, {openModal: openRetailShipmentModal}] = useModal();
    const [handleRetailRefundModal, {openModal: openRetailRefundModal}] = useModal();
    const [handleSaleOrderModal, {openModal: openSaleOrderModal}] = useModal();
    const [handleSaleShipmentsModal, {openModal: openSaleShipmentsModal}] = useModal();
    const [handleSaleRefundModal, {openModal: openSaleRefundModal}] = useModal();
    const [handlePurchaseOrderModal, {openModal: openPurchaseOrderModal}] = useModal();
    const [handlePurchaseStorageModal, {openModal: openPurchaseStorageModal}] = useModal();
    const [handlePurchaseRefundModal, {openModal: openPurchaseRefundModal}] = useModal();
    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({
        confirmLoading: false,
        destroyOnClose: true,
        width: 1000,
        title: '查看账户流水',
        showOkBtn: false,
        helpMessage: '账户的流水统计是由前往后，最后面的余额是最终余额',
      });
      accountId.value = data.accountId;
    });

    const [registerTable, {reload}] = useTable({
      api: getAccountFlow,
      rowKey: 'id',
      columns: accountFlowColumns,
      useSearchForm: false,
      bordered: true,
      canResize: false,
      formConfig: {
        autoSubmitOnEnter: true,
      },
      striped: true,
      showIndexColumn: false,

      beforeFetch: (data) => {
        data.accountId = accountId.value;
      },
    });

    function openReceipt(openReceiptNumber: string) {
      if (openReceiptNumber.startsWith('XSD')) {
        openSaleOrderModal(true, {
          receiptNumber: openReceiptNumber,
        });
      } else if (openReceiptNumber.startsWith('XSCK')) {
        openSaleShipmentsModal(true, {
          receiptNumber: openReceiptNumber,
        });
      } else if (openReceiptNumber.startsWith('XSTH')) {
        openSaleRefundModal(true, {
          receiptNumber: openReceiptNumber,
        });
      } else if (openReceiptNumber.startsWith('CGD')) {
        openPurchaseOrderModal(true, {
          receiptNumber: openReceiptNumber,
        });
      } else if (openReceiptNumber.startsWith('CGRK')) {
        openPurchaseStorageModal(true, {
          receiptNumber: openReceiptNumber,
        });
      } else if (openReceiptNumber.startsWith('CGTH')) {
        openPurchaseRefundModal(true, {
          receiptNumber: openReceiptNumber,
        });
      } else if (openReceiptNumber.startsWith('LSCK')) {
        openRetailShipmentModal(true, {
          receiptNumber: openReceiptNumber,
        });
      } else if (openReceiptNumber.startsWith('LSTH')) {
        openRetailRefundModal(true, {
          receiptNumber: openReceiptNumber,
        });
      }
    }

    async function handleSuccess() {
      reload();
    }

    async function handleCancel() {
      reload();
    }

    return {
      registerTable,
      registerModal,
      handleSuccess,
      handleCancel,
      openReceipt,
      handleRetailShipmentModal,
      handleRetailRefundModal,
      handleSaleOrderModal,
      handleSaleShipmentsModal,
      handleSaleRefundModal,
      handlePurchaseOrderModal,
      handlePurchaseStorageModal,
      handlePurchaseRefundModal
    }
  }
})
</script>