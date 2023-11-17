<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click=""> 导出</a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'id'">
          <a @click="handleStockFlow(record)">流水</a>
        </template>
      </template>
    </BasicTable>
    <StockFlowModal @register="registerModal"/>
  </div>
</template>
<div>
</div>

<script lang="ts">
import {defineComponent} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {useModal} from "@/components/Modal";
import {productStockColumns, searchProductStockSchema} from "@/views/report/report.data";
import {Tag} from "ant-design-vue";
import {getProductStock} from "@/api/report/report";
import XEUtils from "xe-utils";
import StockFlowModal from "@/views/report/modal/StockFlowModal.vue";
export default defineComponent({
  name: 'ProductStock',
  components: {Tag, TableAction, BasicTable, StockFlowModal},
  setup() {
    const [registerModal, {openModal}] = useModal();
    const [registerTable, { reload }] = useTable({
      title: '商品库存报表',
      api: getProductStock,
      rowKey: 'id',
      columns: productStockColumns,
      formConfig: {
        labelWidth: 110,
        schemas: searchProductStockSchema,
        autoSubmitOnEnter: true,
      },
      useSearchForm: true,
      bordered: true,
      showTableSetting: true,
      striped: true,
      showIndexColumn: false,
      showSummary: true,
      summaryFunc: handleSummary,
    });

    function handleSummary(tableData: Recordable[]) {
      const totalInitStock = tableData.reduce((prev, next) => prev + next.initialStock, 0);
      const totalCurrentStock = tableData.reduce((prev, next) => prev + next.currentStock, 0);
      const totalStockAmount = tableData.reduce((prev, next) => prev + next.stockAmount, 0);
      return [
        {
          _index: '合计',
          initialStock: totalInitStock,
          currentStock: totalCurrentStock,
          stockAmount: `￥${XEUtils.commafy(XEUtils.toNumber(totalStockAmount), { digits: 2 })}`
        },
      ];
    }

    function handleStockFlow(record: Recordable) {
      openModal(true, record);
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
      registerModal,
      handleStockFlow
    }
  }
})
</script>