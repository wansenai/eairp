<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button @click="exportTable" v-text="t('reports.storageSummary.export')"/>
        <a-button @click="primaryPrint" type="primary" v-text="t('reports.storageSummary.regularPrint')"/>
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
import {useI18n} from "vue-i18n";
import {useLocaleStore} from "@/store/modules/locale";

export default defineComponent({
  name: 'StorageSummaryStatistics',
  components: {Tag, TableAction, BasicTable},
  setup() {
    const { t } = useI18n();
    const printTableData = ref<any[]>([]);
    const {createMessage} = useMessage();
    const printStorageNumber = ref(0);
    const printStorageAmount = ref(0);
    const amountSymbol = ref<string>('')
    const localeStore = useLocaleStore().getLocale;
    if(localeStore === 'zh_CN') {
      amountSymbol.value = '￥'
    } else if (localeStore === 'en') {
      amountSymbol.value = '$'
    }
    const [registerTable, {reload, getForm, getDataSource}] = useTable({
      title: t('reports.storageSummary.title'),
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
      printStorageNumber.value = storageNumber;
      printStorageAmount.value = storageAmount;
      printTableData.value = tableData;
      return [
        {
          _index: t('reports.storageSummary.table.total'),
          storageNumber: storageNumber,
          storageAmount: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(storageAmount), {digits: 2})}`,
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
        createMessage.warn(t('reports.storageSummary.notice'));
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
          link.download = t('reports.storageSummary.data') + timestamp + ".xlsx";
          link.target = "_blank";
          link.click();
        }
      });
    }

    function primaryPrint() {
      printTableData.value.push({
        storageNumber: printStorageNumber.value,
        storageAmount: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(printStorageAmount.value), {digits: 2})}`,
        productBarcode: t('reports.storageSummary.table.total'),
        warehouseName: '',
        productName: '',
        productCategoryName: '',
        productStandard: '',
        productModel: '',
        productUnit: '',
        createTime: ''
      });
      printJS({
        documentTitle: "EAIRP " + t('reports.storageSummary.detail'),
        properties: storageSummaryStatisticsColumns.map(item => {
          return {field: item.dataIndex, displayName: item.title}
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