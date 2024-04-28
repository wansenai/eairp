<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <div class="components-page-header-demo-responsive" style="border: 1px solid rgb(235, 237, 240)">
      <a-page-header
          :title="t('purchase.order.detail')"
          :sub-title= "receiptNumber">
        <template #extra>
          <a-button @click="exportTable" v-text="t('purchase.order.export.name')"/>
          <a-button @click="primaryPrint" type="primary" v-text="t('purchase.regularPrint')"/>
          <!--          <a-button key="triplePrint">三联打印</a-button>-->
          <!--          <a-button key="2" type="primary">发起流程审批</a-button>-->
        </template>
        <a-descriptions size="small" :column="3">
          <a-descriptions-item :label="t('purchase.order.view.supplier')">{{ supplierName }}</a-descriptions-item>
          <a-descriptions-item :label="t('purchase.order.view.receiptDate')">{{ receiptDate }}</a-descriptions-item>
          <a-descriptions-item :label="t('purchase.order.view.settlementAccount')">{{ accountName }}</a-descriptions-item>
          <a-descriptions-item :label="t('purchase.order.view.discountRate')">{{ discountRate }}</a-descriptions-item>
          <a-descriptions-item :label="t('purchase.order.view.paymentDiscount')">{{ discountAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('purchase.order.view.discountAmount')">{{ discountLastAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('purchase.order.view.paymentDeposit')">{{ deposit }}</a-descriptions-item>
          <a-descriptions-item :label="t('purchase.order.view.remark')">
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
                :title="t('purchase.order.view.status')"
                :value="status === 1 ? t('sys.table.audited') : t('sys.table.unaudited')"
                :value-style="status === 1 ? { color: '#3f8600' } : { color: '#cf1322' }"
                :style="{
                marginRight: '32px',
                color: 'green',
              }"
            />
            <a-statistic :title="t('purchase.order.view.receiptAmount')"
                         prefix="￥"
                         :value-style="status === 1 ? { color: '#3f8600' } : { color: '#cf1322' }"
                         :value="discountLastAmount"/>
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
import {getLinkOrderDetail, exportOrderDetail} from "@/api/purchase/order";
import {purchaseOrderTableColumns} from "@/views/purchase/order/purchaseOrder.data";
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
  name: 'ViewPurchaseOrderModal',
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
    const otherReceipt = ref('');
    const supplierName = ref(0);
    const discountRate = ref('');
    const receiptDate = ref('');
    const discountAmount = ref('');
    const discountLastAmount = ref('');
    const deposit = ref('');
    const accountName = ref('');
    const remark = ref('')
    const status = ref();

    const tableData = ref([]);
    const [registerTable] = useTable({
      title: t('purchase.order.view.title'),
      columns: purchaseOrderTableColumns,
      dataSource: tableData,
      pagination: false,
      showIndexColumn: false,
      bordered: true,
    });
    const getTitle = ref(t('purchase.order.receipt'));
    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false, destroyOnClose: true, width: 1200, showOkBtn: false});
      const res = await getLinkOrderDetail(data.receiptNumber);
      tableData.value = res.data.tableData;
      receiptNumber.value = res.data.receiptNumber;
      supplierName.value = res.data.supplierName;
      receiptDate.value = res.data.receiptDate;
      discountRate.value = res.data.discountRate;
      discountAmount.value = res.data.discountAmount;
      discountLastAmount.value = res.data.discountLastAmount;
      accountName.value = res.data.accountName;
      otherReceipt.value = res.data.otherReceipt;
      remark.value = res.data.remark;
      status.value = res.data.status;
    });

    function handleSubmit() {
      closeModal();
    }

    async function exportTable() {
      const file: any = await exportOrderDetail(receiptNumber.value)
      if (file.size > 0) {
        const blob = new Blob([file]);
        const link = document.createElement("a");
        const timestamp = getTimestamp(new Date());
        link.href = URL.createObjectURL(blob);
        link.download = t('purchase.order.export.exportData') + timestamp + ".xlsx";
        link.target = "_blank";
        link.click();
      }
    }

    const flexContainer = 'display: flex; justify-content: space-between; border-bottom: 1px solid #ddd; padding: 8px;';
    const flexItem = 'display: flex; flex-direction: column; justify-content: space-between; font-size: 12px;';
    function primaryPrint() {
      const header = `
  <div style="${flexContainer}">
    <div style="${flexItem}">${t('purchase.order.table.receiptNumber')}：${receiptNumber.value}</div>
    <div style="${flexItem}">${t('purchase.order.view.receiptAmount')}：${discountLastAmount.value}</div>
    <div style="${flexItem}">${t('purchase.order.view.receiptDate')}：${receiptDate.value}</div>
  </div>
  <div style="${flexContainer}">
    <div style="${flexItem}">${t('purchase.order.view.settlementAccount')}：${accountName.value}</div>
    <div style="${flexItem}">${t('purchase.order.view.discountRate')}：${discountRate.value}</div>
    <div style="${flexItem}">${t('purchase.order.view.paymentDiscount')}：${discountAmount.value}</div>
  </div>
  <div style="${flexContainer}">
    <div style="${flexItem}">${t('purchase.order.view.paymentDeposit')}：${deposit.value}</div>
    <div style="${flexItem}">${t('purchase.order.view.supplier')}：${supplierName.value}</div>
    <div style="${flexItem}">${t('purchase.order.view.remark')}：${remark.value}</div>
  </div>
`;
      printJS({
        documentTitle: "EAIRP " + t('purchase.order.detail'),
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
      discountRate,
      receiptDate,
      discountAmount,
      deposit,
      discountLastAmount,
      accountName,
      remark,
      status,
      registerTable,
      registerModal,
      getTitle,
      handleSubmit,
      exportTable,
      primaryPrint,
    };
  },
});
</script>
<style scoped>

</style>
