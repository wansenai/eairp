<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button @click="exportTable" v-text="t('reports.storageDetail.export')"/>
        <a-button @click="primaryPrint" type="primary"  v-text="t('reports.storageDetail.regularPrint')"/>
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
import {useI18n} from "vue-i18n";

export default defineComponent({
  name: 'StorageDetailStatistics',
  components: {
    ViewPurchaseStorageModal,
    ViewSaleRefundModal,
    ViewRefundModal,
    Tag, TableAction, BasicTable},
  setup() {
    const { t } = useI18n();
    const [handleRetailRefundModal, {openModal: openRetailRefundModal}] = useModal();
    const [handleSaleRefundModal, {openModal: openSaleRefundModal}] = useModal();
    const [handlePurchaseStorageModal, {openModal: openPurchaseStorageModal}] = useModal();
    const printTableData = ref<any[]>([]);
    const { createMessage } = useMessage();
    const printProductNumber = ref(0);
    const printAmount = ref(0);
    const printTaxAmount = ref(0);
    const [registerTable, { reload, getForm, getDataSource }] = useTable({
      title: t('reports.storageDetail.title'),
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
      printProductNumber.value = productNumber;
      printAmount.value = amount;
      printTaxAmount.value = taxAmount;
      printTableData.value = tableData;
      return [
        {
          _index: t('reports.storageDetail.table.total'),
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
        createMessage.warn(t('reports.storageDetail.notice'));
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
          link.download = t('reports.storageDetail.data') + timestamp + ".xlsx";
          link.target = "_blank";
          link.click();
        }
      });
    }

    function primaryPrint() {
      printTableData.value.push({
        productNumber: printProductNumber.value,
        amount: `￥${XEUtils.commafy(XEUtils.toNumber(printAmount.value), { digits: 2 })}`,
        taxAmount: `￥${XEUtils.commafy(XEUtils.toNumber(printTaxAmount.value), { digits: 2 })}`,
        receiptNumber: t('reports.storageDetail.table.total'),
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
      printJS({
        documentTitle: "EAIRP" + t('reports.storageDetail.detail'),
        properties: storageDetailStatisticsColumns.map(item => {
          return { field: item.dataIndex, displayName: item.title }
        }),
        printable: printTableData.value,
        gridHeaderStyle: 'border: 1px solid #ddd; font-size: 12px; text-align: center; padding: 8px;',
        gridStyle: 'border: 1px solid #ddd; font-size: 12px; text-align: center; padding: 8px;',
        type: 'json',
      });
      printTableData.value.pop();
    }

    return {
      t,
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