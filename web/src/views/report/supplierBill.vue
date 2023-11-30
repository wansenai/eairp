<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button @click="exportTable">导出</a-button>
        <a-button @click="primaryPrint" type="primary">普通打印</a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'supplierId'">
          <a @click="openReceipt(record.supplierId)"> 详情 </a>
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
import {getSupplierBill} from "@/api/report/report";
import XEUtils from "xe-utils";
import {useModal} from "@/components/Modal";
import SupplierBillDetailModal from "@/views/report/modal/SupplierBillDetailModal.vue";
import printJS from "print-js";

export default defineComponent({
  name: 'SupplierBill',
  components: {
    SupplierBillDetailModal, Tag, TableAction, BasicTable},
  setup() {
    const printTableData = ref<any[]>([]);
    const [handleRegister, {openModal}] = useModal();
    const [registerTable, { reload }] = useTable({
      title: '供应商对账报表',
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
      printTableData.value = tableData;
      printTableData.value.push({
        firstQuarterPayment:`￥${XEUtils.commafy(XEUtils.toNumber(firstQuarterPayment), { digits: 2 })}`,
        secondQuarterPayment:`￥${XEUtils.commafy(XEUtils.toNumber(secondQuarterPayment), { digits: 2 })}`,
        thirdQuarterPayment:`￥${XEUtils.commafy(XEUtils.toNumber(thirdQuarterPayment), { digits: 2 })}`,
        fourthQuarterPayment:`￥${XEUtils.commafy(XEUtils.toNumber(fourthQuarterPayment), { digits: 2 })}`,
        totalPayment:`￥${XEUtils.commafy(XEUtils.toNumber(totalPayment), { digits: 2 })}`,
        totalArrears: `￥${XEUtils.commafy(XEUtils.toNumber(totalArrears), { digits: 2 })}`,
        remainingPaymentArrears: `￥${XEUtils.commafy(XEUtils.toNumber(remainingPaymentArrears), { digits: 2 })}`,
        supplierName: '合计',
        contactName: '',
        contactPhone: '',
        email: '',
      });
      return [
        {
          _index: '合计',
          firstQuarterPayment:`￥${XEUtils.commafy(XEUtils.toNumber(firstQuarterPayment), { digits: 2 })}`,
          secondQuarterPayment:`￥${XEUtils.commafy(XEUtils.toNumber(secondQuarterPayment), { digits: 2 })}`,
          thirdQuarterPayment:`￥${XEUtils.commafy(XEUtils.toNumber(thirdQuarterPayment), { digits: 2 })}`,
          fourthQuarterPayment:`￥${XEUtils.commafy(XEUtils.toNumber(fourthQuarterPayment), { digits: 2 })}`,
          totalPayment:`￥${XEUtils.commafy(XEUtils.toNumber(totalPayment), { digits: 2 })}`,
          totalArrears: `￥${XEUtils.commafy(XEUtils.toNumber(totalArrears), { digits: 2 })}`,
          remainingPaymentArrears: `￥${XEUtils.commafy(XEUtils.toNumber(remainingPaymentArrears), { digits: 2 })}`
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
    }

    function primaryPrint() {
      const printColumns = supplierBillColumns.filter(item => item.dataIndex !== 'supplierId');
      printJS({
        documentTitle: "EAIRP (供应商对账)",
        properties: printColumns.map(item => {
          return { field: item.dataIndex, displayName: item.title }
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