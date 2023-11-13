<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <div class="components-page-header-demo-responsive" style="border: 1px solid rgb(235, 237, 240)">
      <a-page-header
          title="采购入库-详情"
          :sub-title= "receiptNumber">
        <template #extra>
          <a-button key="1">导出</a-button>
          <a-button key="1">普通打印</a-button>
          <a-button key="1">三联打印</a-button>
          <a-button key="2" type="primary">发起流程审批</a-button>
        </template>
        <a-descriptions size="small" :column="4">
          <a-descriptions-item label="供应商">{{ supplierName }}</a-descriptions-item>
          <a-descriptions-item label="单据日期">{{ receiptDate }}</a-descriptions-item>
          <a-descriptions-item label="采购单据">
            <a @click="viewOrderReceipt">{{ otherReceipt }}</a>
          </a-descriptions-item>
          <a-descriptions-item label="优惠率">{{ paymentRate }}</a-descriptions-item>
          <a-descriptions-item label="付款优惠">{{ paymentAmount }}</a-descriptions-item>
          <a-descriptions-item label="优惠后金额">{{ paymentLastAmount }}</a-descriptions-item>
          <a-descriptions-item label="其它费用">{{ otherAmount }}</a-descriptions-item>
          <a-descriptions-item label="结算账户">{{ accountName }}</a-descriptions-item>
          <a-descriptions-item label="本次付款">{{ thisPaymentAmount }}</a-descriptions-item>
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
import {getLinkStorageDetail} from "@/api/purchase/storage";
import {purchaseOrderTableColumns} from "@/views/purchase/order/purchaseOrder.data";
import ViewOrderModal from "@/views/purchase/order/components/ViewOrderModal.vue";
import {
  Descriptions,
  DescriptionsItem,
  PageHeader,
  Statistic,
} from 'ant-design-vue';

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
    const deposit = ref('');
    const accountName = ref('');
    const remark = ref('')
    const status = ref();
    const [viewOrderReceiptModal, {openModal: openViewOrderModal}] = useModal();
    const tableData = ref([]);
    const [registerTable] = useTable({
      title: '采购入库表数据',
      columns: purchaseOrderTableColumns,
      dataSource: tableData,
      pagination: false,
      showIndexColumn: false,
      bordered: true,
    });
    const getTitle = ref('单据详情');
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

    return {
      receiptNumber,
      otherReceipt,
      supplierName,
      paymentRate,
      receiptDate,
      paymentAmount,
      deposit,
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
      viewOrderReceipt
    };
  },
});
</script>
<style scoped>

</style>
