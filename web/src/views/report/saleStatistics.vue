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
import {searchSalesSchema, salesStatisticsColumns} from "@/views/report/report.data";
import {Tag} from "ant-design-vue";
import {getSalesStatistics, exportSalesStatistics} from "@/api/report/report";
import XEUtils from "xe-utils";
import printJS from "print-js";
import {useMessage} from "@/hooks/web/useMessage";
import {getTimestamp} from "@/utils/dateUtil";

export default defineComponent({
  name: 'SalesStatistics',
  components: {Tag, TableAction, BasicTable},
  setup() {
    const printTableData = ref<any[]>([]);
    const { createMessage } = useMessage();
    const [registerTable, { reload, getForm, getDataSource }] = useTable({
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
      printTableData.value = tableData;
      printTableData.value.push({
        salesNumber: salesNumber,
        salesAmount: `￥${XEUtils.commafy(XEUtils.toNumber(salesAmount), { digits: 2 })}`,
        salesRefundNumber: salesRefundNumber,
        salesRefundAmount: `￥${XEUtils.commafy(XEUtils.toNumber(salesRefundAmount), { digits: 2 })}`,
        salesLastAmount: `￥${XEUtils.commafy(XEUtils.toNumber(salesLastAmount), { digits: 2 })}`,
        productBarcode: '合计',
        warehouseName: '',
        productName: '',
        productStandard: '',
        productModel: '',
        productExtendInfo: '',
        productUnit: '',
      });
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

    function exportTable() {
      if (getDataSource() === undefined || getDataSource().length === 0) {
        createMessage.warn('当前查询条件下无数据可导出');
        return;
      }
      const data: any = getForm().getFieldsValue();
      exportSalesStatistics(data).then(res => {
        const file: any = res;
        if (file.size > 0) {
          const blob = new Blob([file]);
          const link = document.createElement("a");
          link.href = URL.createObjectURL(blob);
          const timestamp = getTimestamp(new Date());
          link.download = "销售统计数据" + timestamp + ".xlsx";
          link.target = "_blank";
          link.click();
        }
      });
    }

    function primaryPrint() {
      printJS({
        documentTitle: "EAIRP (销售统计-详情)",
        properties: salesStatisticsColumns.map(item => {
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