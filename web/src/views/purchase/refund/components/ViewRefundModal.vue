<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <div class="components-page-header-demo-responsive" style="border: 1px solid rgb(235, 237, 240)">
      <a-page-header
          title="采购退货-详情"
          :sub-title= "receiptNumber">
        <template #extra>
          <a-button @click="exportTable">导出</a-button>
          <a-button @click="primaryPrint" type="primary">普通打印</a-button>
          <!--          <a-button key="triplePrint">三联打印</a-button>-->
          <!--          <a-button key="2" type="primary">发起流程审批</a-button>-->
        </template>
        <a-descriptions size="small" :column="4">
          <a-descriptions-item label="供应商">{{ supplierName }}</a-descriptions-item>
          <a-descriptions-item label="单据日期">{{ receiptDate }}</a-descriptions-item>
          <a-descriptions-item label="入库单据">
            <a @click="viewOrderReceipt">{{ otherReceipt }}</a>
          </a-descriptions-item>
          <a-descriptions-item label="优惠率">{{ refundOfferRate }}</a-descriptions-item>
          <a-descriptions-item label="退款优惠">{{ refundOfferAmount }}</a-descriptions-item>
          <a-descriptions-item label="优惠后金额">{{ refundLastAmount }}</a-descriptions-item>
          <a-descriptions-item label="其它费用">{{ otherAmount }}</a-descriptions-item>
          <a-descriptions-item label="结算账户">{{ accountName }}</a-descriptions-item>
          <a-descriptions-item label="本次退款">{{ thisRefundAmount }}</a-descriptions-item>
          <a-descriptions-item label="本次欠款">{{ thisArrearsAmount }}</a-descriptions-item>
          <a-descriptions-item label="备注">
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
                title="单据状态"
                :value="status === 1 ? '已审核' : '未审核'"
                :value-style="status === 1 ? { color: '#3f8600' } : { color: '#cf1322' }"
                :style="{
                marginRight: '32px',
                color: 'green',
              }"
            />
            <a-statistic title="单据金额"
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
import {getLinkRefundDetail} from "@/api/purchase/refund";
import {purchaseOrderTableColumns} from "@/views/purchase/order/purchaseOrder.data";
import ViewStorageModal from "@/views/purchase/storage/components/ViewStorageModal.vue";
import {
  Descriptions,
  DescriptionsItem,
  PageHeader,
  Statistic,
} from 'ant-design-vue';
import printJS from "print-js";

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
      title: '采购退货表数据',
      columns: purchaseOrderTableColumns,
      dataSource: tableData,
      pagination: false,
      showIndexColumn: false,
      bordered: true,
    });
    const getTitle = ref('单据详情');
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

    function exportTable() {

    }

    const flexContainer = 'display: flex; justify-content: space-between; border-bottom: 1px solid #ddd; padding: 8px;';
    const flexItem = 'display: flex; flex-direction: column; justify-content: space-between; font-size: 12px;';
    function primaryPrint() {
      const header = `
  <div style="${flexContainer}">
    <div style="${flexItem}">单据编号：${receiptNumber.value}</div>
    <div style="${flexItem}">单据金额：${refundLastAmount.value}</div>
    <div style="${flexItem}">单据日期：${receiptDate.value}</div>
  </div>
  <div style="${flexContainer}">
    <div style="${flexItem}">结算账户：${accountName.value}</div>
    <div style="${flexItem}">优惠率：${refundOfferRate.value}</div>
    <div style="${flexItem}">退款优惠：${refundOfferAmount.value}</div>
    <div style="${flexItem}">其他费用：${otherAmount.value}</div>
  </div>
  <div style="${flexContainer}">
    <div style="${flexItem}">本次退款：${thisRefundAmount.value}</div>
    <div style="${flexItem}">本次欠款：${thisArrearsAmount.value}</div>
    <div style="${flexItem}">供应商：${supplierName.value}</div>
    <div style="${flexItem}">备注：${remark.value}</div>
  </div>
`;
      printJS({
        documentTitle: "EAIRP (采购退货单据-详情)",
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
