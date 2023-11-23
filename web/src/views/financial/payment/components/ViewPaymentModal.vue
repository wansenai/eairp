<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <div class="components-page-header-demo-responsive" style="border: 1px solid rgb(235, 237, 240)">
      <a-page-header
          title="付款单-详情"
          :sub-title= "receiptNumber">
        <template #extra>
          <a-button key="1" type="primary">打印</a-button>
        </template>
        <a-descriptions size="small" :column="3">
          <a-descriptions-item label="供应商">{{ supplierName }}</a-descriptions-item>
          <a-descriptions-item label="单据日期">{{ receiptDate }}</a-descriptions-item>
          <a-descriptions-item label="财务人员">{{ financialPersonName }}</a-descriptions-item>
          <a-descriptions-item label="付款账户">{{ paymentAccountName }}</a-descriptions-item>
          <a-descriptions-item label="合计付款">{{ totalPaymentAmount }}</a-descriptions-item>
          <a-descriptions-item label="优惠金额">{{ discountAmount }}</a-descriptions-item>
          <a-descriptions-item label="实际付款">{{ actualPaymentAmount }}</a-descriptions-item>
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
import {getPaymentDetailById} from "@/api/financial/payment";
import {
  Descriptions,
  DescriptionsItem,
  PageHeader,
  Statistic,
} from 'ant-design-vue';
import {paymentReceiptTableColumns} from "@/views/financial/payment/payment.data";
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
      title: '付款单详情数据',
      columns: paymentReceiptTableColumns,
      dataSource: tableData,
      pagination: false,
      showIndexColumn: false,
      bordered: true,
      canResize: false,
    });
    const getTitle = ref('单据详情');
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

    return {
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
    };
  },
});
</script>
<style scoped>

</style>
