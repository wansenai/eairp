<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button @click="exportTable" v-text="t('reports.customerBill.export')"/>
        <a-button @click="primaryPrint" type="primary" v-text="t('reports.customerBill.regularPrint')"/>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'customerId'">
          <a @click="openReceipt(record.customerId)"> {{ t('reports.customerBill.table.detail') }} </a>
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
import {useI18n} from "vue-i18n";
import {useLocaleStore} from "@/store/modules/locale";

export default defineComponent({
  name: 'CustomerBill',
  components: {
    CustomerBillDetailModal, Tag, TableAction, BasicTable
  },
  setup() {
    const { t } = useI18n();
    const printTableData = ref<any[]>([]);
    const { createMessage } = useMessage();
    const [handleRegister, {openModal}] = useModal();
    const printFirstQuarterReceivable = ref(0);
    const printSecondQuarterReceivable = ref(0);
    const printThirdQuarterReceivable = ref(0);
    const printFourthQuarterReceivable = ref(0);
    const printTotalQuarterArrears = ref(0);
    const printTotalQuarterReceivable = ref(0);
    const printRemainingReceivableArrears = ref(0);
    const amountSymbol = ref<string>('')
    const localeStore = useLocaleStore().getLocale;
    if(localeStore === 'zh_CN') {
      amountSymbol.value = '￥'
    } else if (localeStore === 'en') {
      amountSymbol.value = '$'
    }
    const [registerTable, {reload, getForm, getDataSource}] = useTable({
      title: t('reports.customerBill.title'),
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
      printFirstQuarterReceivable.value = firstQuarterReceivable;
      printSecondQuarterReceivable.value = secondQuarterReceivable;
      printThirdQuarterReceivable.value = thirdQuarterReceivable;
      printFourthQuarterReceivable.value = fourthQuarterReceivable;
      printTotalQuarterArrears.value = totalQuarterArrears;
      printTotalQuarterReceivable.value = totalQuarterReceivable;
      printRemainingReceivableArrears.value = remainingReceivableArrears;
      printTableData.value = tableData;
      return [
        {
          _index: t('reports.customerBill.table.total'),
          firstQuarterReceivable: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(firstQuarterReceivable), {digits: 2})}`,
          secondQuarterReceivable: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(secondQuarterReceivable), {digits: 2})}`,
          thirdQuarterReceivable: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(thirdQuarterReceivable), {digits: 2})}`,
          fourthQuarterReceivable: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(fourthQuarterReceivable), {digits: 2})}`,
          totalQuarterArrears: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(totalQuarterArrears), {digits: 2})}`,
          totalQuarterReceivable: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(totalQuarterReceivable), {digits: 2})}`,
          remainingReceivableArrears: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(remainingReceivableArrears), {digits: 2})}`
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
        createMessage.warn(t('reports.customerBill.notice'));
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
          link.download = t('reports.customerBill.data') + timestamp + ".xlsx";
          link.target = "_blank";
          link.click();
        }
      });
    }

    function primaryPrint() {
      const printColumns = customerBillColumns.filter(item => item.dataIndex !== 'customerId');
      printTableData.value.push({
        firstQuarterReceivable: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(printFirstQuarterReceivable.value), {digits: 2})}`,
        secondQuarterReceivable: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(printSecondQuarterReceivable.value), {digits: 2})}`,
        thirdQuarterReceivable: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(printThirdQuarterReceivable.value), {digits: 2})}`,
        fourthQuarterReceivable: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(printFourthQuarterReceivable.value), {digits: 2})}`,
        totalQuarterArrears: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(printTotalQuarterArrears.value), {digits: 2})}`,
        totalQuarterReceivable: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(printTotalQuarterReceivable.value), {digits: 2})}`,
        remainingReceivableArrears: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(printRemainingReceivableArrears.value), {digits: 2})}`,
        customerName: t('reports.customerBill.table.total'),
        contactName: '',
        contactPhone: '',
        email: '',
      });
      printJS({
        documentTitle: "EAIRP " + t('reports.customerBill.detail'),
        properties: printColumns.map(item => {
          return {field: item.dataIndex, displayName: item.title}
        }),
        printable: printTableData.value,
        gridHeaderStyle: 'border: 1px solid #ddd; font-size: 12px; text-align: center; padding: 8px;',
        gridStyle: 'border: 1px solid #ddd; font-size: 12px; text-align: center; padding: 8px;',
        type: 'json',
      });
      printTableData.value.pop();
    }

    return {
      t,
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