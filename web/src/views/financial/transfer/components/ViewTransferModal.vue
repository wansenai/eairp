<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <div class="components-page-header-demo-responsive" style="border: 1px solid rgb(235, 237, 240)">
      <a-page-header
          :title="t('financial.transfer.transferReceiptDetail')"
          :sub-title= "receiptNumber">
        <template #extra>
          <a-button @click="exportTable" v-text="t('financial.transfer.export.name')"/>
          <a-button @click="primaryPrint" type="primary" v-text="t('financial.regularPrint')"/>
          <!--          <a-button key="triplePrint">三联打印</a-button>-->
          <!--          <a-button key="2" type="primary">发起流程审批</a-button>-->
        </template>
        <a-descriptions size="small" :column="3">
          <a-descriptions-item :label="t('financial.transfer.header.receiptDate')">{{ receiptDate }}</a-descriptions-item>
          <a-descriptions-item :label="t('financial.transfer.header.financialPerson')">{{ financialPersonName }}</a-descriptions-item>
          <a-descriptions-item :label="t('financial.transfer.header.paymentAccount')">{{ paymentAccountName }}</a-descriptions-item>
          <a-descriptions-item :label="t('financial.transfer.table.paymentAmount')">{{ paymentAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('financial.transfer.header.remark')">
            {{ remark }}
          </a-descriptions-item>
        </a-descriptions>
        <div class="extra">
          <div
              class="descriptions-context"
              :style="{
              display: 'flex',
              width: 'max-content',
              justifyContent: 'flex-end',
            }"
          >
            <a-statistic
                :title="t('financial.transfer.header.status')"
                :value="status === 1 ? t('sys.table.audited') : t('sys.table.unaudited')"
                :value-style="status === 1 ? { color: '#3f8600' } : { color: '#cf1322' }"
                :style="{
                marginRight: '32px',
                color: 'green',
              }"
            />
            <a-statistic :title="t('financial.transfer.view.amount')"
                         :prefix="amountSymbol"
                         :value-style="status === 1 ? { color: '#3f8600' } : { color: '#cf1322' }"
                         :value="paymentAmount"/>
          </div>
        </div>
      </a-page-header>
      <BasicTable @register="registerTable">
      </BasicTable>
    </div>
  </BasicModal>
</template>
<script lang="ts">
import {defineComponent, ref} from 'vue';
import {BasicTable, useTable} from '/src/components/Table';
import {BasicModal, useModalInner} from "@/components/Modal";
import {getTransferDetailById, exportTransferDetail} from "@/api/financial/transfer";
import {
  Descriptions,
  DescriptionsItem,
  PageHeader,
  Statistic,
} from 'ant-design-vue';
import {transferReceiptTableColumns} from "@/views/financial/transfer/transfer.data";
import printJS from "print-js";
import {getTimestamp} from "@/utils/dateUtil";
import {useI18n} from "vue-i18n";
import {useLocaleStore} from "@/store/modules/locale";

export default defineComponent({
  name: 'ViewTransferModal',
  components: {
    BasicModal,
    BasicTable,
    'a-page-header': PageHeader,
    'a-descriptions': Descriptions,
    'a-descriptions-item': DescriptionsItem,
    'a-statistic': Statistic,
  },
  setup() {
    const { t } = useI18n();
    const receiptNumber = ref('');
    const receiptDate = ref('');
    const financialPersonName = ref('');
    const paymentAccountName = ref('');
    const paymentAmount = ref('');
    const remark = ref('')
    const tableData = ref([]);
    const status = ref(-1);
    const [registerTable] = useTable({
      title: t('financial.transfer.receiptDetail'),
      columns: transferReceiptTableColumns,
      dataSource: tableData,
      pagination: false,
      showIndexColumn: false,
      bordered: true,
      canResize: false,
    });
    const getTitle = ref(t('financial.transfer.transferReceiptDetail'));
    const amountSymbol = ref<string>('')
    const localeStore = useLocaleStore().getLocale;
    if(localeStore === 'zh_CN') {
      amountSymbol.value = '￥'
    } else if (localeStore === 'en') {
      amountSymbol.value = '$'
    }
    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false, destroyOnClose: true, width: 1000, showOkBtn: false});
      const res = await getTransferDetailById(data.id);
      tableData.value = res.data.tableData;
      receiptNumber.value = res.data.receiptNumber;
      financialPersonName.value = res.data.financialPersonName;
      receiptDate.value = res.data.receiptDate;
      paymentAccountName.value = res.data.paymentAccountName;
      paymentAmount.value = res.data.paymentAmount;
      remark.value = res.data.remark;
      status.value = res.data.status;
    });

    function handleSubmit() {
      closeModal();
    }

    async function exportTable() {
      const file: any = await exportTransferDetail(receiptNumber.value)
      if (file.size > 0) {
        const blob = new Blob([file]);
        const link = document.createElement("a");
        const timestamp = getTimestamp(new Date());
        link.href = URL.createObjectURL(blob);
        link.download = t('financial.transfer.receiptDetail') + timestamp + ".xlsx";
        link.target = "_blank";
        link.click();
      }
    }

    const flexContainer = 'display: flex; justify-content: space-between; border-bottom: 1px solid #ddd; padding: 8px;';
    const flexItem = 'display: flex; flex-direction: column; justify-content: space-between; font-size: 12px;';
    function primaryPrint() {
      const header = `
  <div style="${flexContainer}">
    <div style="${flexItem}">${t('financial.transfer.header.receiptNumber')}：${receiptNumber.value}</div>
    <div style="${flexItem}">${t('financial.transfer.header.receiptDate')}：${receiptDate.value}</div>
    <div style="${flexItem}">${t('financial.transfer.table.paymentAmount')}：${paymentAmount.value}</div>
  </div>
  <div style="${flexContainer}">
    <div style="${flexItem}">${t('financial.transfer.header.paymentAccount')}：${paymentAccountName.value}</div>
    <div style="${flexItem}">${t('financial.transfer.header.financialPerson')}：${financialPersonName.value}</div>
    <div style="${flexItem}">${t('financial.transfer.header.remark')}：${remark.value}</div>
  </div>
`;
      printJS({
        documentTitle: "EAIRP " + t('financial.transfer.receiptDetail'),
        header: header,
        properties: transferReceiptTableColumns.map((item) => {
          return {
            field: item.dataIndex,
            displayName: item.title,
          };
        }),
        printable: tableData.value,
        gridHeaderStyle: 'border: 1px solid #ddd; font-size: 12px; text-align: center; padding: 8px;',
        gridStyle: 'border: 1px solid #ddd; font-size: 12px; text-align: center; padding: 8px;',
        type: 'json',
      });
    }

    return {
      t,
      receiptNumber,
      receiptDate,
      financialPersonName,
      paymentAccountName,
      paymentAmount,
      remark,
      status,
      registerTable,
      registerModal,
      getTitle,
      handleSubmit,
      exportTable,
      primaryPrint,
      amountSymbol
    };
  },
});
</script>
<style scoped>

</style>
