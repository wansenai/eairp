<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click=""> 打印</a-button>
        <a-button type="primary" @click=""> 导出</a-button>
      </template>
    </BasicTable>
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

export default defineComponent({
  name: 'productStock',
  components: {Tag, TableAction, BasicTable },
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
      showIndexColumn: true,
      showSummary: true,
      summaryFunc: handleSummary,
    });

    function handleSummary(tableData: Recordable[]) {
      const totalInitStock = tableData.reduce((prev, next) => {
        prev += next.initialStock;
        return prev;
      }, 0);
      const totalCurrentStock = tableData.reduce((prev, next) => {
        prev += next.currentStock;
        return prev;
      }, 0);
      const totalStockAmount = tableData.reduce((prev, next) => {
        prev += next.stockAmount;
        return prev;
      }, 0);
      return [
        {
          _row: '合计',
          _index: '合计',
          initialStock: totalInitStock,
          currentStock: totalCurrentStock,
          stockAmount: `￥${XEUtils.commafy(XEUtils.toNumber(totalStockAmount), { digits: 2 })}`
        },
      ];
    }

    async function handleCreate() {
      openModal(true, {
        isUpdate: false,
      });
    }

    async function handleSuccess() {
      reload();
    }

    async function handleCancel() {
      reload();
    }

    return {
      registerTable,
      registerModal,
      handleCreate,
      handleSuccess,
      handleCancel,
    }
  }
})
</script>