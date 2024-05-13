<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button @click="exportTable" v-text="t('reports.supplierBill.export')"/>
        <a-button @click="primaryPrint" type="primary" v-text="t('reports.supplierBill.regularPrint')"/>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'supplierId'">
          <a @click="openReceipt(record.supplierId)"> {{ t('reports.supplierBill.table.detail') }} </a>
        </template>
      </template>
    </BasicTable>
    <SupplierBillDetailModal @register="handleRegister" />
  </div>
</template>
<div>
</div>

<script lang="ts">
import {defineComponent, ref} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {searchSupplierBillSchema, supplierBillColumns} from "@/views/report/report.data";
import {Tag} from "ant-design-vue";
import {getSupplierBill, exportSupplierBill} from "@/api/report/report";
import XEUtils from "xe-utils";
import {useModal} from "@/components/Modal";
import SupplierBillDetailModal from "@/views/report/modal/SupplierBillDetailModal.vue";
import printJS from "print-js";
import {useMessage} from "@/hooks/web/useMessage";
import {getTimestamp} from "@/utils/dateUtil";
import {useI18n} from "vue-i18n";
import {useLocaleStore} from "@/store/modules/locale";

export default defineComponent({
  name: 'SupplierBill',
  components: {
    SupplierBillDetailModal, Tag, TableAction, BasicTable},
  setup() {
    const { t } = useI18n();
    const printTableData = ref<any[]>([]);
    const { createMessage } = useMessage();
    const printFirstQuarterPayment = ref(0);
    const printSecondQuarterPayment = ref(0);
    const printThirdQuarterPayment = ref(0);
    const printFourthQuarterPayment = ref(0);
    const printTotalPayment = ref(0);
    const printTotalArrears = ref(0);
    const printRemainingPaymentArrears = ref(0);
    const [handleRegister, {openModal}] = useModal();
    const amountSymbol = ref<string>('')
    const localeStore = useLocaleStore().getLocale;
    if(localeStore === 'zh_CN') {
      amountSymbol.value = '￥'
    } else if (localeStore === 'en') {
      amountSymbol.value = '$'
    }
    const [registerTable, { reload, getForm, getDataSource }] = useTable({
      title: t('reports.supplierBill.title'),
      api: getSupplierBill,
      columns: supplierBillColumns,
      formConfig: {
        labelWidth: 110,
        schemas: searchSupplierBillSchema,
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
      const firstQuarterPayment = tableData.reduce((prev, next) => prev + next.firstQuarterPayment, 0);
      const secondQuarterPayment = tableData.reduce((prev, next) => prev + next.secondQuarterPayment, 0);
      const thirdQuarterPayment = tableData.reduce((prev, next) => prev + next.thirdQuarterPayment, 0);
      const fourthQuarterPayment = tableData.reduce((prev, next) => prev + next.fourthQuarterPayment, 0);
      const totalPayment = tableData.reduce((prev, next) => prev + next.totalPayment, 0);
      const totalArrears = tableData.reduce((prev, next) => prev + next.totalArrears, 0);
      const remainingPaymentArrears = tableData.reduce((prev, next) => prev + next.remainingPaymentArrears, 0);
      printFirstQuarterPayment.value = firstQuarterPayment;
      printSecondQuarterPayment.value = secondQuarterPayment;
      printThirdQuarterPayment.value = thirdQuarterPayment;
      printFourthQuarterPayment.value = fourthQuarterPayment;
      printTotalPayment.value = totalPayment;
      printTotalArrears.value = totalArrears;
      printRemainingPaymentArrears.value = remainingPaymentArrears;
      printTableData.value = tableData;
      return [
        {
          _index: t('reports.supplierBill.table.total'),
          firstQuarterPayment: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(firstQuarterPayment), { digits: 2 })}`,
          secondQuarterPayment: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(secondQuarterPayment), { digits: 2 })}`,
          thirdQuarterPayment: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(thirdQuarterPayment), { digits: 2 })}`,
          fourthQuarterPayment: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(fourthQuarterPayment), { digits: 2 })}`,
          totalPayment: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(totalPayment), { digits: 2 })}`,
          totalArrears: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(totalArrears), { digits: 2 })}`,
          remainingPaymentArrears: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(remainingPaymentArrears), { digits: 2 })}`
        },
      ];
    }
    async function handleSuccess() {
      reload();
    }

    async function handleCancel() {
      reload();
    }

    function openReceipt(supplierId: string) {
      openModal(true, {
        supplierId: supplierId,
      });
    }

    function exportTable() {
      if (getDataSource() === undefined || getDataSource().length === 0) {
        createMessage.warn(t('reports.supplierBill.notice'));
        return;
      }
      const data: any = getForm().getFieldsValue();
      exportSupplierBill(data).then(res => {
        const file: any = res;
        if (file.size > 0) {
          const blob = new Blob([file]);
          const link = document.createElement("a");
          link.href = URL.createObjectURL(blob);
          const timestamp = getTimestamp(new Date());
          link.download = t('reports.supplierBill.data') + timestamp + ".xlsx";
          link.target = "_blank";
          link.click();
        }
      });
    }

    function primaryPrint() {
      printTableData.value.push({
        firstQuarterPayment: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(printFirstQuarterPayment.value), { digits: 2 })}`,
        secondQuarterPayment: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(printSecondQuarterPayment.value), { digits: 2 })}`,
        thirdQuarterPayment: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(printThirdQuarterPayment.value), { digits: 2 })}`,
        fourthQuarterPayment: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(printFourthQuarterPayment.value), { digits: 2 })}`,
        totalPayment: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(printTotalPayment.value), { digits: 2 })}`,
        totalArrears: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(printTotalArrears.value), { digits: 2 })}`,
        remainingPaymentArrears: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(printRemainingPaymentArrears.value), { digits: 2 })}`,
        supplierName: t('reports.supplierBill.table.total'),
        contactName: '',
        contactPhone: '',
        email: '',
      });
      const printColumns = supplierBillColumns.filter(item => item.dataIndex !== 'supplierId');
      printJS({
        documentTitle: "EAIRP " + t('reports.supplierBill.detail'),
        properties: printColumns.map(item => {
          return { field: item.dataIndex, displayName: item.title }
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