<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button @click="exportTable">导出</a-button>
        <a-button @click="primaryPrint" type="primary">普通打印</a-button>
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
import {defineComponent, ref} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {useModal} from "@/components/Modal";
import {productStockColumns, searchProductStockSchema} from "@/views/report/report.data";
import {Tag} from "ant-design-vue";
import {getProductStock} from "@/api/report/report";
import XEUtils from "xe-utils";
import StockFlowModal from "@/views/report/modal/StockFlowModal.vue";
import printJS from "print-js";
export default defineComponent({
  name: 'ProductStock',
  components: {Tag, TableAction, BasicTable, StockFlowModal},
  setup() {
    const [registerModal, {openModal}] = useModal();
    const printTableData = ref<any[]>([]);
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
      canResize: false,
      showSummary: true,
      summaryFunc: handleSummary,
    });

    function handleSummary(tableData: Recordable[]) {
      const totalInitStock = tableData.reduce((prev, next) => prev + next.initialStock, 0);
      const totalCurrentStock = tableData.reduce((prev, next) => prev + next.currentStock, 0);
      const totalStockAmount = tableData.reduce((prev, next) => prev + next.stockAmount, 0);
      // 将数据写入到printTableData里面
      printTableData.value = tableData;
      printTableData.value.push({
        initialStock: totalInitStock,
        currentStock: totalCurrentStock,
        stockAmount: `￥${XEUtils.commafy(XEUtils.toNumber(totalStockAmount), { digits: 2 })}`,
        productBarcode: '合计',
        // 将其他字段写成空字符串 为了打印的时候不显示
        warehouseName: '',
        productName: '',
        productCategoryName: '',
        productStandard: '',
        productModel: '',
        productWeight: '',
        productColor: '',
        productUnit: '',
        warehouseShelves: '',
        unitPrice: ''
      });
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

    function exportTable() {

    }

    function primaryPrint() {
      const printColumns = productStockColumns.filter(item => item.dataIndex !== 'productId' && item.dataIndex !== 'warehouseId'
          && item.dataIndex !== 'id');
      printJS({
        documentTitle: "EAIRP (商品库存-详情)",
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
      registerTable,
      handleSuccess,
      handleCancel,
      registerModal,
      handleStockFlow,
      exportTable,
      primaryPrint
    }
  }
})
</script>