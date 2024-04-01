<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button @click="exportTable" v-text="t('reports.shipmentsSummary.export')"/>
        <a-button @click="primaryPrint" type="primary" v-text="t('reports.shipmentsSummary.regularPrint')"/>
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
import {getShipmentsSummary, exportShipmentsSummary} from "@/api/report/report";
import XEUtils from "xe-utils";
import printJS from "print-js";
import {getTimestamp} from "@/utils/dateUtil";
import {useMessage} from "@/hooks/web/useMessage";
import {useI18n} from "vue-i18n";

export default defineComponent({
  name: 'ShipmentsSummaryStatistics',
  components: {Tag, TableAction, BasicTable},
  setup() {
    const { t } = useI18n();
    const printTableData = ref<any[]>([]);
    const { createMessage } = useMessage();
    const printShipmentsNumber = ref(0);
    const printShipmentsAmount = ref(0);
    const [registerTable, { reload, getForm, getDataSource }] = useTable({
      title: t('reports.shipmentsSummary.title'),
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
      printShipmentsNumber.value = shipmentsNumber;
      printShipmentsAmount.value = shipmentsAmount;
      printTableData.value = tableData;
      return [
        {
          _index: t('reports.shipmentsSummary.table.total'),
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
      if (getDataSource() === undefined || getDataSource().length === 0) {
        createMessage.warn(t('reports.shipmentsSummary.notice'));
        return;
      }
      const data: any = getForm().getFieldsValue();
      exportShipmentsSummary(data).then(res => {
        const file: any = res;
        if (file.size > 0) {
          const blob = new Blob([file]);
          const link = document.createElement("a");
          link.href = URL.createObjectURL(blob);
          const timestamp = getTimestamp(new Date());
          link.download = t('reports.shipmentsSummary.data') + timestamp + ".xlsx";
          link.target = "_blank";
          link.click();
        }
      });
    }

    function primaryPrint() {
      printTableData.value.push({
        shipmentsNumber: printShipmentsNumber.value,
        shipmentsAmount: `￥${XEUtils.commafy(XEUtils.toNumber(printShipmentsAmount.value), { digits: 2 })}`,
        productBarcode: t('reports.shipmentsSummary.table.total'),
        warehouseName: '',
        productName: '',
        productCategoryName: '',
        productStandard: '',
        productModel: '',
        productUnit: '',
        createTime: ''
      });
      printJS({
        documentTitle: "EAIRP " + t('reports.shipmentsSummary.detail'),
        properties: shipmentsSummaryStatisticsColumns.map(item => {
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