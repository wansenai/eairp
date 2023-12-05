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
  searchStorageSummarySchema,
  storageSummaryStatisticsColumns
} from "@/views/report/report.data";
import {Tag} from "ant-design-vue";
import {getStorageSummary, exportStorageSummary} from "@/api/report/report";
import XEUtils from "xe-utils";
import printJS from "print-js";
import {useMessage} from "@/hooks/web/useMessage";
import {getTimestamp} from "@/utils/dateUtil";

export default defineComponent({
  name: 'StorageSummaryStatistics',
  components: {Tag, TableAction, BasicTable},
  setup() {
    const printTableData = ref<any[]>([]);
    const {createMessage} = useMessage();
    const [registerTable, {reload, getForm, getDataSource}] = useTable({
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
      printTableData.value = tableData;
      printTableData.value.push({
        storageNumber: storageNumber,
        storageAmount: `￥${XEUtils.commafy(XEUtils.toNumber(storageAmount), {digits: 2})}`,
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
          storageNumber: storageNumber,
          storageAmount: `￥${XEUtils.commafy(XEUtils.toNumber(storageAmount), {digits: 2})}`,
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
      exportStorageSummary(data).then(res => {
        const file: any = res;
        if (file.size > 0) {
          const blob = new Blob([file]);
          const link = document.createElement("a");
          link.href = URL.createObjectURL(blob);
          const timestamp = getTimestamp(new Date());
          link.download = "入库汇总数据" + timestamp + ".xlsx";
          link.target = "_blank";
          link.click();
        }
      });
    }

    function primaryPrint() {
      printJS({
        documentTitle: "EAIRP (入库汇总)",
        properties: storageSummaryStatisticsColumns.map(item => {
          return {field: item.dataIndex, displayName: item.title}
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