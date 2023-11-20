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
import {searchRetailSchema, retailStatisticsColumns} from "@/views/report/report.data";
import {Tag} from "ant-design-vue";
import {getRetailStatistics} from "@/api/report/report";
import XEUtils from "xe-utils";

export default defineComponent({
  name: 'RetailStatistics',
  components: {Tag, TableAction, BasicTable},
  setup() {
    const [registerTable, { reload }] = useTable({
      title: '零售统计报表',
      api: getRetailStatistics,
      columns: retailStatisticsColumns,
      formConfig: {
        labelWidth: 110,
        schemas: searchRetailSchema,
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
      const retailNumber = tableData.reduce((prev, next) => prev + next.retailNumber, 0);
      const retailAmount = tableData.reduce((prev, next) => prev + next.retailAmount, 0);
      const retailRefundNumber = tableData.reduce((prev, next) => prev + next.retailRefundNumber, 0);
      const retailRefundAmount = tableData.reduce((prev, next) => prev + next.retailRefundAmount, 0);
      const retailLastAmount = tableData.reduce((prev, next) => prev + next.retailLastAmount, 0);
      return [
        {
          _index: '合计',
          retailNumber: retailNumber,
          retailAmount: `￥${XEUtils.commafy(XEUtils.toNumber(retailAmount), { digits: 2 })}`,
          retailRefundNumber: retailRefundNumber,
          retailRefundAmount: `￥${XEUtils.commafy(XEUtils.toNumber(retailRefundAmount), { digits: 2 })}`,
          retailLastAmount: `￥${XEUtils.commafy(XEUtils.toNumber(retailLastAmount), { digits: 2 })}`
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