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
import {searchStorageSummarySchema, storageSummaryStatisticsColumns} from "@/views/report/report.data";
import {Tag} from "ant-design-vue";
import {getStorageSummary} from "@/api/report/report";
import XEUtils from "xe-utils";

export default defineComponent({
  name: 'StorageSummaryStatistics',
  components: {Tag, TableAction, BasicTable},
  setup() {
    const [registerTable, { reload }] = useTable({
      title: '入库汇总报表',
      api: getStorageSummary,
      columns: storageSummaryStatisticsColumns,
      formConfig: {
        labelWidth: 110,
        schemas: searchStorageSummarySchema,
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
      const storageNumber = tableData.reduce((prev, next) => prev + next.storageNumber, 0);
      const storageAmount = tableData.reduce((prev, next) => prev + next.storageAmount, 0);
      return [
        {
          _index: '合计',
          storageNumber: storageNumber,
          storageAmount: `￥${XEUtils.commafy(XEUtils.toNumber(storageAmount), { digits: 2 })}`,
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