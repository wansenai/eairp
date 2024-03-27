<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <div class="components-page-header-demo-responsive" style="border: 1px solid rgb(235, 237, 240)">
      <a-page-header
          :title="t('financial.expense.expenseReceiptDetail')"
          :sub-title= "receiptNumber">
        <template #extra>
          <a-button @click="exportTable" v-text="t('financial.expense.export.name')"/>
          <a-button @click="primaryPrint" type="primary" v-text="t('financial.regularPrint')"/>
          <!--          <a-button key="triplePrint">三联打印</a-button>-->
          <!--          <a-button key="2" type="primary">发起流程审批</a-button>-->
        </template>
        <a-descriptions size="small" :column="3">
          <a-descriptions-item :label="t('financial.expense.header.correspondenceUnit')">{{ relatedPersonName }}</a-descriptions-item>
          <a-descriptions-item :label="t('financial.expense.header.receiptDate')">{{ receiptDate }}</a-descriptions-item>
          <a-descriptions-item :label="t('financial.expense.header.financialPerson')">{{ financialPersonName }}</a-descriptions-item>
          <a-descriptions-item :label="t('financial.expense.header.expenseAccount')">{{ expenseAccountName }}</a-descriptions-item>
          <a-descriptions-item :label="t('financial.expense.table.expenseAmount')">{{ expenseAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('financial.expense.header.remark')">
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
                :title="t('financial.expense.header.remark')"
                :value="status === 1 ? t('sys.table.audited') : t('sys.table.unaudited')"
                :value-style="status === 1 ? { color: '#3f8600' } : { color: '#cf1322' }"
                :style="{
                marginRight: '32px',
                color: 'green',
              }"
            />
            <a-statistic :title="t('financial.expense.table.expenseAmount')"
                         prefix="￥"
                         :value-style="status === 1 ? { color: '#3f8600' } : { color: '#cf1322' }"
                         :value="expenseAmount"/>
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
import {getExpenseDetailById, exportExpenseDetail} from "@/api/financial/expense";
import {
  Descriptions,
  DescriptionsItem,
  PageHeader,
  Statistic,
} from 'ant-design-vue';
import {expenseReceiptTableColumns} from "@/views/financial/expense/expense.data";
import printJS from "print-js";
import {getTimestamp} from "@/utils/dateUtil";
import {useI18n} from "vue-i18n";
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
    const { t } = useI18n();
    const receiptNumber = ref('');
    const relatedPersonName = ref('');
    const receiptDate = ref('');
    const financialPersonName = ref('');
    const expenseAccountName = ref('');
    const expenseAmount = ref('');
    const remark = ref('')
    const tableData = ref([]);
    const status = ref(-1);
    const [registerTable] = useTable({
      title: t('financial.expense.receiptDetail'),
      columns: expenseReceiptTableColumns,
      dataSource: tableData,
      pagination: false,
      showIndexColumn: false,
      bordered: true,
      canResize: false,
    });
    const getTitle = ref('单据详情');
    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false, destroyOnClose: true, width: 1000, showOkBtn: false});
      const res = await getExpenseDetailById(data.id);
      tableData.value = res.data.tableData;
      receiptNumber.value = res.data.receiptNumber;
      relatedPersonName.value = res.data.relatedPersonName;
      financialPersonName.value = res.data.financialPersonName;
      receiptDate.value = res.data.receiptDate;
      expenseAccountName.value = res.data.expenseAccountName;
      expenseAmount.value = res.data.expenseAmount;
      remark.value = res.data.remark;
      status.value = res.data.status;
    });

    function handleSubmit() {
      closeModal();
    }

    async function exportTable() {
      const file: any = await exportExpenseDetail(receiptNumber.value)
      if (file.size > 0) {
        const blob = new Blob([file]);
        const link = document.createElement("a");
        const timestamp = getTimestamp(new Date());
        link.href = URL.createObjectURL(blob);
        link.download = t('financial.expense.receiptDetail') + timestamp + ".xlsx";
        link.target = "_blank";
        link.click();
      }
    }

    const flexContainer = 'display: flex; justify-content: space-between; border-bottom: 1px solid #ddd; padding: 8px;';
    const flexItem = 'display: flex; flex-direction: column; justify-content: space-between; font-size: 12px;';
    function primaryPrint() {
      const header = `
  <div style="${flexContainer}">
    <div style="${flexItem}">${t('financial.expense.header.receiptNumber')}：${receiptNumber.value}</div>
    <div style="${flexItem}">${t('financial.expense.header.receiptDate')}：${receiptDate.value}</div>
    <div style="${flexItem}">${t('financial.expense.table.expenseAmount')}：${expenseAmount.value}</div>
  </div>
  <div style="${flexContainer}">
    <div style="${flexItem}">${t('financial.expense.header.correspondenceUnit')}：${relatedPersonName.value}</div>
    <div style="${flexItem}">${t('financial.expense.header.expenseAccount')}：${expenseAccountName.value}</div>
    <div style="${flexItem}">${t('financial.expense.header.financialPerson')}：${financialPersonName.value}</div>
    <div style="${flexItem}">${t('financial.expense.header.remark')}：${remark.value}</div>
  </div>
`;
      printJS({
        documentTitle: "EAIRP " + t('financial.expense.receiptDetail'),
        header: header,
        properties: expenseReceiptTableColumns.map((item) => {
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
      relatedPersonName,
      receiptDate,
      financialPersonName,
      expenseAccountName,
      expenseAmount,
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
