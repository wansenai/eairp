<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button @click="exportTable" v-text="t('reports.purchase.export')"/>
        <a-button @click="primaryPrint" type="primary" v-text="t('reports.purchase.regularPrint')"/>
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
import {useI18n} from "vue-i18n";
import {useLocaleStore} from "@/store/modules/locale";

export default defineComponent({
  name: 'PurchaseStatistics',
  components: {Tag, TableAction, BasicTable},
  setup() {
    const { t } = useI18n();
    const printTableData = ref<any[]>([]);
    const { createMessage } = useMessage();
    const printPurchaseNumber = ref(0);
    const printPurchaseAmount = ref(0);
    const printPurchaseRefundNumber = ref(0);
    const printPurchaseRefundAmount = ref(0);
    const printPurchaseLastAmount = ref(0);
    const amountSymbol = ref<string>('')
    const localeStore = useLocaleStore().getLocale;
    if(localeStore === 'zh_CN') {
      amountSymbol.value = '￥'
    } else if (localeStore === 'en') {
      amountSymbol.value = '$'
    }
    const [registerTable, { reload, getForm, getDataSource }] = useTable({
      title: t('reports.purchase.title'),
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
      printPurchaseNumber.value = purchaseNumber;
      printPurchaseAmount.value = purchaseAmount;
      printPurchaseRefundNumber.value = purchaseRefundNumber;
      printPurchaseRefundAmount.value = purchaseRefundAmount;
      printPurchaseLastAmount.value = purchaseLastAmount;
      printTableData.value = tableData;
      return [
        {
          _index: t('reports.purchase.table.total'),
          purchaseNumber: purchaseNumber,
          purchaseAmount:  amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(purchaseAmount), { digits: 2 })}`,
          purchaseRefundNumber: purchaseRefundNumber,
          purchaseRefundAmount: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(purchaseRefundAmount), { digits: 2 })}`,
          purchaseLastAmount: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(purchaseLastAmount), { digits: 2 })}`
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
        createMessage.warn(t('reports.purchase.notice'));
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
          link.download = t('reports.purchase.data') + timestamp + ".xlsx";
          link.target = "_blank";
          link.click();
        }
      });
    }

    function primaryPrint() {
      printTableData.value.push({
        purchaseNumber: printPurchaseNumber.value,
        purchaseAmount: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(printPurchaseAmount.value), { digits: 2 })}`,
        purchaseRefundNumber: printPurchaseRefundNumber.value,
        purchaseRefundAmount: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(printPurchaseRefundAmount.value), { digits: 2 })}`,
        purchaseLastAmount: amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(printPurchaseLastAmount.value), { digits: 2 })}`,
        productBarcode: t('reports.purchase.table.total'),
        warehouseName: '',
        productName: '',
        productStandard: '',
        productModel: '',
        productExtendInfo: '',
        productUnit: '',
      });
      printJS({
        documentTitle: "EAIRP " + t('reports.purchase.detail'),
        properties: purchaseStatisticsColumns.map(item => {
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