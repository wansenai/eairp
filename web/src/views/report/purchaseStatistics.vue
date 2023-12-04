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
import {searchPurchaseSchema, purchaseStatisticsColumns} from "@/views/report/report.data";
import {Tag} from "ant-design-vue";
import {getPurchaseStatistics, exportPurchaseStatistics} from "@/api/report/report";
import XEUtils from "xe-utils";
import printJS from "print-js";
import {getTimestamp} from "@/utils/dateUtil";
import {useMessage} from "@/hooks/web/useMessage";

export default defineComponent({
  name: 'PurchaseStatistics',
  components: {Tag, TableAction, BasicTable},
  setup() {
    const printTableData = ref<any[]>([]);
    const { createMessage } = useMessage();
    const [registerTable, { reload, getForm, getDataSource }] = useTable({
      title: '采购统计报表',
      api: getPurchaseStatistics,
      columns: purchaseStatisticsColumns,
      formConfig: {
        labelWidth: 110,
        schemas: searchPurchaseSchema,
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
      const purchaseNumber = tableData.reduce((prev, next) => prev + next.purchaseNumber, 0);
      const purchaseAmount = tableData.reduce((prev, next) => prev + next.purchaseAmount, 0);
      const purchaseRefundNumber = tableData.reduce((prev, next) => prev + next.purchaseRefundNumber, 0);
      const purchaseRefundAmount = tableData.reduce((prev, next) => prev + next.purchaseRefundAmount, 0);
      const purchaseLastAmount = tableData.reduce((prev, next) => prev + next.purchaseLastAmount, 0);
      printTableData.value = tableData;
      printTableData.value.push({
        purchaseNumber: purchaseNumber,
        purchaseAmount: `￥${XEUtils.commafy(XEUtils.toNumber(purchaseAmount), { digits: 2 })}`,
        purchaseRefundNumber: purchaseRefundNumber,
        purchaseRefundAmount: `￥${XEUtils.commafy(XEUtils.toNumber(purchaseRefundAmount), { digits: 2 })}`,
        purchaseLastAmount: `￥${XEUtils.commafy(XEUtils.toNumber(purchaseLastAmount), { digits: 2 })}`,
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
          purchaseNumber: purchaseNumber,
          purchaseAmount: `￥${XEUtils.commafy(XEUtils.toNumber(purchaseAmount), { digits: 2 })}`,
          purchaseRefundNumber: purchaseRefundNumber,
          purchaseRefundAmount: `￥${XEUtils.commafy(XEUtils.toNumber(purchaseRefundAmount), { digits: 2 })}`,
          purchaseLastAmount: `￥${XEUtils.commafy(XEUtils.toNumber(purchaseLastAmount), { digits: 2 })}`
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
      exportPurchaseStatistics(data).then(res => {
        const file: any = res;
        if (file.size > 0) {
          const blob = new Blob([file]);
          const link = document.createElement("a");
          link.href = URL.createObjectURL(blob);
          const timestamp = getTimestamp(new Date());
          link.download = "采购统计数据" + timestamp + ".xlsx";
          link.target = "_blank";
          link.click();
        }
      });
    }

    function primaryPrint() {
      printJS({
        documentTitle: "EAIRP (采购统计-详情)",
        properties: purchaseStatisticsColumns.map(item => {
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