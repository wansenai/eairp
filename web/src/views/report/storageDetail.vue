<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click=""> 导出</a-button>
        <a-button @click=""> 打印</a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'receiptNumber'">
          <a @click="openReceipt(record.receiptNumber)"> {{record.receiptNumber}} </a>
        </template>
      </template>
    </BasicTable>
    <ViewRefundModal @register="handleRetailRefundModal" />
    <ViewSaleRefundModal @register="handleSaleRefundModal" />
    <ViewPurchaseStorageModal @register="handlePurchaseStorageModal" />
  </div>
</template>
<div>
</div>

<script lang="ts">
import {defineComponent} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {searchStorageDetailSchema, storageDetailStatisticsColumns} from "@/views/report/report.data";
import {Tag} from "ant-design-vue";
import {getStorageDetail} from "@/api/report/report";
import XEUtils from "xe-utils";
import {useModal} from "@/components/Modal";
import ViewRefundModal from "@/views/retail/refund/components/ViewRefundModal.vue";
import ViewSaleRefundModal from "@/views/sales/refund/components/ViewSaleRefundModal.vue";
import ViewPurchaseStorageModal from "@/views/purchase/storage/components/ViewStorageModal.vue";

export default defineComponent({
  name: 'StorageDetailStatistics',
  components: {
    ViewPurchaseStorageModal,
    ViewSaleRefundModal,
    ViewRefundModal,
    Tag, TableAction, BasicTable},
  setup() {
    const [handleRetailRefundModal, {openModal: openRetailRefundModal}] = useModal();
    const [handleSaleRefundModal, {openModal: openSaleRefundModal}] = useModal();
    const [handlePurchaseStorageModal, {openModal: openPurchaseStorageModal}] = useModal();
    const [registerTable, { reload }] = useTable({
      title: '入库明细报表',
      api: getStorageDetail,
      columns: storageDetailStatisticsColumns,
      formConfig: {
        labelWidth: 110,
        schemas: searchStorageDetailSchema,
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

    function handleSummary(tableData: Recordable[]) {
      const productNumber = tableData.reduce((prev, next) => prev + next.productNumber, 0);
      const amount = tableData.reduce((prev, next) => prev + next.amount, 0);
      const taxAmount = tableData.reduce((prev, next) => prev + next.taxAmount, 0);
      return [
        {
          _index: '合计',
          productNumber: productNumber,
          amount: `￥${XEUtils.commafy(XEUtils.toNumber(amount), { digits: 2 })}`,
          taxAmount: `￥${XEUtils.commafy(XEUtils.toNumber(taxAmount), { digits: 2 })}`
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
      if (openReceiptNumber.startsWith('LSTH')) {
        openRetailRefundModal(true, {
          receiptNumber: openReceiptNumber,
        });
      } else if (openReceiptNumber.startsWith('XSTH')) {
        openSaleRefundModal(true, {
          receiptNumber: openReceiptNumber,
        });
      } else if (openReceiptNumber.startsWith('CGRK')) {
        openPurchaseStorageModal(true, {
          receiptNumber: openReceiptNumber,
        });
      }
    }

    return {
      openReceipt,
      registerTable,
      handleSuccess,
      handleCancel,
      handleRetailRefundModal,
      handleSaleRefundModal,
      handlePurchaseStorageModal,
    }
  }
})
</script>