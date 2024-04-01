<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle">
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="exportTable" v-text="t('reports.customerBill.export')"/>
        <a-button @click="primaryPrint" v-text="t('reports.customerBill.regularPrint')"/>
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
import {
  supplierBillDetailColumns,
  searchSupplierBillDetailSchema,
} from "@/views/report/report.data";
import {Tag} from "ant-design-vue";
import {getSupplierBillDetail, exportSupplierBillDetail} from "@/api/report/report";
import XEUtils from "xe-utils";
import {BasicModal, useModal, useModalInner} from "@/components/Modal";
import ViewPurchaseStorageModal from "@/views/purchase/storage/components/ViewStorageModal.vue";
import ViewPurchaseRefundModal from "@/views/purchase/refund/components/ViewRefundModal.vue";
import {useMessage} from "@/hooks/web/useMessage";
import {getTimestamp} from "@/utils/dateUtil";
import printJS from "print-js";
import {useI18n} from "vue-i18n";

export default defineComponent({
  name: 'SupplierBillDetailModal',
  components: {
    ViewPurchaseRefundModal,
    ViewPurchaseStorageModal,
    BasicModal,
    Tag, TableAction, BasicTable},
  setup() {
    const { t } = useI18n();
    const { createMessage } = useMessage();
    const printTableData = ref<any[]>([]);
    const getTitle = ref(t('reports.other.supplierArrearsDetail'));
    const supplierId = ref('');
    const [handlePurchaseStorageModal, {openModal: openPurchaseStorageModal}] = useModal();
    const [handlePurchaseRefundModal, {openModal: openPurchaseRefundModal}] = useModal();
    const [registerTable, { reload, getForm, getDataSource }] = useTable({
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
          _index: t('reports.customerBill.table.total'),
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

    function exportTable() {
      if (getDataSource() === undefined || getDataSource().length === 0) {
        createMessage.warn(t('reports.customerBill.notice'));
        return;
      }
      const data: any = getForm().getFieldsValue();
      data.supplierId = supplierId.value;
      exportSupplierBillDetail(data).then(res => {
        const file: any = res;
        if (file.size > 0) {
          const blob = new Blob([file]);
          const link = document.createElement("a");
          link.href = URL.createObjectURL(blob);
          const timestamp = getTimestamp(new Date());
          link.download = t('reports.other.supplierArrearsData') + timestamp + ".xlsx";
          link.target = "_blank";
          link.click();
        }
      });
    }

    function primaryPrint() {
      printTableData.value = getDataSource();
      printTableData.value.push({
        receiptNumber: t('reports.customerBill.table.total'),
        supplierName: '',
        productInfo: '',
        receiptDate: '',
        operator: '',
        thisReceiptArrears:  `￥${XEUtils.commafy(XEUtils.toNumber(getDataSource().reduce((prev, next) => prev + next.thisReceiptArrears, 0)), { digits: 2 })}`,
        prepaidArrears: `￥${XEUtils.commafy(XEUtils.toNumber(getDataSource().reduce((prev, next) => prev + next.prepaidArrears, 0)), { digits: 2 })}`,
        paymentArrears: `￥${XEUtils.commafy(XEUtils.toNumber(getDataSource().reduce((prev, next) => prev + next.paymentArrears, 0)), { digits: 2 })}`,
      });
      printJS({
        documentTitle: "EAIRP " + t('reports.other.supplierArrearsDetail'),
        properties: supplierBillDetailColumns.map(item => {
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
      getTitle,
      registerModal,
      openReceipt,
      registerTable,
      handleSuccess,
      handleCancel,
      handlePurchaseStorageModal,
      handlePurchaseRefundModal,
      exportTable,
      primaryPrint
    }
  }
})
</script>