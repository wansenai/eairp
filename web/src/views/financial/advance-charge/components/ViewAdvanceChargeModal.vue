<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <div class="components-page-header-demo-responsive" style="border: 1px solid rgb(235, 237, 240)">
      <a-page-header
          title="收预付款单-详情"
          :sub-title= "receiptNumber">
        <template #extra>
          <a-button @click="exportTable">导出</a-button>
          <a-button @click="primaryPrint" type="primary">普通打印</a-button>
          <!--          <a-button key="triplePrint">三联打印</a-button>-->
          <!--          <a-button key="2" type="primary">发起流程审批</a-button>-->
        </template>
        <a-descriptions size="small" :column="3">
          <a-descriptions-item label="付款会员">{{ memberName }}</a-descriptions-item>
          <a-descriptions-item label="单据日期">{{ receiptDate }}</a-descriptions-item>
          <a-descriptions-item label="财务人员">{{ financialPersonnel }}</a-descriptions-item>
          <a-descriptions-item label="合计金额">{{ totalAmount }}</a-descriptions-item>
          <a-descriptions-item label="收款金额">{{ collectedAmount }}</a-descriptions-item>
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
                         :value="collectedAmount"/>
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
import {getAdvanceDetail, exportAdvanceDetail} from "@/api/financial/advance";
import {
  Descriptions,
  DescriptionsItem,
  PageHeader,
  Statistic,
} from 'ant-design-vue';
import {advanceChargeTableColumns} from "@/views/financial/advance-charge/advance.data";
import printJS from "print-js";
import {getTimestamp} from "@/utils/dateUtil";

export default defineComponent({
  name: 'ViewIncomeModal',
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
    const memberName = ref('');
    const receiptDate = ref('');
    const financialPersonnel = ref('');
    const totalAmount = ref('');
    const collectedAmount = ref('');
    const remark = ref('')
    const tableData = ref([]);
    const status = ref(-1);
    const [registerTable] = useTable({
      title: '收预付款单据详情数据',
      columns: advanceChargeTableColumns,
      dataSource: tableData,
      pagination: false,
      showIndexColumn: false,
      bordered: true,
      canResize: false,
    });
    const getTitle = ref('单据详情');
    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false, destroyOnClose: true, width: 1000, showOkBtn: false});
      const res = await getAdvanceDetail(data.id);
      tableData.value = res.data.tableData;
      receiptNumber.value = res.data.receiptNumber;
      memberName.value = res.data.memberName;
      financialPersonnel.value = res.data.financialPersonnel;
      receiptDate.value = res.data.receiptDate;
      totalAmount.value = res.data.totalAmount;
      collectedAmount.value = res.data.collectedAmount;
      remark.value = res.data.remark;
      status.value = res.data.status;
    });

    function handleSubmit() {
      closeModal();
    }

    async function exportTable() {
      const file: any = await exportAdvanceDetail(receiptNumber.value)
      if (file.size > 0) {
        const blob = new Blob([file]);
        const link = document.createElement("a");
        const timestamp = getTimestamp(new Date());
        link.href = URL.createObjectURL(blob);
        link.download = "收预付款单据详情" + timestamp + ".xlsx";
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
    <div style="${flexItem}">单据日期：${receiptDate.value}</div>
    <div style="${flexItem}">单据金额：${totalAmount.value}</div>
  </div>
  <div style="${flexContainer}">
    <div style="${flexItem}">付款会员：${memberName.value}</div>
    <div style="${flexItem}">财务人员：${financialPersonnel.value}</div>
    <div style="${flexItem}">备注：${remark.value}</div>
  </div>
`;
      printJS({
        documentTitle: "EAIRP (收预付款单单据-详情)",
        header: header,
        properties: advanceChargeTableColumns.map((item) => {
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
      receiptDate,
      financialPersonnel,
      collectedAmount,
      totalAmount,
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
