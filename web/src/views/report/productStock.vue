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
import {getProductStock, exportProductStock} from "@/api/report/report";
import XEUtils from "xe-utils";
import StockFlowModal from "@/views/report/modal/StockFlowModal.vue";
import printJS from "print-js";
import {useMessage} from "@/hooks/web/useMessage";
import {getTimestamp} from "@/utils/dateUtil";
export default defineComponent({
  name: 'ProductStock',
  components: {Tag, TableAction, BasicTable, StockFlowModal},
  setup() {
    const [registerModal, {openModal}] = useModal();
    const printTableData = ref<any[]>([]);
    const { createMessage } = useMessage();
    const printTotalInitStock = ref(0);
    const printTotalCurrentStock = ref(0);
    const printTotalStockAmount = ref(0);
    const [registerTable, { reload, getForm, getDataSource }] = useTable({
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
      printTotalInitStock.value = totalInitStock;
      printTotalCurrentStock.value = totalCurrentStock;
      printTotalStockAmount.value = totalStockAmount;
      printTableData.value = tableData;
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
      if (getDataSource() === undefined || getDataSource().length === 0) {
        createMessage.warn('当前查询条件下无数据可导出');
        return;
      }
      const data: any = getForm().getFieldsValue();
      exportProductStock(data).then(res => {
        const file: any = res;
        if (file.size > 0) {
          const blob = new Blob([file]);
          const link = document.createElement("a");
          link.href = URL.createObjectURL(blob);
          const timestamp = getTimestamp(new Date());
          link.download = "商品库存数据" + timestamp + ".xlsx";
          link.target = "_blank";
          link.click();
        }
      });
    }

    function primaryPrint() {
      const printColumns = productStockColumns.filter(item => item.dataIndex !== 'productId' && item.dataIndex !== 'warehouseId'
          && item.dataIndex !== 'id');
      printTableData.value.push({
        initialStock: printTotalInitStock.value,
        currentStock: printTotalCurrentStock.value,
        stockAmount: `￥${XEUtils.commafy(XEUtils.toNumber(printTotalStockAmount.value), { digits: 2 })}`,
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

      // 移除最后一条数据
      printTableData.value.pop();
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