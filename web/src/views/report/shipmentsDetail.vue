<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button @click="exportTable">导出</a-button>
        <a-button @click="primaryPrint" type="primary">普通打印</a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'receiptNumber'">
          <a @click="openReceipt(record.receiptNumber)"> {{record.receiptNumber}} </a>
        </template>
      </template>
    </BasicTable>
    <ViewShipmentModal @register="handleRetailShipmentModal" />
    <ViewSaleShipmentsModal @register="handleSaleShipmentsModal" />
    <ViewPurchaseRefundModal @register="handlePurchaseRefundModal" />
  </div>
</template>
<div>
</div>

<script lang="ts">
import {defineComponent, ref} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {
  searchShipmentsDetailSchema,
  shipmentsDetailStatisticsColumns
} from "@/views/report/report.data";
import {Tag} from "ant-design-vue";
import {getShipmentsDetail} from "@/api/report/report";
import XEUtils from "xe-utils";
import {useModal} from "@/components/Modal";
import ViewShipmentModal from "@/views/retail/shipments/components/ViewShipmentModal.vue";
import ViewSaleShipmentsModal from "@/views/sales/shipments/components/ViewSaleShipmentsModal.vue";
import ViewPurchaseRefundModal from "@/views/purchase/refund/components/ViewRefundModal.vue";
import printJS from "print-js";

export default defineComponent({
  name: 'ShipmentsDetailStatistics',
  components: {ViewPurchaseRefundModal, ViewSaleShipmentsModal, ViewShipmentModal, Tag, TableAction, BasicTable},
  setup() {
    const [handleSaleShipmentsModal, {openModal: openSaleShipmentsModal}] = useModal();
    const [handlePurchaseRefundModal, {openModal: openPurchaseRefundModal}] = useModal();
    const [handleRetailShipmentModal, {openModal: openRetailShipmentModal}] = useModal();
    const printTableData = ref<any[]>([]);
    const [registerTable, { reload }] = useTable({
      title: '出库明细报表',
      api: getShipmentsDetail,
      columns: shipmentsDetailStatisticsColumns,
      formConfig: {
        labelWidth: 110,
        schemas: searchShipmentsDetailSchema,
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
      printTableData.value = tableData;
      printTableData.value.push({
        productNumber: productNumber,
        amount: `￥${XEUtils.commafy(XEUtils.toNumber(amount), { digits: 2 })}`,
        taxAmount: `￥${XEUtils.commafy(XEUtils.toNumber(taxAmount), { digits: 2 })}`,
        receiptNumber: '合计',
        type: '',
        name: '',
        productBarcode: '',
        warehouseName: '',
        productName: '',
        productStandard: '',
        productModel: '',
        productUnit: '',
        unitPrice: '',
        taxRate: '',
        createTime: ''
      });
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
      if (openReceiptNumber.startsWith('XSCK')) {
        openSaleShipmentsModal(true, {
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
      }
    }

    function exportTable() {
    }

    function primaryPrint() {
      printJS({
        documentTitle: "EAIRP (出库明细)",
        properties: shipmentsDetailStatisticsColumns.map(item => {
          return { field: item.dataIndex, displayName: item.title }
        }),
        printable: printTableData.value,
        gridHeaderStyle: 'border: 1px solid #ddd; font-size: 12px; text-align: center; padding: 8px;',
        gridStyle: 'border: 1px solid #ddd; font-size: 12px; text-align: center; padding: 8px;',
        type: 'json',
      });
    }

    return {
      openReceipt,
      registerTable,
      handleSuccess,
      handleCancel,
      handleRetailShipmentModal,
      handleSaleShipmentsModal,
      handlePurchaseRefundModal,
      exportTable,
      primaryPrint
    }
  }
})
</script>