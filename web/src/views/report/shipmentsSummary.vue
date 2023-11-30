<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button @click="exportTable">导出</a-button>
        <a-button @click="primaryPrint" type="primary">普通打印</a-button>
      </template>
    </BasicTable>
  </div>
</template>
<div>
</div>

<script lang="ts">
import {defineComponent, ref} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {
  searchShipmentsSummarySchema,
  shipmentsSummaryStatisticsColumns
} from "@/views/report/report.data";
import {Tag} from "ant-design-vue";
import {getShipmentsSummary} from "@/api/report/report";
import XEUtils from "xe-utils";
import printJS from "print-js";

export default defineComponent({
  name: 'ShipmentsSummaryStatistics',
  components: {Tag, TableAction, BasicTable},
  setup() {
    const printTableData = ref<any[]>([]);
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
      printTableData.value = tableData;
      printTableData.value.push({
        shipmentsNumber: shipmentsNumber,
        shipmentsAmount: `￥${XEUtils.commafy(XEUtils.toNumber(shipmentsAmount), { digits: 2 })}`,
        productBarcode: '合计',
        warehouseName: '',
        productName: '',
        productCategoryName: '',
        productStandard: '',
        productModel: '',
        productUnit: '',
        createTime: ''
      });
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

    function exportTable() {
    }

    function primaryPrint() {
      printJS({
        documentTitle: "EAIRP (出库汇总)",
        properties: shipmentsSummaryStatisticsColumns.map(item => {
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
      exportTable,
      primaryPrint
    }
  }
})
</script>