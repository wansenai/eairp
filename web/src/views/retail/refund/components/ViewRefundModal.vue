<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <div class="components-page-header-demo-responsive" style="border: 1px solid rgb(235, 237, 240)">
      <a-page-header
          title="零售退货入库-详情"
          :sub-title= "receiptNumber">
        <template #extra>
          <a-button @click="exportTable">导出</a-button>
          <a-button @click="primaryPrint" type="primary">普通打印</a-button>
          <!--          <a-button key="triplePrint">三联打印</a-button>-->
          <!--          <a-button key="2" type="primary">发起流程审批</a-button>-->
        </template>
        <a-descriptions size="small" :column="3">
          <a-descriptions-item label="会员">{{ memberName }}</a-descriptions-item>
          <a-descriptions-item label="单据日期">{{ receiptDate }}</a-descriptions-item>
          <a-descriptions-item label="单据金额">{{ receiptAmount }}</a-descriptions-item>
          <a-descriptions-item label="付款金额">{{ paymentAmount }}</a-descriptions-item>
          <a-descriptions-item label="找零">{{ backAmount }}</a-descriptions-item>
          <a-descriptions-item label="付款账户">{{ accountName }}</a-descriptions-item>
          <a-descriptions-item label="关联单据">
            <a @click="viewShipmentReceipt">{{ otherReceipt }}</a>
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
      title: '退货商品表数据',
      columns: retailShipmentsTableColumns,
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
        link.download = "零售退货单据详情" + timestamp + ".xlsx";
        link.target = "_blank";
        link.click();
      }
    }

    const flexContainer = 'display: flex; justify-content: space-between; border-bottom: 1px solid #ddd; padding: 8px;';
    const flexItem = 'display: flex; flex-direction: column; justify-content: space-between; font-size: 12px;';
    function primaryPrint() {
      const header = `
  <div style="${flexContainer}">
    <div style="${flexItem}">单据编号：${receiptNumber.value}</div>
    <div style="${flexItem}">单据金额：${receiptAmount.value}</div>
    <div style="${flexItem}">单据日期：${receiptDate.value}</div>
  </div>
  <div style="${flexContainer}">
    <div style="${flexItem}">付款账户：${accountName.value}</div>
    <div style="${flexItem}">付款金额：${paymentType.value}</div>
    <div style="${flexItem}">付款金额：${paymentAmount.value}</div>
  </div>
  <div style="${flexContainer}">
    <div style="${flexItem}">找零：${backAmount.value}</div>
    <div style="${flexItem}">会员：${memberName.value}</div>
    <div style="${flexItem}">备注：${remark.value}</div>
  </div>
`;
      printJS({
        documentTitle: "EAIRP (零售退货单据-详情)",
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
