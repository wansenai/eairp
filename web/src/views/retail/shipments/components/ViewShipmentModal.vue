<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <div class="components-page-header-demo-responsive" style="border: 1px solid rgb(235, 237, 240)">
      <a-page-header
          title="零售出库-详情"
          :sub-title= "receiptNumber">
        <template #extra>
          <a-button key="1">导出</a-button>
          <a-button key="1">普通打印</a-button>
          <a-button key="1">三联打印</a-button>
          <a-button key="2" type="primary">发起流程审批</a-button>
        </template>
        <a-descriptions size="small" :column="3">
          <a-descriptions-item label="会员">{{ memberName }}</a-descriptions-item>
          <a-descriptions-item label="单据日期">{{ receiptDate }}</a-descriptions-item>
          <a-descriptions-item label="收款类型">{{ paymentType }}</a-descriptions-item>
          <a-descriptions-item label="单据金额">{{ receiptAmount }}</a-descriptions-item>
          <a-descriptions-item label="收款金额">{{ collectAmount }}</a-descriptions-item>
          <a-descriptions-item label="找零">{{ backAmount }}</a-descriptions-item>
          <a-descriptions-item label="收款账户">{{ accountName }}</a-descriptions-item>
          <a-descriptions-item label="退货单号">
            <a @click="viewRefundReceipt">{{ otherReceipt }}</a>
          </a-descriptions-item>
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
import {getLinkShipmentsDetail} from "@/api/retail/shipments";
import {
  Descriptions,
  DescriptionsItem,
  PageHeader,
  Statistic,
} from 'ant-design-vue';
import {retailShipmentsTableColumns} from "@/views/retail/shipments/shipments.data";

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
    const receiptNumber = ref('');
    const otherReceipt = ref('');
    const memberName = ref(0);
    const paymentType = ref('');
    const receiptDate = ref('');
    const receiptType = ref('');
    const receiptAmount = ref('');
    const collectAmount = ref('');
    const backAmount = ref('');
    const accountName = ref('');
    const remark = ref('')
    const status = ref();

    const tableData = ref([]);
    const [registerTable] = useTable({
      title: '出库商品表数据',
      columns: retailShipmentsTableColumns,
      dataSource: tableData,
      pagination: false,
      showIndexColumn: false,
      bordered: true,
    });
    const getTitle = ref('单据详情');
    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false, destroyOnClose: true, width: 1200, showOkBtn: false});
      const res = await getLinkShipmentsDetail(data.receiptNumber);
      console.info(res.data);
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

    return {
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
      viewRefundReceipt
    };
  },
});
</script>
<style scoped>

</style>
