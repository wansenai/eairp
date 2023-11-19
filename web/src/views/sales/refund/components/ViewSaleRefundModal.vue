<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <div class="components-page-header-demo-responsive" style="border: 1px solid rgb(235, 237, 240)">
      <a-page-header
          title="销售退货-详情"
          :sub-title= "receiptNumber">
        <template #extra>
          <a-button key="1">导出</a-button>
          <a-button key="1">普通打印</a-button>
          <a-button key="1">三联打印</a-button>
          <a-button key="2" type="primary">发起流程审批</a-button>
        </template>
        <a-descriptions size="small" :column="4">
          <a-descriptions-item label="供应商">{{ customerName }}</a-descriptions-item>
          <a-descriptions-item label="单据日期">{{ receiptDate }}</a-descriptions-item>
          <a-descriptions-item label="出库单据">
            <a @click="viewShipmentReceipt">{{ otherReceipt }}</a>
          </a-descriptions-item>
          <a-descriptions-item label="优惠率">{{ refundOfferRate }}</a-descriptions-item>
          <a-descriptions-item label="退款优惠">{{ refundOfferAmount }}</a-descriptions-item>
          <a-descriptions-item label="优惠后金额">{{ refundLastAmount }}</a-descriptions-item>
          <a-descriptions-item label="其它费用">{{ otherAmount }}</a-descriptions-item>
          <a-descriptions-item label="结算账户">{{ accountName }}</a-descriptions-item>
          <a-descriptions-item label="本次退款">{{ thisRefundAmount }}</a-descriptions-item>
          <a-descriptions-item label="本次欠款">{{ thisArrearsAmount }}</a-descriptions-item>
          <a-descriptions-item label="销售人员">{{  }}</a-descriptions-item>
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
                         :value="thisRefundAmount"/>
          </div>
        </div>
      </a-page-header>
      <BasicTable @register="registerTable">
      </BasicTable>
    </div>
  </BasicModal>
  <ViewShipmentsModal @register="viewShipmentsReceiptModal"/>
</template>
<script lang="ts">
import {defineComponent, ref} from 'vue';
import {BasicTable, useTable} from '/src/components/Table';
import {BasicModal, useModal, useModalInner} from "@/components/Modal";
import {getLinkRefundDetail} from "@/api/sale/refund";
import {TableColumns} from "@/views/sales/order/sales.data";
import ViewShipmentsModal from "@/views/sales/shipments/components/ViewSaleShipmentsModal.vue";
import {
  Descriptions,
  DescriptionsItem,
  PageHeader,
  Statistic,
} from 'ant-design-vue';

export default defineComponent({
  name: 'ViewSaleRefundModal',
  components: {
    BasicModal,
    BasicTable,
    ViewShipmentsModal,
    'a-page-header': PageHeader,
    'a-descriptions': Descriptions,
    'a-descriptions-item': DescriptionsItem,
    'a-statistic': Statistic,
  },
  setup() {
    const receiptNumber = ref('');
    const otherReceipt = ref('');
    const customerName = ref(0);
    const refundOfferRate = ref('');
    const receiptDate = ref('');
    const refundOfferAmount = ref('');
    const refundLastAmount = ref('');
    const otherAmount = ref('');
    const thisRefundAmount = ref('');
    const thisArrearsAmount = ref('');
    const accountName = ref('');
    const remark = ref('')
    const status = ref();
    const [viewShipmentsReceiptModal, {openModal: openViewShipmentsReceiptModal}] = useModal();
    const tableData = ref([]);
    const [registerTable] = useTable({
      title: '销售退货表数据',
      columns: TableColumns,
      dataSource: tableData,
      pagination: false,
      showIndexColumn: false,
      bordered: true,
    });
    const getTitle = ref('单据详情');
    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false, destroyOnClose: true, width: 1200, showOkBtn: false});
      const res = await getLinkRefundDetail(data.receiptNumber);
      tableData.value = res.data.tableData;
      receiptNumber.value = res.data.receiptNumber;
      customerName.value = res.data.customerName;
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

    function viewShipmentReceipt() {
      closeModal();
      openViewShipmentsReceiptModal(true, {
        receiptNumber: otherReceipt.value
      });
    }

    function handleSubmit() {
      closeModal();
    }

    return {
      receiptNumber,
      otherReceipt,
      customerName,
      refundOfferRate,
      receiptDate,
      refundOfferAmount,
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
      viewShipmentsReceiptModal,
      viewShipmentReceipt
    };
  },
});
</script>
<style scoped>

</style>
