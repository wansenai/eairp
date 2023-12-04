<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button @click="exportTable">导出</a-button>
        <a-button @click="primaryPrint" type="primary">普通打印</a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'customerId'">
          <a @click="openReceipt(record.customerId)"> 详情 </a>
        </template>
      </template>
    </BasicTable>
    <CustomerBillDetailModal @register="handleRegister"/>
  </div>
</template>
<div>
</div>

<script lang="ts">
import {defineComponent, ref} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {
  customerBillColumns,
  searchCustomerBillSchema,
} from "@/views/report/report.data";
import {Tag} from "ant-design-vue";
import {getCustomerBill, exportCustomerBill} from "@/api/report/report";
import XEUtils from "xe-utils";
import {useModal} from "@/components/Modal";
import CustomerBillDetailModal from "@/views/report/modal/CustomerBillDetailModal.vue";
import printJS from "print-js";
import {useMessage} from "@/hooks/web/useMessage";
import {getTimestamp} from "@/utils/dateUtil";

export default defineComponent({
  name: 'CustomerBill',
  components: {
    CustomerBillDetailModal, Tag, TableAction, BasicTable
  },
  setup() {
    const printTableData = ref<any[]>([]);
    const { createMessage } = useMessage();
    const [handleRegister, {openModal}] = useModal();
    const [registerTable, {reload, getForm, getDataSource}] = useTable({
      title: '客户对账报表',
      api: getCustomerBill,
      columns: customerBillColumns,
      formConfig: {
        labelWidth: 110,
        schemas: searchCustomerBillSchema,
        autoSubmitOnEnter: true,
      },
      bordered: true,
      useSearchForm: true,
      showTableSetting: true,
      striped: true,
      canResize: false,
      showIndexColumn: true,
      showSummary: true,
      summaryFunc: handleSummary,
    });

    function handleSummary(tableData: Recordable[]) {
      const firstQuarterReceivable = tableData.reduce((prev, next) => prev + next.firstQuarterReceivable, 0);
      const secondQuarterReceivable = tableData.reduce((prev, next) => prev + next.secondQuarterReceivable, 0);
      const thirdQuarterReceivable = tableData.reduce((prev, next) => prev + next.thirdQuarterReceivable, 0);
      const fourthQuarterReceivable = tableData.reduce((prev, next) => prev + next.fourthQuarterReceivable, 0);
      const totalQuarterArrears = tableData.reduce((prev, next) => prev + next.totalQuarterArrears, 0);
      const totalQuarterReceivable = tableData.reduce((prev, next) => prev + next.totalQuarterReceivable, 0);
      const remainingReceivableArrears = tableData.reduce((prev, next) => prev + next.remainingReceivableArrears, 0);
      printTableData.value = tableData;
      printTableData.value.push({
        firstQuarterReceivable: `￥${XEUtils.commafy(XEUtils.toNumber(firstQuarterReceivable), {digits: 2})}`,
        secondQuarterReceivable: `￥${XEUtils.commafy(XEUtils.toNumber(secondQuarterReceivable), {digits: 2})}`,
        thirdQuarterReceivable: `￥${XEUtils.commafy(XEUtils.toNumber(thirdQuarterReceivable), {digits: 2})}`,
        fourthQuarterReceivable: `￥${XEUtils.commafy(XEUtils.toNumber(fourthQuarterReceivable), {digits: 2})}`,
        totalQuarterArrears: `￥${XEUtils.commafy(XEUtils.toNumber(totalQuarterArrears), {digits: 2})}`,
        totalQuarterReceivable: `￥${XEUtils.commafy(XEUtils.toNumber(totalQuarterReceivable), {digits: 2})}`,
        remainingReceivableArrears: `￥${XEUtils.commafy(XEUtils.toNumber(remainingReceivableArrears), {digits: 2})}`,
        customerName: '合计',
        contactName: '',
        contactPhone: '',
        email: '',
      });
      return [
        {
          _index: '合计',
          firstQuarterReceivable: `￥${XEUtils.commafy(XEUtils.toNumber(firstQuarterReceivable), {digits: 2})}`,
          secondQuarterReceivable: `￥${XEUtils.commafy(XEUtils.toNumber(secondQuarterReceivable), {digits: 2})}`,
          thirdQuarterReceivable: `￥${XEUtils.commafy(XEUtils.toNumber(thirdQuarterReceivable), {digits: 2})}`,
          fourthQuarterReceivable: `￥${XEUtils.commafy(XEUtils.toNumber(fourthQuarterReceivable), {digits: 2})}`,
          totalQuarterArrears: `￥${XEUtils.commafy(XEUtils.toNumber(totalQuarterArrears), {digits: 2})}`,
          totalQuarterReceivable: `￥${XEUtils.commafy(XEUtils.toNumber(totalQuarterReceivable), {digits: 2})}`,
          remainingReceivableArrears: `￥${XEUtils.commafy(XEUtils.toNumber(remainingReceivableArrears), {digits: 2})}`
        },
      ];
    }

    async function handleSuccess() {
      reload();
    }

    async function handleCancel() {
      reload();
    }

    function openReceipt(customerId: string) {
      openModal(true, {
        customerId: customerId,
      });
    }

    function exportTable() {
      if (getDataSource() === undefined || getDataSource().length === 0) {
        createMessage.warn('当前查询条件下无数据可导出');
        return;
      }
      const data: any = getForm().getFieldsValue();
      exportCustomerBill(data).then(res => {
        const file: any = res;
        if (file.size > 0) {
          const blob = new Blob([file]);
          const link = document.createElement("a");
          link.href = URL.createObjectURL(blob);
          const timestamp = getTimestamp(new Date());
          link.download = "客户对账数据" + timestamp + ".xlsx";
          link.target = "_blank";
          link.click();
        }
      });
    }

    function primaryPrint() {
      const printColumns = customerBillColumns.filter(item => item.dataIndex !== 'customerId');
      printJS({
        documentTitle: "EAIRP (客户对账)",
        properties: printColumns.map(item => {
          return {field: item.dataIndex, displayName: item.title}
        }),
        printable: printTableData.value,
        gridHeaderStyle: 'border: 1px solid #ddd; font-size: 12px; text-align: center; padding: 8px;',
        gridStyle: 'border: 1px solid #ddd; font-size: 12px; text-align: center; padding: 8px;',
        type: 'json',
      });
    }

    return {
      openReceipt,
      registerTable,
      handleSuccess,
      handleCancel,
      handleRegister,
      exportTable,
      primaryPrint
    }
  }
})
</script>