<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button @click="exportTable" v-text="t('reports.account.export')"/>
        <a-button @click="primaryPrint" type="primary" v-text="t('reports.account.regularPrint')"/>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'accountId'">
          <a @click="handleAccountFlow(record)"> {{ t('reports.account.table.flow') }} </a>
        </template>
      </template>
    </BasicTable>
    <AccountFlowModal @register="registerModal"/>
  </div>
</template>
<div>
</div>

<script lang="ts">
import {defineComponent, ref} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {accountStatisticsColumns, searchAccountSchema} from "@/views/report/report.data";
import {Tag} from "ant-design-vue";
import {getAccountStatistics, exportAccountStatistics} from "@/api/report/report";
import XEUtils from "xe-utils";
import AccountFlowModal from "@/views/report/modal/AccountFlowModal.vue";
import StockFlowModal from "@/views/report/modal/StockFlowModal.vue";
import {useModal} from "@/components/Modal";
import printJS from "print-js";
import {getTimestamp} from "@/utils/dateUtil";
import {useMessage} from "@/hooks/web/useMessage";
import {useI18n} from "vue-i18n";
import {useLocaleStore} from "@/store/modules/locale";

export default defineComponent({
  name: 'AccountStatistics',
  components: {StockFlowModal, AccountFlowModal, Tag, TableAction, BasicTable},
  setup() {
    const { t } = useI18n();
    const printTableData = ref<any[]>([]);
    const { createMessage } = useMessage();
    const [registerModal, {openModal}] = useModal();
    const printInitialAmount = ref(0);
    const printThisMonthChangeAmount = ref(0);
    const printCurrentAmount = ref(0);
    const amountSymbol = ref<string>('')
    const localeStore = useLocaleStore().getLocale;
    if(localeStore === 'zh_CN') {
      amountSymbol.value = '￥'
    } else if (localeStore === 'en') {
      amountSymbol.value = '$'
    }
    const [registerTable, { reload, getForm, getDataSource }] = useTable({
      title: t('reports.account.title'),
      api: getAccountStatistics,
      rowKey: 'id',
      columns: accountStatisticsColumns,
      formConfig: {
        labelWidth: 110,
        schemas: searchAccountSchema,
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
      const initialAmount = tableData.reduce((prev, next) => prev + next.initialAmount, 0);
      const thisMonthChangeAmount = tableData.reduce((prev, next) => prev + next.thisMonthChangeAmount, 0);
      const currentAmount = tableData.reduce((prev, next) => prev + next.currentAmount, 0);
      printInitialAmount.value = initialAmount;
      printThisMonthChangeAmount.value = thisMonthChangeAmount;
      printCurrentAmount.value = currentAmount;
      printTableData.value = tableData;
      return [
        {
          _index: 'Total',
          initialAmount:  amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(initialAmount), { digits: 2 })}`,
          thisMonthChangeAmount:  amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(thisMonthChangeAmount), { digits: 2 })}`,
          currentAmount:  amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(currentAmount), { digits: 2 })}`
        },
      ];
    }

    function handleAccountFlow(record: Recordable) {
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
        createMessage.warn(t('reports.account.notice'));
        return;
      }
      const data: any = getForm().getFieldsValue();
      exportAccountStatistics(data).then(res => {
        const file: any = res;
        if (file.size > 0) {
          const blob = new Blob([file]);
          const link = document.createElement("a");
          link.href = URL.createObjectURL(blob);
          const timestamp = getTimestamp(new Date());
          link.download = t('reports.account.data') + timestamp + ".xlsx";
          link.target = "_blank";
          link.click();
        }
      });
    }

    function primaryPrint() {
      const printColumns = accountStatisticsColumns.filter(item => item.dataIndex !== 'accountId');
      printTableData.value.push({
        accountName: t('reports.account.table.total'),
        accountNumber: '',
        initialAmount:  amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(printInitialAmount.value), { digits: 2 })}`,
        thisMonthChangeAmount:  amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(printThisMonthChangeAmount.value), { digits: 2 })}`,
        currentAmount:  amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(printCurrentAmount.value), { digits: 2 })}`
      });
      printJS({
        documentTitle: "EAIRP " + t('reports.account.detail'),
        properties: printColumns.map(item => {
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
      registerModal,
      registerTable,
      handleSuccess,
      handleCancel,
      handleAccountFlow,
      exportTable,
      primaryPrint
    }
  }
})
</script>