<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <div class="components-page-header-demo-responsive" style="border: 1px solid rgb(235, 237, 240)">
      <a-page-header
          :title="t('financial.payment.paymentReceiptDetail')"
          :sub-title= "receiptNumber">
        <template #extra>
          <a-button @click="exportTable" v-text="t('financial.payment.export.name')"/>
          <a-button @click="primaryPrint" type="primary" v-text="t('financial.regularPrint')"/>
          <!--          <a-button key="triplePrint">三联打印</a-button>-->
          <!--          <a-button key="2" type="primary">发起流程审批</a-button>-->
        </template>
        <a-descriptions size="small" :column="3">
          <a-descriptions-item :label="t('financial.payment.header.supplier')">{{ supplierName }}</a-descriptions-item>
          <a-descriptions-item :label="t('financial.payment.header.receiptDate')">{{ receiptDate }}</a-descriptions-item>
          <a-descriptions-item :label="t('financial.payment.header.financialPerson')">{{ financialPersonName }}</a-descriptions-item>
          <a-descriptions-item :label="t('financial.payment.table.paymentAccount')">{{ paymentAccountName }}</a-descriptions-item>
          <a-descriptions-item :label="t('financial.payment.table.totalPayment')">{{ totalPaymentAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('financial.payment.table.discountAmount')">{{ discountAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('financial.payment.table.actualPayment')">{{ actualPaymentAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('financial.payment.table.remark')">
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
                :title="t('financial.payment.table.status')"
                :value="status === 1 ? t('sys.table.audited') : t('sys.table.unaudited')"
                :value-style="status === 1 ? { color: '#3f8600' } : { color: '#cf1322' }"
                :style="{
                marginRight: '32px',
                color: 'green',
              }"
            />
            <a-statistic :title="t('financial.payment.table.totalPayment')"
                         prefix="￥"
                         :value-style="status === 1 ? { color: '#3f8600' } : { color: '#cf1322' }"
                         :value="totalPaymentAmount"/>
          </div>
        </div>
      </a-page-header>
      <BasicTable @register="registerTable">
      </BasicTable>
    </div>
  </BasicModal>
</template>
<script lang="ts">
import {defineComponent, ref} from 'vue';
import {BasicTable, useTable} from '/src/components/Table';
import {BasicModal, useModalInner} from "@/components/Modal";
import {getPaymentDetailById, exportPaymentDetail} from "@/api/financial/payment";
import {
  Descriptions,
  DescriptionsItem,
  PageHeader,
  Statistic,
} from 'ant-design-vue';
import {paymentReceiptTableColumns} from "@/views/financial/payment/payment.data";
import printJS from "print-js";
import {getTimestamp} from "@/utils/dateUtil";
import {useI18n} from "vue-i18n";
export default defineComponent({
  name: 'ViewExpenseModal',
  components: {
    BasicModal,
    BasicTable,
    'a-page-header': PageHeader,
    'a-descriptions': Descriptions,
    'a-descriptions-item': DescriptionsItem,
    'a-statistic': Statistic,
  },
  setup() {
    const { t } = useI18n();
    const receiptNumber = ref('');
    const supplierName = ref('');
    const receiptDate = ref('');
    const financialPersonName = ref('');
    const paymentAccountName = ref('');
    const totalPaymentAmount = ref('');
    const discountAmount = ref('');
    const actualPaymentAmount = ref('');
    const remark = ref('')
    const tableData = ref([]);
    const status = ref(-1);
    const [registerTable] = useTable({
      title: t('financial.payment.receiptDetail'),
      columns: paymentReceiptTableColumns,
      dataSource: tableData,
      pagination: false,
      showIndexColumn: false,
      bordered: true,
      canResize: false,
    });
    const getTitle = ref(t('financial.payment.receiptDetail'));
    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false, destroyOnClose: true, width: 1000, showOkBtn: false});
      const res = await getPaymentDetailById(data.id);
      tableData.value = res.data.tableData;
      receiptNumber.value = res.data.receiptNumber;
      supplierName.value = res.data.supplierName;
      financialPersonName.value = res.data.financialPersonName;
      receiptDate.value = res.data.receiptDate;
      paymentAccountName.value = res.data.paymentAccountName;
      totalPaymentAmount.value = res.data.totalPaymentAmount;
      discountAmount.value = res.data.discountAmount;
      actualPaymentAmount.value = res.data.actualPaymentAmount;
      remark.value = res.data.remark;
      status.value = res.data.status;
    });

    function handleSubmit() {
      closeModal();
    }

    async function exportTable() {
      const file: any = await exportPaymentDetail(receiptNumber.value)
      if (file.size > 0) {
        const blob = new Blob([file]);
        const link = document.createElement("a");
        const timestamp = getTimestamp(new Date());
        link.href = URL.createObjectURL(blob);
        link.download = t('financial.payment.receiptDetail') + timestamp + ".xlsx";
        link.target = "_blank";
        link.click();
      }
    }

    const flexContainer = 'display: flex; justify-content: space-between; border-bottom: 1px solid #ddd; padding: 8px;';
    const flexItem = 'display: flex; flex-direction: column; justify-content: space-between; font-size: 12px;';
    function primaryPrint() {
      const header = `
  <div style="${flexContainer}">
    <div style="${flexItem}">${t('financial.payment.header.receiptNumber')}：${receiptNumber.value}</div>
    <div style="${flexItem}">${t('financial.payment.header.receiptDate')}：${receiptDate.value}</div>
    <div style="${flexItem}">${t('financial.payment.table.totalPayment')}：${totalPaymentAmount.value}</div>
  </div>
  <div style="${flexContainer}">
    <div style="${flexItem}">${t('financial.payment.table.paymentAccount')}：${paymentAccountName.value}</div>
    <div style="${flexItem}">${t('financial.payment.table.discountAmount')}：${discountAmount.value}</div>
    <div style="${flexItem}">${t('financial.payment.table.actualPayment')}：${actualPaymentAmount.value}</div>
  </div>
  <div style="${flexContainer}">
    <div style="${flexItem}">${t('financial.payment.table.supplier')}：${supplierName.value}</div>
    <div style="${flexItem}">${t('financial.payment.table.financialPerson')}：${financialPersonName.value}</div>
    <div style="${flexItem}">${t('financial.payment.table.remark')}：${remark.value}</div>
  </div>
`;
      printJS({
        documentTitle: "EAIRP " + t('financial.payment.export.exportData'),
        header: header,
        properties: paymentReceiptTableColumns.map((item) => {
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
      supplierName,
      receiptDate,
      financialPersonName,
      paymentAccountName,
      totalPaymentAmount,
      discountAmount,
      actualPaymentAmount,
      remark,
      status,
      registerTable,
      registerModal,
      getTitle,
      handleSubmit,
      exportTable,
      primaryPrint
    };
  },
});
</script>
<style scoped>

</style>
