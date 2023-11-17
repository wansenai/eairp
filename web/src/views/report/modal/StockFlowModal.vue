<template>
    <BasicModal v-bind="$attrs" @register="registerModal">
      <BasicTable @register="registerTable">
        <template #toolbar>
          <a-button type="primary" @click=""> 导出</a-button>
        </template>
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
import {searchStockFlowSchema, stockFlowColumns} from "@/views/report/report.data";
import {Tag} from "ant-design-vue";
import {getProductStockFlow} from "@/api/report/report";
import ViewSaleOrderModal from "@/views/sales/order/components/ViewSaleOrderModal.vue";
import ViewPurchaseOrderModal from "@/views/purchase/order/components/ViewOrderModal.vue";
import ViewShipmentModal from "@/views/retail/shipments/components/ViewShipmentModal.vue";
import ViewSaleShipmentsModal from "@/views/sales/shipments/components/ViewSaleShipmentsModal.vue";
import ViewPurchaseStorageModal from "@/views/purchase/storage/components/ViewStorageModal.vue";
import ViewPurchaseRefundModal from "@/views/purchase/refund/components/ViewRefundModal.vue";
import ViewRefundModal from "@/views/retail/refund/components/ViewRefundModal.vue";
import ViewSaleRefundModal from "@/views/sales/refund/components/ViewSaleRefundModal.vue";

export default defineComponent({
  name: 'productStock',
  components: {
    ViewSaleRefundModal,
    ViewRefundModal,
    ViewPurchaseRefundModal,
    ViewPurchaseStorageModal,
    ViewSaleShipmentsModal,
    ViewShipmentModal, ViewPurchaseOrderModal, ViewSaleOrderModal, BasicModal, Tag, TableAction, BasicTable},
  setup() {
    const productId = ref();
    const warehouseId = ref();
    const productBarcode = ref();
    const [handleRetailShipmentModal, {openModal: openRetailShipmentModal}] = useModal();
    const [handleRetailRefundModal, {openModal: openRetailRefundModal}] = useModal();
    const [handleSaleOrderModal, {openModal: openSaleOrderModal}] = useModal();
    const [handleSaleShipmentsModal, {openModal: openSaleShipmentsModal}] = useModal();
    const [handleSaleRefundModal, {openModal: openSaleRefundModal}] = useModal();
    const [handlePurchaseOrderModal, {openModal: openPurchaseOrderModal}] = useModal();
    const [handlePurchaseStorageModal, {openModal: openPurchaseStorageModal}] = useModal();
    const [handlePurchaseRefundModal, {openModal: openPurchaseRefundModal}] = useModal();
    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false, destroyOnClose: true, width: 1000, title: '查看商品库存流水'});
      productId.value = data.productId;
      warehouseId.value = data.warehouseId;
      productBarcode.value = data.productBarcode;
    });

    const [registerTable, {reload}] = useTable({
      api: getProductStockFlow,
      rowKey: 'id',
      columns: stockFlowColumns,
      formConfig: {
        labelWidth: 110,
        schemas: searchStockFlowSchema,
        autoSubmitOnEnter: true,
      },
      useSearchForm: true,
      bordered: true,
      showTableSetting: true,
      striped: true,
      showIndexColumn: false,
      beforeFetch: (data) => {
        data.productId = productId.value;
        data.warehouseId = warehouseId.value;
        data.productBarcode = productBarcode.value;
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
      closeModal();
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