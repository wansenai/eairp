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
import {searchShipmentsSummarySchema, shipmentsSummaryStatisticsColumns} from "@/views/report/report.data";
import {Tag} from "ant-design-vue";
import {getShipmentsSummary} from "@/api/report/report";
import XEUtils from "xe-utils";

export default defineComponent({
  name: 'ShipmentsSummaryStatistics',
  components: {Tag, TableAction, BasicTable},
  setup() {
    const [registerTable, { reload }] = useTable({
      title: '出库汇总报表',
      api: getShipmentsSummary,
      columns: shipmentsSummaryStatisticsColumns,
      formConfig: {
        labelWidth: 110,
        schemas: searchShipmentsSummarySchema,
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
      const shipmentsNumber = tableData.reduce((prev, next) => prev + next.shipmentsNumber, 0);
      const shipmentsAmount = tableData.reduce((prev, next) => prev + next.shipmentsAmount, 0);
      return [
        {
          _index: '合计',
          shipmentsNumber: shipmentsNumber,
          shipmentsAmount: `￥${XEUtils.commafy(XEUtils.toNumber(shipmentsAmount), { digits: 2 })}`,
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