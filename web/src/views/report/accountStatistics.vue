<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click=""> 导出</a-button>
        <a-button @click=""> 打印</a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'accountId'">
          <a @click="">流水</a>
        </template>
      </template>
    </BasicTable>
  </div>
</template>
<div>
</div>

<script lang="ts">
import {defineComponent} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {accountStatisticsColumns, searchAccountSchema} from "@/views/report/report.data";
import {Tag} from "ant-design-vue";
import {getAccountStatistics} from "@/api/report/report";
import XEUtils from "xe-utils";

export default defineComponent({
  name: 'AccountStatistics',
  components: {Tag, TableAction, BasicTable},
  setup() {
    const [registerTable, { reload }] = useTable({
      title: '账户统计报表',
      api: getAccountStatistics,
      rowKey: 'id',
      columns: accountStatisticsColumns,
      formConfig: {
        labelWidth: 110,
        schemas: searchAccountSchema,
        autoSubmitOnEnter: true,
      },
      useSearchForm: true,
      bordered: true,
      showTableSetting: true,
      striped: true,
      showIndexColumn: true,
      showSummary: true,
      summaryFunc: handleSummary,
    });

    function handleSummary(tableData: Recordable[]) {
      const initialAmount = tableData.reduce((prev, next) => prev + next.initialAmount, 0);
      const thisMonthChangeAmount = tableData.reduce((prev, next) => prev + next.thisMonthChangeAmount, 0);
      const currentAmount = tableData.reduce((prev, next) => prev + next.currentAmount, 0);
      return [
        {
          _index: '合计',
          initialAmount: `￥${XEUtils.commafy(XEUtils.toNumber(initialAmount), { digits: 2 })}`,
          thisMonthChangeAmount: `￥${XEUtils.commafy(XEUtils.toNumber(thisMonthChangeAmount), { digits: 2 })}`,
          currentAmount: `￥${XEUtils.commafy(XEUtils.toNumber(currentAmount), { digits: 2 })}`
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
      handleCancel
    }
  }
})
</script>