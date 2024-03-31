<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <div class="components-page-header-demo-responsive" style="border: 1px solid rgb(235, 237, 240)">
      <a-page-header
          :title="t('purchase.storage.detail')"
          :sub-title= "receiptNumber">
        <template #extra>
          <a-button @click="exportTable" v-text="t('purchase.storage.export.name')"/>
          <a-button @click="primaryPrint" type="primary" v-text="t('purchase.regularPrint')"/>
          <!--          <a-button key="triplePrint">三联打印</a-button>-->
          <!--          <a-button key="2" type="primary">发起流程审批</a-button>-->
        </template>
        <a-descriptions size="small" :column="4">
          <a-descriptions-item :label="t('purchase.storage.view.supplier')">{{ supplierName }}</a-descriptions-item>
          <a-descriptions-item :label="t('purchase.storage.view.receiptDate')">{{ receiptDate }}</a-descriptions-item>
          <a-descriptions-item :label="t('purchase.storage.view.purchaseOrder')">
            <a @click="viewOrderReceipt">{{ otherReceipt }}</a>
          </a-descriptions-item>
          <a-descriptions-item :label="t('purchase.storage.view.discountRate')">{{ paymentRate }}</a-descriptions-item>
          <a-descriptions-item :label="t('purchase.storage.view.paymentDiscount')">{{ paymentAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('purchase.storage.view.discountAmount')">{{ paymentLastAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('purchase.storage.view.otherFees')">{{ otherAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('purchase.storage.view.settlementAccount')">{{ accountName }}</a-descriptions-item>
          <a-descriptions-item :label="t('purchase.storage.view.thisTimePaymentAmount')">{{ thisPaymentAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('purchase.storage.view.thisTimeArrearsAmount')">{{ thisArrearsAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('purchase.storage.view.remark')">
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
                :title="t('purchase.storage.view.status')"
                :value="status === 1 ? t('sys.table.audited') : t('sys.table.unaudited')"
                :value-style="status === 1 ? { color: '#3f8600' } : { color: '#cf1322' }"
                :style="{
                marginRight: '32px',
                color: 'green',
              }"
            />
            <a-statistic :title="t('purchase.storage.view.receiptAmount')"
                         prefix="￥"
                         :value-style="status === 1 ? { color: '#3f8600' } : { color: '#cf1322' }"
                         :value="paymentLastAmount"/>
          </div>
        </div>
      </a-page-header>
      <BasicTable @register="registerTable">
      </BasicTable>
    </div>
  </BasicModal>
  <ViewOrderModal @register="viewOrderReceiptModal"/>
</template>
<script lang="ts">
import {defineComponent, ref} from 'vue';
import {BasicTable, useTable} from '/src/components/Table';
import {BasicModal, useModal, useModalInner} from "@/components/Modal";
import {getLinkStorageDetail, exportStorageDetail} from "@/api/purchase/storage";
import {purchaseOrderTableColumns} from "@/views/purchase/order/purchaseOrder.data";
import ViewOrderModal from "@/views/purchase/order/components/ViewOrderModal.vue";
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
  name: 'ViewPurchaseStorageModal',
  components: {
    BasicModal,
    BasicTable,
    ViewOrderModal,
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
    const paymentRate = ref('');
    const receiptDate = ref('');
    const paymentAmount = ref('');
    const paymentLastAmount = ref('');
    const otherAmount = ref('');
    const thisPaymentAmount = ref('');
    const thisArrearsAmount = ref('');
    const accountName = ref('');
    const remark = ref('')
    const status = ref();
    const [viewOrderReceiptModal, {openModal: openViewOrderModal}] = useModal();
    const tableData = ref([]);
    const [registerTable] = useTable({
      title: t('purchase.storage.view.title'),
      columns: purchaseOrderTableColumns,
      dataSource: tableData,
      pagination: false,
      showIndexColumn: false,
      bordered: true,
    });
    const getTitle = ref(t('purchase.storage.receipt'));
    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false, destroyOnClose: true, width: 1200, showOkBtn: false});
      const res = await getLinkStorageDetail(data.receiptNumber);
      console.info(res.data);
      tableData.value = res.data.tableData;
      receiptNumber.value = res.data.receiptNumber;
      supplierName.value = res.data.supplierName;
      receiptDate.value = res.data.receiptDate;
      paymentRate.value = res.data.paymentRate;
      paymentAmount.value = res.data.paymentAmount;
      paymentLastAmount.value = res.data.paymentLastAmount;
      otherAmount.value = res.data.otherAmount;
      thisPaymentAmount.value = res.data.thisPaymentAmount;
      thisArrearsAmount.value = res.data.thisArrearsAmount;
      accountName.value = res.data.accountName;
      otherReceipt.value = res.data.otherReceipt;
      remark.value = res.data.remark;
      status.value = res.data.status;
    });

    function viewOrderReceipt() {
      closeModal();
      openViewOrderModal(true, {
        receiptNumber: otherReceipt.value
      });
    }

    function handleSubmit() {
      closeModal();
    }

    async function exportTable() {
      const file: any = await exportStorageDetail(receiptNumber.value)
      if (file.size > 0) {
        const blob = new Blob([file]);
        const link = document.createElement("a");
        const timestamp = getTimestamp(new Date());
        link.href = URL.createObjectURL(blob);
        link.download = t('purchase.storage.export.exportData') + timestamp + ".xlsx";
        link.target = "_blank";
        link.click();
      }
    }

    const flexContainer = 'display: flex; justify-content: space-between; border-bottom: 1px solid #ddd; padding: 8px;';
    const flexItem = 'display: flex; flex-direction: column; justify-content: space-between; font-size: 12px;';
    function primaryPrint() {
      const header = `
  <div style="${flexContainer}">
    <div style="${flexItem}">${t('purchase.storage.table.receiptNumber')}：${receiptNumber.value}</div>
    <div style="${flexItem}">${t('purchase.storage.view.receiptAmount')}：${paymentLastAmount.value}</div>
    <div style="${flexItem}">${t('purchase.storage.view.receiptDate')}：${receiptDate.value}</div>
  </div>
  <div style="${flexContainer}">
    <div style="${flexItem}">${t('purchase.storage.view.settlementAccount')}：${accountName.value}</div>
    <div style="${flexItem}">${t('purchase.storage.view.discountRate')}：${paymentRate.value}</div>
    <div style="${flexItem}">${t('purchase.storage.view.paymentDiscount')}：${paymentAmount.value}</div>
    <div style="${flexItem}">${t('purchase.storage.view.otherFees')}：${otherAmount.value}</div>
  </div>
  <div style="${flexContainer}">
    <div style="${flexItem}">${t('purchase.storage.view.thisTimePaymentAmount')}：${thisPaymentAmount.value}</div>
    <div style="${flexItem}">${t('purchase.storage.view.thisTimeArrearsAmount')}：${thisArrearsAmount.value}</div>
    <div style="${flexItem}">${t('purchase.storage.view.supplier')}：${supplierName.value}</div>
    <div style="${flexItem}">${t('purchase.storage.view.remark')}：${remark.value}</div>
  </div>
`;
      printJS({
        documentTitle: "EAIRP " + t('purchase.storage.detail'),
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
      paymentRate,
      receiptDate,
      paymentAmount,
      paymentLastAmount,
      otherAmount,
      thisPaymentAmount,
      thisArrearsAmount,
      accountName,
      remark,
      status,
      registerTable,
      registerModal,
      getTitle,
      handleSubmit,
      viewOrderReceiptModal,
      viewOrderReceipt,
      exportTable,
      primaryPrint
    };
  },
});
</script>
<style scoped>

</style>
