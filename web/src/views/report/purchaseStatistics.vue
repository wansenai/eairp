<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click=""> 导出</a-button>
        <a-button @click=""> 打印</a-button>
      </template>
    </BasicTable>
  </div>
</template>
<div>
</div>

<script lang="ts">
import {defineComponent} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {searchPurchaseSchema, purchaseStatisticsColumns} from "@/views/report/report.data";
import {Tag} from "ant-design-vue";
import {getPurchaseStatistics} from "@/api/report/report";
import XEUtils from "xe-utils";

export default defineComponent({
  name: 'PurchaseStatistics',
  components: {Tag, TableAction, BasicTable},
  setup() {
    const [registerTable, { reload }] = useTable({
      title: '采购统计报表',
      api: getPurchaseStatistics,
      columns: purchaseStatisticsColumns,
      formConfig: {
        labelWidth: 110,
        schemas: searchPurchaseSchema,
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
      const purchaseNumber = tableData.reduce((prev, next) => prev + next.purchaseNumber, 0);
      const purchaseAmount = tableData.reduce((prev, next) => prev + next.purchaseAmount, 0);
      const purchaseRefundNumber = tableData.reduce((prev, next) => prev + next.purchaseRefundNumber, 0);
      const purchaseRefundAmount = tableData.reduce((prev, next) => prev + next.purchaseRefundAmount, 0);
      const purchaseLastAmount = tableData.reduce((prev, next) => prev + next.purchaseLastAmount, 0);
      return [
        {
          _index: '合计',
          purchaseNumber: purchaseNumber,
          purchaseAmount: `￥${XEUtils.commafy(XEUtils.toNumber(purchaseAmount), { digits: 2 })}`,
          purchaseRefundNumber: purchaseRefundNumber,
          purchaseRefundAmount: `￥${XEUtils.commafy(XEUtils.toNumber(purchaseRefundAmount), { digits: 2 })}`,
          purchaseLastAmount: `￥${XEUtils.commafy(XEUtils.toNumber(purchaseLastAmount), { digits: 2 })}`
        },
      ];
    }
    async function handleSuccess() {
      reload();
    }

    async function handleCancel() {
      reload();
    }

    return {
      registerTable,
      handleSuccess,
      handleCancel,
    }
  }
})
</script>