<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <div class="components-page-header-demo-responsive" style="border: 1px solid rgb(235, 237, 240)">
      <a-page-header
          :title="t('retail.shipments.detail')"
          :sub-title="receiptNumber">
        <template #extra>
          <a-button @click="exportTable" v-text="t('retail.shipments.export.name')"/>
          <a-button @click="primaryPrint" type="primary" v-text="t('retail.regularPrint')"/>
<!--          <a-button key="triplePrint">三联打印</a-button>-->
<!--          <a-button key="2" type="primary">发起流程审批</a-button>-->
        </template>
        <a-descriptions size="small" :column="3">
          <a-descriptions-item :label="t('retail.shipments.view.member')">{{ memberName }}</a-descriptions-item>
          <a-descriptions-item :label="t('retail.shipments.view.receiptDate')">{{ receiptDate }}</a-descriptions-item>
          <a-descriptions-item :label="t('retail.shipments.view.collectionType')">{{ paymentType }}</a-descriptions-item>
          <a-descriptions-item :label="t('retail.shipments.view.receiptAmount')">{{ receiptAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('retail.shipments.view.collectionAmount')">{{ collectAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('retail.shipments.view.changeAmount')">{{ backAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('retail.shipments.view.collectionAccount')">{{ accountName }}</a-descriptions-item>
          <a-descriptions-item :label="t('retail.shipments.view.refundReceipt')">
            <a @click="viewRefundReceipt">{{ otherReceipt }}</a>
          </a-descriptions-item>
          <a-descriptions-item :label="t('retail.shipments.view.remark')">
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
                :title="t('retail.shipments.view.status')"
                :value="status === 1 ? t('sys.table.audited') : t('sys.table.unaudited')"
                :value-style="status === 1 ? { color: '#3f8600' } : { color: '#cf1322' }"
                :style="{
                marginRight: '32px',
                color: 'green',
              }"
            />
            <a-statistic :title="t('retail.shipments.view.receiptAmount')"
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
</template>
<script lang="ts">
import {defineComponent, ref} from 'vue';
import {BasicTable, useTable} from '/src/components/Table';
import {BasicModal, useModalInner} from "@/components/Modal";
import {getLinkShipmentsDetail, exportShipmentsDetail} from "@/api/retail/shipments";
import printJS from 'print-js';
import {
  Descriptions,
  DescriptionsItem,
  PageHeader,
  Statistic,
} from 'ant-design-vue';
import {retailShipmentsTableColumns} from "@/views/retail/shipments/shipments.data";
import {getTimestamp} from "@/utils/dateUtil";
import {useI18n} from "vue-i18n";

export default defineComponent({
  name: 'ViewShipmentModal',
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
    const memberName = ref<any>('');
    const paymentType = ref('');
    const receiptDate = ref('');
    const receiptType = ref('');
    const receiptAmount = ref<any>('');
    const collectAmount = ref('');
    const backAmount = ref<any>('');
    const accountName = ref('');
    const remark = ref('')
    const status = ref();

    const tableData = ref<any>([]);
    const [registerTable] = useTable({
      title: t('retail.shipments.detail'),
      columns: retailShipmentsTableColumns,
      dataSource: tableData,
      pagination: false,
      showIndexColumn: false,
      bordered: true,
    });
    const getTitle = ref(t('retail.shipments.receipt'));
    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false, destroyOnClose: true, width: 1200, showOkBtn: false});
      const res = await getLinkShipmentsDetail(data.receiptNumber);
      tableData.value = res.data.tableData;
      receiptNumber.value = res.data.receiptNumber;
      memberName.value = res.data.memberName;
      paymentType.value = res.data.paymentType;
      receiptDate.value = res.data.receiptDate;
      receiptAmount.value = res.data.receiptAmount;
      collectAmount.value = res.data.collectAmount;
      backAmount.value = res.data.backAmount;
      accountName.value = res.data.accountName;
      otherReceipt.value = res.data.otherReceipt;
      remark.value = res.data.remark;
      status.value = res.data.status;
    });

    function handleSubmit() {
      closeModal();
    }

    function viewRefundReceipt() {
      closeModal();
    }

    async function exportTable() {
      const file: any = await exportShipmentsDetail(receiptNumber.value)
      if (file.size > 0) {
        const blob = new Blob([file]);
        const link = document.createElement("a");
        const timestamp = getTimestamp(new Date());
        link.href = URL.createObjectURL(blob);
        link.download = t('retail.shipments.export.exportData') + timestamp + ".xlsx";
        link.target = "_blank";
        link.click();
      }
    }

    const flexContainer = 'display: flex; justify-content: space-between; border-bottom: 1px solid #ddd; padding: 8px;';
    const flexItem = 'display: flex; flex-direction: column; justify-content: space-between; font-size: 12px;';
    function primaryPrint() {
      const header = `
  <div style="${flexContainer}">
    <div style="${flexItem}">${t('retail.shipments.table.receiptNumber')}：${receiptNumber.value}</div>
    <div style="${flexItem}">${t('retail.shipments.view.receiptAmount')}：${receiptAmount.value}</div>
    <div style="${flexItem}">${t('retail.shipments.view.receiptDate')}：${receiptDate.value}</div>
  </div>
  <div style="${flexContainer}">
    <div style="${flexItem}">${t('retail.shipments.view.collectionAccount')}：${accountName.value}</div>
    <div style="${flexItem}">${t('retail.shipments.view.collectionType')}：${paymentType.value}</div>
    <div style="${flexItem}">${t('retail.shipments.view.collectionAmount')}：${collectAmount.value}</div>
  </div>
  <div style="${flexContainer}">
    <div style="${flexItem}">${t('retail.shipments.view.changeAmount')}：${backAmount.value}</div>
    <div style="${flexItem}">${t('retail.shipments.view.member')}：${memberName.value}</div>
    <div style="${flexItem}">${t('retail.shipments.view.remark')}：${remark.value}</div>
  </div>
`;
      printJS({
        documentTitle: "EAIRP " + t('retail.shipments.export.exportData'),
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

    function triplePrint() {

    }

    return {
      t,
      receiptNumber,
      otherReceipt,
      memberName,
      paymentType,
      collectAmount,
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
      viewRefundReceipt,
      exportTable,
      primaryPrint
    };
  },
});
</script>
<style scoped>

</style>
