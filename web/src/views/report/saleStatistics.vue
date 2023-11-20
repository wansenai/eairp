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
import {searchSalesSchema, salesStatisticsColumns} from "@/views/report/report.data";
import {Tag} from "ant-design-vue";
import {getSalesStatistics} from "@/api/report/report";
import XEUtils from "xe-utils";

export default defineComponent({
  name: 'SalesStatistics',
  components: {Tag, TableAction, BasicTable},
  setup() {
    const [registerTable, { reload }] = useTable({
      title: '销售统计报表',
      api: getSalesStatistics,
      columns: salesStatisticsColumns,
      formConfig: {
        labelWidth: 110,
        schemas: searchSalesSchema,
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
      const salesNumber = tableData.reduce((prev, next) => prev + next.salesNumber, 0);
      const salesAmount = tableData.reduce((prev, next) => prev + next.salesAmount, 0);
      const salesRefundNumber = tableData.reduce((prev, next) => prev + next.salesRefundNumber, 0);
      const salesRefundAmount = tableData.reduce((prev, next) => prev + next.salesRefundAmount, 0);
      const salesLastAmount = tableData.reduce((prev, next) => prev + next.salesLastAmount, 0);
      return [
        {
          _index: '合计',
          salesNumber: salesNumber,
          salesAmount: `￥${XEUtils.commafy(XEUtils.toNumber(salesAmount), { digits: 2 })}`,
          salesRefundNumber: salesRefundNumber,
          salesRefundAmount: `￥${XEUtils.commafy(XEUtils.toNumber(salesRefundAmount), { digits: 2 })}`,
          salesLastAmount: `￥${XEUtils.commafy(XEUtils.toNumber(salesLastAmount), { digits: 2 })}`
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