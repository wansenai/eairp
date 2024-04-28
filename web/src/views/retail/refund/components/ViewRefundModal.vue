<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <div class="components-page-header-demo-responsive" style="border: 1px solid rgb(235, 237, 240)">
      <a-page-header
          :title="t('retail.refund.detail')"
          :sub-title= "receiptNumber">
        <template #extra>
          <a-button @click="exportTable" v-text="t('retail.refund.export.name')"/>
          <a-button @click="primaryPrint" type="primary" v-text="t('retail.regularPrint')"/>
          <!--          <a-button key="triplePrint">三联打印</a-button>-->
          <!--          <a-button key="2" type="primary">发起流程审批</a-button>-->
        </template>
        <a-descriptions size="small" :column="3">
          <a-descriptions-item :label="t('retail.refund.view.member')">{{ memberName }}</a-descriptions-item>
          <a-descriptions-item :label="t('retail.refund.view.receiptDate')">{{ receiptDate }}</a-descriptions-item>
          <a-descriptions-item :label="t('retail.refund.view.receiptAmount')">{{ receiptAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('retail.refund.view.paymentAmount')">{{ paymentAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('retail.refund.view.changeAmount')">{{ backAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('retail.refund.view.paymentAccount')">{{ accountName }}</a-descriptions-item>
          <a-descriptions-item :label="t('retail.refund.view.relatedReceipt')">
            <a @click="viewShipmentReceipt">{{ otherReceipt }}</a>
          </a-descriptions-item>
          <a-descriptions-item :label="t('retail.refund.view.remark')">
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
                :title="t('retail.refund.view.status')"
                :value="status === 1 ? t('sys.table.audited') : t('sys.table.unaudited')"
                :value-style="status === 1 ? { color: '#3f8600' } : { color: '#cf1322' }"
                :style="{
                marginRight: '32px',
                color: 'green',
              }"
            />
            <a-statistic :title="t('retail.refund.view.paymentAmount')"
                         prefix="￥"
                         :value-style="status === 1 ? { color: '#3f8600' } : { color: '#cf1322' }"
                         :value="receiptAmount"/>
          </div>
        </div>
      </a-page-header>
      <BasicTable @register="registerTable">
      </BasicTable>
    </div>
  </BasicModal>
  <ViewShipmentModal @register="viewShipmentReceiptModal"></ViewShipmentModal>
</template>
<script lang="ts">
import {defineComponent, ref} from 'vue';
import {BasicTable, useTable} from '/src/components/Table';
import {BasicModal, useModal, useModalInner} from "@/components/Modal";
import {getLinkRefundDetail, exportRefundDetail} from "@/api/retail/refund";
import {
  Descriptions,
  DescriptionsItem,
  PageHeader,
  Statistic,
} from 'ant-design-vue';
import {retailShipmentsTableColumns} from "@/views/retail/shipments/shipments.data";
import ViewShipmentModal from "@/views/retail/shipments/components/ViewShipmentModal.vue";
import printJS from "print-js";
import {getTimestamp} from "@/utils/dateUtil";
import {useI18n} from "vue-i18n";

export default defineComponent({
  name: 'ViewRefundModal',
  components: {
    BasicModal,
    BasicTable,
    ViewShipmentModal,
    'a-page-header': PageHeader,
    'a-descriptions': Descriptions,
    'a-descriptions-item': DescriptionsItem,
    'a-statistic': Statistic,
  },
  setup() {
    const { t } = useI18n();
    const receiptNumber = ref('');
    const memberName = ref(0);
    const paymentType = ref('');
    const receiptDate = ref('');
    const receiptType = ref('');
    const receiptAmount = ref('');
    const paymentAmount = ref('');
    const backAmount = ref('');
    const accountName = ref('');
    const remark = ref('')
    const status = ref();
    const otherReceipt = ref('');
    const [viewShipmentReceiptModal, {openModal: openShipmentViewModal}] = useModal();
    const tableData = ref([]);
    const [registerTable] = useTable({
      title: t('retail.refund.detail'),
      columns: retailShipmentsTableColumns,
      dataSource: tableData,
      pagination: false,
      showIndexColumn: false,
      bordered: true,
    });
    const getTitle = ref(t('retail.refund.receipt'));
    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false, destroyOnClose: true, width: 1200, showOkBtn: false});
      const res = await getLinkRefundDetail(data.receiptNumber);
      console.info(res.data);
      tableData.value = res.data.tableData;
      receiptNumber.value = res.data.receiptNumber;
      memberName.value = res.data.memberName;
      paymentType.value = res.data.paymentType;
      receiptDate.value = res.data.receiptDate;
      receiptAmount.value = res.data.receiptAmount;
      paymentAmount.value = res.data.paymentAmount;
      backAmount.value = res.data.backAmount;
      accountName.value = res.data.accountName;
      otherReceipt.value = res.data.otherReceipt;
      remark.value = res.data.remark;
      status.value = res.data.status;
    });

    function handleSubmit() {
      closeModal();
    }

    function viewShipmentReceipt() {
      closeModal();
      openShipmentViewModal(true, {
        isUpdate: false,
        receiptNumber: otherReceipt.value
      });
    }

    async function exportTable() {
      const file: any = await exportRefundDetail(receiptNumber.value)
      if (file.size > 0) {
        const blob = new Blob([file]);
        const link = document.createElement("a");
        const timestamp = getTimestamp(new Date());
        link.href = URL.createObjectURL(blob);
        link.download = t('retail.refund.export.exportData') + timestamp + ".xlsx";
        link.target = "_blank";
        link.click();
      }
    }

    const flexContainer = 'display: flex; justify-content: space-between; border-bottom: 1px solid #ddd; padding: 8px;';
    const flexItem = 'display: flex; flex-direction: column; justify-content: space-between; font-size: 12px;';
    function primaryPrint() {
      const header = `
  <div style="${flexContainer}">
    <div style="${flexItem}">${t('retail.refund.table.receiptNumber')}：${receiptNumber.value}</div>
    <div style="${flexItem}">${t('retail.refund.view.receiptAmount')}：${receiptAmount.value}</div>
    <div style="${flexItem}">${t('retail.refund.view.receiptDate')}：${receiptDate.value}</div>
  </div>
  <div style="${flexContainer}">
    <div style="${flexItem}">${t('retail.refund.view.paymentAccount')}：${accountName.value}</div>
    <div style="${flexItem}">${t('retail.refund.view.paymentAmount')}：${paymentAmount.value}</div>
  </div>
  <div style="${flexContainer}">
    <div style="${flexItem}">${t('retail.refund.view.changeAmount')}：${backAmount.value}</div>
    <div style="${flexItem}">${t('retail.refund.view.member')}：${memberName.value}</div>
    <div style="${flexItem}">${t('retail.refund.view.remark')}：${remark.value}</div>
  </div>
`;
      printJS({
        documentTitle: "EAIRP " + t('retail.refund.export.exportData'),
        header: header,
        properties: retailShipmentsTableColumns.map((item) => {
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
      memberName,
      paymentType,
      paymentAmount,
      receiptDate,
      receiptType,
      receiptAmount,
      backAmount,
      accountName,
      remark,
      status,
      registerTable,
      registerModal,
      getTitle,
      handleSubmit,
      otherReceipt,
      viewShipmentReceipt,
      viewShipmentReceiptModal,
      exportTable,
      primaryPrint
    };
  },
});
</script>
<style scoped>

</style>
