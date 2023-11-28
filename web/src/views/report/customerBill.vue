<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click=""> 导出</a-button>
        <a-button @click=""> 打印</a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'customerId'">
          <a @click="openReceipt(record.customerId)"> 详情 </a>
        </template>
      </template>
    </BasicTable>
    <CustomerBillDetailModal @register="handleRegister" />
  </div>
</template>
<div>
</div>

<script lang="ts">
import {defineComponent} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {customerBillColumns, searchCustomerBillSchema} from "@/views/report/report.data";
import {Tag} from "ant-design-vue";
import {getCustomerBill} from "@/api/report/report";
import XEUtils from "xe-utils";
import {useModal} from "@/components/Modal";
import CustomerBillDetailModal from "@/views/report/modal/CustomerBillDetailModal.vue";

export default defineComponent({
  name: 'CustomerBill',
  components: {
    CustomerBillDetailModal, Tag, TableAction, BasicTable},
  setup() {
    const [handleRegister, {openModal}] = useModal();
    const [registerTable, { reload }] = useTable({
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
      return [
        {
          _index: '合计',
          firstQuarterReceivable:`￥${XEUtils.commafy(XEUtils.toNumber(firstQuarterReceivable), { digits: 2 })}`,
          secondQuarterReceivable:`￥${XEUtils.commafy(XEUtils.toNumber(secondQuarterReceivable), { digits: 2 })}`,
          thirdQuarterReceivable:`￥${XEUtils.commafy(XEUtils.toNumber(thirdQuarterReceivable), { digits: 2 })}`,
          fourthQuarterReceivable:`￥${XEUtils.commafy(XEUtils.toNumber(fourthQuarterReceivable), { digits: 2 })}`,
          totalQuarterArrears:`￥${XEUtils.commafy(XEUtils.toNumber(totalQuarterArrears), { digits: 2 })}`,
          totalQuarterReceivable: `￥${XEUtils.commafy(XEUtils.toNumber(totalQuarterReceivable), { digits: 2 })}`,
          remainingReceivableArrears: `￥${XEUtils.commafy(XEUtils.toNumber(remainingReceivableArrears), { digits: 2 })}`
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

    return {
      openReceipt,
      registerTable,
      handleSuccess,
      handleCancel,
      handleRegister,
    }
  }
})
</script>