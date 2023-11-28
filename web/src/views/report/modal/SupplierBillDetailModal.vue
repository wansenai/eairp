<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle">
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click=""> 导出</a-button>
        <a-button @click=""> 打印</a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'receiptNumber'">
          <a @click="openReceipt(record.receiptNumber)"> {{ record.receiptNumber }} </a>
        </template>
      </template>
    </BasicTable>
    <ViewPurchaseStorageModal @register="handlePurchaseStorageModal" />
    <ViewPurchaseRefundModal @register="handlePurchaseRefundModal" />
  </BasicModal>
</template>
<div>
</div>

<script lang="ts">
import {defineComponent, ref} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {supplierBillDetailColumns, searchSupplierBillDetailSchema} from "@/views/report/report.data";
import {Tag} from "ant-design-vue";
import {getSupplierBillDetail} from "@/api/report/report";
import XEUtils from "xe-utils";
import {BasicModal, useModal, useModalInner} from "@/components/Modal";
import ViewPurchaseStorageModal from "@/views/purchase/storage/components/ViewStorageModal.vue";
import ViewPurchaseRefundModal from "@/views/purchase/refund/components/ViewRefundModal.vue";

export default defineComponent({
  name: 'SupplierBillDetailModal',
  components: {
    ViewPurchaseRefundModal,
    ViewPurchaseStorageModal,
    BasicModal,
    Tag, TableAction, BasicTable},
  setup() {
    const getTitle = ref('供应商欠款详情');
    const supplierId = ref('');
    const [handlePurchaseStorageModal, {openModal: openPurchaseStorageModal}] = useModal();
    const [handlePurchaseRefundModal, {openModal: openPurchaseRefundModal}] = useModal();
    const [registerTable, { reload }] = useTable({
      api: getSupplierBillDetail,
      beforeFetch: (data) => {
        data.supplierId = supplierId.value;
      },
      columns: supplierBillDetailColumns,
      formConfig: {
        labelWidth: 110,
        schemas: searchSupplierBillDetailSchema,
        autoSubmitOnEnter: true,
      },
      bordered: true,
      useSearchForm: true,
      showTableSetting: true,
      striped: true,
      canResize: false,
      showIndexColumn: true,
      showSummary: true,
      summaryFunc: handleSummary,
    });

    const [registerModal, {setModalProps}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false, destroyOnClose: true, width: 1100});
      supplierId.value = data.supplierId
    });

    function handleSummary(tableData: Recordable[]) {
      const thisReceiptArrears = tableData.reduce((prev, next) => prev + next.thisReceiptArrears, 0);
      const prepaidArrears = tableData.reduce((prev, next) => prev + next.prepaidArrears, 0);
      const paymentArrears = tableData.reduce((prev, next) => prev + next.paymentArrears, 0);
      return [
        {
          _index: '合计',
          thisReceiptArrears:`￥${XEUtils.commafy(XEUtils.toNumber(thisReceiptArrears), { digits: 2 })}`,
          prepaidArrears:`￥${XEUtils.commafy(XEUtils.toNumber(prepaidArrears), { digits: 2 })}`,
          paymentArrears:`￥${XEUtils.commafy(XEUtils.toNumber(paymentArrears), { digits: 2 })}`,
        },
      ];
    }
    async function handleSuccess() {
      reload();
    }

    async function handleCancel() {
      reload();
    }

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

    return {
      getTitle,
      registerModal,
      openReceipt,
      registerTable,
      handleSuccess,
      handleCancel,
      handlePurchaseStorageModal,
      handlePurchaseRefundModal,
    }
  }
})
</script>