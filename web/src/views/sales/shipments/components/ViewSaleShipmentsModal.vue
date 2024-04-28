<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <div class="components-page-header-demo-responsive" style="border: 1px solid rgb(235, 237, 240)">
      <a-page-header
          :title="t('sales.shipments.detail')"
          :sub-title= "receiptNumber">
        <template #extra>
          <a-button @click="exportTable" v-text="t('sales.shipments.export.name')"/>
          <a-button @click="primaryPrint" type="primary" v-text="t('sales.regularPrint')"/>
          <!--          <a-button key="triplePrint">三联打印</a-button>-->
          <!--          <a-button key="2" type="primary">发起流程审批</a-button>-->
        </template>
        <a-descriptions size="small" :column="4">
          <a-descriptions-item :label="t('sales.shipments.view.customer')">{{ customerName }}</a-descriptions-item>
          <a-descriptions-item :label="t('sales.shipments.view.receiptDate')">{{ receiptDate }}</a-descriptions-item>
          <a-descriptions-item :label="t('sales.shipments.view.salesOrder')">
            <a @click="viewOrderReceipt">{{ otherReceipt }}</a>
          </a-descriptions-item>
          <a-descriptions-item :label="t('sales.shipments.view.discountRate')">{{ collectOfferRate }}</a-descriptions-item>
          <a-descriptions-item :label="t('sales.shipments.view.collectionDiscount')">{{ collectOfferAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('sales.shipments.view.discountAmount')">{{ collectOfferLastAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('sales.shipments.view.otherFees')">{{ otherAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('sales.shipments.view.settlementAccount')">{{ accountName }}</a-descriptions-item>
          <a-descriptions-item :label="t('sales.shipments.view.thisTimeCollectAmount')">{{ thisCollectAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('sales.shipments.view.thisTimeArrearsAmount')">{{ thisArrearsAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('sales.shipments.view.salesPerson')">{{  }}</a-descriptions-item>
          <a-descriptions-item ></a-descriptions-item>
          <a-descriptions-item :label="t('sales.shipments.view.remark')">
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
                :title="t('sales.shipments.view.status')"
                :value="status === 1 ? t('sys.table.audited') : t('sys.table.unaudited')"
                :value-style="status === 1 ? { color: '#3f8600' } : { color: '#cf1322' }"
                :style="{
                marginRight: '32px',
                color: 'green',
              }"
            />
            <a-statistic :title="t('sales.shipments.view.receiptAmount')"
                         prefix="￥"
                         :value-style="status === 1 ? { color: '#3f8600' } : { color: '#cf1322' }"
                         :value="thisCollectAmount"/>
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
import {getLinkShipmentsDetail, exportShipmentsDetail} from "@/api/sale/shipments";
import {TableColumns} from "@/views/sales/order/sales.data";
import ViewOrderModal from "@/views/sales/order/components/ViewSaleOrderModal.vue";
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
  name: 'ViewSaleShipmentsModal',
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
    const customerName = ref(0);
    const collectOfferRate = ref('');
    const receiptDate = ref('');
    const collectOfferAmount = ref('');
    const collectOfferLastAmount = ref('');
    const otherAmount = ref('');
    const thisCollectAmount = ref('');
    const thisArrearsAmount = ref('');
    const accountName = ref('');
    const remark = ref('')
    const status = ref();
    const [viewOrderReceiptModal, {openModal: openViewOrderModal}] = useModal();
    const tableData = ref([]);
    const [registerTable] = useTable({
      title: t('sales.shipments.view.title'),
      columns: TableColumns,
      dataSource: tableData,
      pagination: false,
      showIndexColumn: false,
      bordered: true,
    });
    const getTitle = ref(t('sales.shipments.receipt'));
    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false, destroyOnClose: true, width: 1200, showOkBtn: false});
      const res = await getLinkShipmentsDetail(data.receiptNumber);
      tableData.value = res.data.tableData;
      receiptNumber.value = res.data.receiptNumber;
      customerName.value = res.data.customerName;
      receiptDate.value = res.data.receiptDate;
      collectOfferRate.value = res.data.collectOfferRate;
      collectOfferAmount.value = res.data.collectOfferAmount;
      collectOfferLastAmount.value = res.data.collectOfferLastAmount;
      otherAmount.value = res.data.otherAmount;
      thisCollectAmount.value = res.data.thisCollectAmount;
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
      const file: any = await exportShipmentsDetail(receiptNumber.value)
      if (file.size > 0) {
        const blob = new Blob([file]);
        const link = document.createElement("a");
        const timestamp = getTimestamp(new Date());
        link.href = URL.createObjectURL(blob);
        link.download = t('sales.shipments.export.exportData') + timestamp + ".xlsx";
        link.target = "_blank";
        link.click();
      }
    }

    const flexContainer = 'display: flex; justify-content: space-between; border-bottom: 1px solid #ddd; padding: 8px;';
    const flexItem = 'display: flex; flex-direction: column; justify-content: space-between; font-size: 12px;';
    function primaryPrint() {
      const header = `
  <div style="${flexContainer}">
    <div style="${flexItem}">${t('sales.shipments.table.receiptNumber')}：${receiptNumber.value}</div>
    <div style="${flexItem}">${t('sales.shipments.view.receiptAmount')}：${collectOfferLastAmount.value}</div>
    <div style="${flexItem}">${t('sales.shipments.view.receiptDate')}：${receiptDate.value}</div>
  </div>
  <div style="${flexContainer}">
    <div style="${flexItem}">${t('sales.shipments.view.settlementAccount')}：${accountName.value}</div>
    <div style="${flexItem}">${t('sales.shipments.view.discountRate')}：${collectOfferRate.value}</div>
    <div style="${flexItem}">${t('sales.shipments.view.collectionDiscount')}：${collectOfferAmount.value}</div>
    <div style="${flexItem}">${t('sales.shipments.view.otherFees')}：${otherAmount.value}</div>
  </div>
  <div style="${flexContainer}">
    <div style="${flexItem}">${t('sales.shipments.view.thisTimeCollectAmount')}：${thisCollectAmount.value}</div>
    <div style="${flexItem}">${t('sales.shipments.view.thisTimeArrearsAmount')}：${thisArrearsAmount.value}</div>
    <div style="${flexItem}">${t('sales.shipments.view.customer')}：${customerName.value}</div>
    <div style="${flexItem}">${t('sales.shipments.view.remark')}：${remark.value}</div>
  </div>
`;
      printJS({
        documentTitle: "EAIRP " + t('sales.shipments.detail'),
        header: header,
        properties: TableColumns.map((item) => {
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
      customerName,
      collectOfferRate,
      receiptDate,
      collectOfferAmount,
      collectOfferLastAmount,
      otherAmount,
      thisCollectAmount,
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
