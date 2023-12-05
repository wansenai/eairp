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
import {searchRetailSchema, retailStatisticsColumns} from "@/views/report/report.data";
import {Tag} from "ant-design-vue";
import {getRetailStatistics, exportRetailStatistics} from "@/api/report/report";
import XEUtils from "xe-utils";
import printJS from "print-js";
import {getTimestamp} from "@/utils/dateUtil";
import {useMessage} from "@/hooks/web/useMessage";

export default defineComponent({
  name: 'RetailStatistics',
  components: {Tag, TableAction, BasicTable},
  setup() {
    const printTableData = ref<any[]>([]);
    const { createMessage } = useMessage();
    const [registerTable, { reload, getForm, getDataSource }] = useTable({
      title: '零售统计报表',
      api: getRetailStatistics,
      columns: retailStatisticsColumns,
      formConfig: {
        labelWidth: 110,
        schemas: searchRetailSchema,
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
      const retailNumber = tableData.reduce((prev, next) => prev + next.retailNumber, 0);
      const retailAmount = tableData.reduce((prev, next) => prev + next.retailAmount, 0);
      const retailRefundNumber = tableData.reduce((prev, next) => prev + next.retailRefundNumber, 0);
      const retailRefundAmount = tableData.reduce((prev, next) => prev + next.retailRefundAmount, 0);
      const retailLastAmount = tableData.reduce((prev, next) => prev + next.retailLastAmount, 0);
      // 将数据写入到printTableData里面
      printTableData.value = tableData;
      printTableData.value.push({
        retailNumber: retailNumber,
        retailAmount: `￥${XEUtils.commafy(XEUtils.toNumber(retailAmount), { digits: 2 })}`,
        retailRefundNumber: retailRefundNumber,
        retailRefundAmount: `￥${XEUtils.commafy(XEUtils.toNumber(retailRefundAmount), { digits: 2 })}`,
        retailLastAmount: `￥${XEUtils.commafy(XEUtils.toNumber(retailLastAmount), { digits: 2 })}`,
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
          retailNumber: retailNumber,
          retailAmount: `￥${XEUtils.commafy(XEUtils.toNumber(retailAmount), { digits: 2 })}`,
          retailRefundNumber: retailRefundNumber,
          retailRefundAmount: `￥${XEUtils.commafy(XEUtils.toNumber(retailRefundAmount), { digits: 2 })}`,
          retailLastAmount: `￥${XEUtils.commafy(XEUtils.toNumber(retailLastAmount), { digits: 2 })}`
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
      exportRetailStatistics(data).then(res => {
        const file: any = res;
        if (file.size > 0) {
          const blob = new Blob([file]);
          const link = document.createElement("a");
          link.href = URL.createObjectURL(blob);
          const timestamp = getTimestamp(new Date());
          link.download = "零售统计数据" + timestamp + ".xlsx";
          link.target = "_blank";
          link.click();
        }
      });
    }

    function primaryPrint() {
      printJS({
        documentTitle: "EAIRP (零售统计-详情)",
        properties: retailStatisticsColumns.map(item => {
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