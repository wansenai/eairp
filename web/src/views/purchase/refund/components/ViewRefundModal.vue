<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <div class="components-page-header-demo-responsive" style="border: 1px solid rgb(235, 237, 240)">
      <a-page-header
          :title="t('purchase.refund.detail')"
          :sub-title= "receiptNumber">
        <template #extra>
          <a-button @click="exportTable" v-text="t('purchase.refund.export.name')"/>
          <a-button @click="primaryPrint" type="primary" v-text="t('purchase.regularPrint')"/>
          <!--          <a-button key="triplePrint">三联打印</a-button>-->
          <!--          <a-button key="2" type="primary">发起流程审批</a-button>-->
        </template>
        <a-descriptions size="small" :column="4">
          <a-descriptions-item :label="t('purchase.refund.view.supplier')">{{ supplierName }}</a-descriptions-item>
          <a-descriptions-item :label="t('purchase.refund.view.receiptDate')">{{ receiptDate }}</a-descriptions-item>
          <a-descriptions-item :label="t('purchase.refund.view.purchaseOrder')">
            <a @click="viewOrderReceipt">{{ otherReceipt }}</a>
          </a-descriptions-item>
          <a-descriptions-item :label="t('purchase.refund.view.discountRate')">{{ refundOfferRate }}</a-descriptions-item>
          <a-descriptions-item :label="t('purchase.refund.view.refundDiscount')">{{ refundOfferAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('purchase.refund.view.discountAmount')">{{ refundLastAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('purchase.refund.view.otherFees')">{{ otherAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('purchase.refund.view.settlementAccount')">{{ accountName }}</a-descriptions-item>
          <a-descriptions-item :label="t('purchase.refund.view.thisRefundAmount')">{{ thisRefundAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('purchase.refund.view.thisTimeArrearsAmount')">{{ thisArrearsAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('purchase.refund.view.remark')">
            {{ remark }}
          </a-descriptions-item>
        </a-descriptions>
        <div class="extra">
          <div
              class="descriptions-context"
              :style="{
              display: 'flex',
              width: 'max-content',
              justifyContent: 'flex-end',
            }"
          >
            <a-statistic
                :title="t('purchase.refund.view.status')"
                :value="status === 1 ? t('sys.table.audited') : t('sys.table.unaudited')"
                :value-style="status === 1 ? { color: '#3f8600' } : { color: '#cf1322' }"
                :style="{
                marginRight: '32px',
                color: 'green',
              }"
            />
            <a-statistic :title="t('purchase.refund.view.receiptAmount')"
                         prefix="￥"
                         :value-style="status === 1 ? { color: '#3f8600' } : { color: '#cf1322' }"
                         :value="refundLastAmount"/>
          </div>
        </div>
      </a-page-header>
      <BasicTable @register="registerTable">
      </BasicTable>
    </div>
  </BasicModal>
  <ViewStorageModal @register="viewStorageReceiptModal"/>
</template>
<script lang="ts">
import {defineComponent, ref} from 'vue';
import {BasicTable, useTable} from '/src/components/Table';
import {BasicModal, useModal, useModalInner} from "@/components/Modal";
import {getLinkRefundDetail, exportRefundDetail} from "@/api/purchase/refund";
import {purchaseOrderTableColumns} from "@/views/purchase/order/purchaseOrder.data";
import ViewStorageModal from "@/views/purchase/storage/components/ViewStorageModal.vue";
import {
  Descriptions,
  DescriptionsItem,
  PageHeader,
  Statistic,
} from 'ant-design-vue';
import printJS from "print-js";
import {getTimestamp} from "@/utils/dateUtil";
import {useI18n} from "vue-i18n";

export default defineComponent({
  name: 'ViewPurchaseRefundModal',
  components: {
    BasicModal,
    BasicTable,
    ViewStorageModal,
    'a-page-header': PageHeader,
    'a-descriptions': Descriptions,
    'a-descriptions-item': DescriptionsItem,
    'a-statistic': Statistic,
  },
  setup() {
    const { t } = useI18n();
    const receiptNumber = ref('');
    const otherReceipt = ref('');
    const supplierName = ref(0);
    const refundOfferRate = ref('');
    const receiptDate = ref('');
    const refundOfferAmount = ref('');
    const refundLastAmount = ref('');
    const otherAmount = ref('');
    const thisRefundAmount = ref('');
    const thisArrearsAmount = ref('');
    const deposit = ref('');
    const accountName = ref('');
    const remark = ref('')
    const status = ref();

    const [viewStorageReceiptModal, {openModal: openViewStorageModal}] = useModal();
    const tableData = ref([]);
    const [registerTable] = useTable({
      title: t('purchase.refund.view.title'),
      columns: purchaseOrderTableColumns,
      dataSource: tableData,
      pagination: false,
      showIndexColumn: false,
      bordered: true,
    });
    const getTitle = ref(t('purchase.refund.receipt'));
    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false, destroyOnClose: true, width: 1200, showOkBtn: false});
      const res = await getLinkRefundDetail(data.receiptNumber);
      console.info(res.data);
      tableData.value = res.data.tableData;
      receiptNumber.value = res.data.receiptNumber;
      supplierName.value = res.data.supplierName;
      receiptDate.value = res.data.receiptDate;
      refundOfferRate.value = res.data.refundOfferRate;
      refundOfferAmount.value = res.data.refundOfferAmount;
      refundLastAmount.value = res.data.refundLastAmount;
      otherAmount.value = res.data.otherAmount;
      thisRefundAmount.value = res.data.thisRefundAmount;
      thisArrearsAmount.value = res.data.thisArrearsAmount;
      accountName.value = res.data.accountName;
      otherReceipt.value = res.data.otherReceipt;
      remark.value = res.data.remark;
      status.value = res.data.status;
    });

    function viewOrderReceipt() {
      closeModal();
      openViewStorageModal(true, {
        receiptNumber: otherReceipt.value
      });
    }

    function handleSubmit() {
      closeModal();
    }

    function viewRefundReceipt() {
      closeModal();
    }

    async function exportTable() {
      const file: any = await exportRefundDetail(receiptNumber.value)
      if (file.size > 0) {
        const blob = new Blob([file]);
        const link = document.createElement("a");
        const timestamp = getTimestamp(new Date());
        link.href = URL.createObjectURL(blob);
        link.download = t('purchase.refund.export.exportData') + timestamp + ".xlsx";
        link.target = "_blank";
        link.click();
      }
    }

    const flexContainer = 'display: flex; justify-content: space-between; border-bottom: 1px solid #ddd; padding: 8px;';
    const flexItem = 'display: flex; flex-direction: column; justify-content: space-between; font-size: 12px;';
    function primaryPrint() {
      const header = `
  <div style="${flexContainer}">
    <div style="${flexItem}">${t('purchase.refund.table.receiptNumber')}：${receiptNumber.value}</div>
    <div style="${flexItem}">${t('purchase.refund.view.receiptAmount')}：${refundLastAmount.value}</div>
    <div style="${flexItem}">${t('purchase.refund.view.receiptDate')}：${receiptDate.value}</div>
  </div>
  <div style="${flexContainer}">
    <div style="${flexItem}">${t('purchase.refund.view.settlementAccount')}：${accountName.value}</div>
    <div style="${flexItem}">${t('purchase.refund.view.discountRate')}：${refundOfferRate.value}</div>
    <div style="${flexItem}">${t('purchase.refund.view.refundDiscount')}：${refundOfferAmount.value}</div>
    <div style="${flexItem}">${t('purchase.refund.view.otherFees')}：${otherAmount.value}</div>
  </div>
  <div style="${flexContainer}">
    <div style="${flexItem}">${t('purchase.refund.view.thisRefundAmount')}：${thisRefundAmount.value}</div>
    <div style="${flexItem}">${t('purchase.refund.view.thisTimeArrearsAmount')}：${thisArrearsAmount.value}</div>
    <div style="${flexItem}">${t('purchase.refund.view.supplier')}：${supplierName.value}</div>
    <div style="${flexItem}">${t('purchase.refund.view.remark')}：${remark.value}</div>
  </div>
`;
      printJS({
        documentTitle: "EAIRP " + t('purchase.refund.detail'),
        header: header,
        properties: purchaseOrderTableColumns.map((item) => {
          return {
            field: item.dataIndex,
            displayName: item.title,
          };
        }),
        printable: tableData.value,
        gridHeaderStyle: 'border: 1px solid #ddd; font-size: 12px; text-align: center; padding: 8px;',
        gridStyle: 'border: 1px solid #ddd; font-size: 12px; text-align: center; padding: 8px;',
        type: 'json',
      });
    }

    return {
      t,
      receiptNumber,
      otherReceipt,
      supplierName,
      refundOfferRate,
      receiptDate,
      refundOfferAmount,
      deposit,
      refundLastAmount,
      otherAmount,
      thisRefundAmount,
      thisArrearsAmount,
      accountName,
      remark,
      status,
      registerTable,
      registerModal,
      getTitle,
      handleSubmit,
      viewRefundReceipt,
      viewOrderReceipt,
      viewStorageReceiptModal,
      primaryPrint,
      exportTable
    };
  },
});
</script>
<style scoped>

</style>
