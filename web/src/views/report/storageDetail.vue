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
    <ViewRefundModal @register="handleRetailRefundModal" />
    <ViewSaleRefundModal @register="handleSaleRefundModal" />
    <ViewPurchaseStorageModal @register="handlePurchaseStorageModal" />
  </div>
</template>
<div>
</div>

<script lang="ts">
import {defineComponent, ref} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {
  searchStorageDetailSchema,
  storageDetailStatisticsColumns
} from "@/views/report/report.data";
import {Tag} from "ant-design-vue";
import {getStorageDetail, exportStorageDetail} from "@/api/report/report";
import XEUtils from "xe-utils";
import {useModal} from "@/components/Modal";
import ViewRefundModal from "@/views/retail/refund/components/ViewRefundModal.vue";
import ViewSaleRefundModal from "@/views/sales/refund/components/ViewSaleRefundModal.vue";
import ViewPurchaseStorageModal from "@/views/purchase/storage/components/ViewStorageModal.vue";
import printJS from "print-js";
import {useMessage} from "@/hooks/web/useMessage";
import {getTimestamp} from "@/utils/dateUtil";

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
    const printTableData = ref<any[]>([]);
    const { createMessage } = useMessage();
    const [registerTable, { reload, getForm, getDataSource }] = useTable({
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

    function exportTable() {
      if (getDataSource() === undefined || getDataSource().length === 0) {
        createMessage.warn('当前查询条件下无数据可导出');
        return;
      }
      const data: any = getForm().getFieldsValue();
      exportStorageDetail(data).then(res => {
        const file: any = res;
        if (file.size > 0) {
          const blob = new Blob([file]);
          const link = document.createElement("a");
          link.href = URL.createObjectURL(blob);
          const timestamp = getTimestamp(new Date());
          link.download = "入库明细数据" + timestamp + ".xlsx";
          link.target = "_blank";
          link.click();
        }
      });
    }

    function primaryPrint() {
      printJS({
        documentTitle: "EAIRP (入库明细)",
        properties: storageDetailStatisticsColumns.map(item => {
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
      handleRetailRefundModal,
      handleSaleRefundModal,
      handlePurchaseStorageModal,
      exportTable,
      primaryPrint
    }
  }
})
</script>