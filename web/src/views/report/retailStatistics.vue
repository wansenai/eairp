<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button @click="exportTable" v-text="t('reports.retail.export')"/>
        <a-button @click="primaryPrint" type="primary" v-text="t('reports.retail.regularPrint')"/>
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
import {useI18n} from "vue-i18n";

export default defineComponent({
  name: 'RetailStatistics',
  components: {Tag, TableAction, BasicTable},
  setup() {
    const { t } = useI18n();
    const printTableData = ref<any[]>([]);
    const { createMessage } = useMessage();
    const printRetailNumber = ref(0);
    const printRetailAmount = ref(0);
    const printRetailRefundNumber = ref(0);
    const printRetailRefundAmount = ref(0);
    const printRetailLastAmount = ref(0);
    const [registerTable, { reload, getForm, getDataSource }] = useTable({
      title: t('reports.retail.title'),
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
      printRetailNumber.value = retailNumber;
      printRetailAmount.value = retailAmount;
      printRetailRefundNumber.value = retailRefundNumber;
      printRetailRefundAmount.value = retailRefundAmount;
      printTableData.value = tableData;
      return [
        {
          _index: t('reports.retail.table.total'),
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
        createMessage.warn(t('reports.retail.notice'));
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
          link.download = t('reports.retail.data') + timestamp + ".xlsx";
          link.target = "_blank";
          link.click();
        }
      });
    }

    function primaryPrint() {
      printTableData.value.push({
        retailNumber: printRetailNumber.value,
        retailAmount: `￥${XEUtils.commafy(XEUtils.toNumber(printRetailAmount.value), { digits: 2 })}`,
        retailRefundNumber: printRetailRefundNumber.value,
        retailRefundAmount: `￥${XEUtils.commafy(XEUtils.toNumber(printRetailRefundAmount.value), { digits: 2 })}`,
        retailLastAmount: `￥${XEUtils.commafy(XEUtils.toNumber(printRetailLastAmount.value), { digits: 2 })}`,
        productBarcode: t('reports.retail.table.total'),
        warehouseName: '',
        productName: '',
        productStandard: '',
        productModel: '',
        productExtendInfo: '',
        productUnit: '',
      });
      printJS({
        documentTitle: "EAIRP " + t('reports.retail.data'),
        properties: retailStatisticsColumns.map(item => {
          return { field: item.dataIndex, displayName: item.title }
        }),
        printable: printTableData.value,
        gridHeaderStyle: 'border: 1px solid #ddd; font-size: 12px; text-align: center; padding: 8px;',
        gridStyle: 'border: 1px solid #ddd; font-size: 12px; text-align: center; padding: 8px;',
        type: 'json',
      });
      printTableData.value.pop();
    }


    return {
      t,
      registerTable,
      handleSuccess,
      handleCancel,
      exportTable,
      primaryPrint
    }
  }
})
</script>